package com.pachedev.fishingconditions.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.pachedev.fishingconditions.data.local.TideDao;
import com.pachedev.fishingconditions.data.local.TideDatabase;
import com.pachedev.fishingconditions.data.local.TideEntity;
import com.pachedev.fishingconditions.data.network.MarineApiService;
import com.pachedev.fishingconditions.data.network.MarineRetrofitInstance;
import com.pachedev.fishingconditions.data.network.TideCheckApiService;
import com.pachedev.fishingconditions.data.network.TideCheckRetrofitInstance;
import com.pachedev.fishingconditions.data.network.WeatherApiService;
import com.pachedev.fishingconditions.data.network.WeatherRetrofitInstance;
import com.pachedev.fishingconditions.model.domain.FishingConditionsData;
import com.pachedev.fishingconditions.model.domain.FishingSpot;
import com.pachedev.fishingconditions.model.domain.MoonPhase;
import com.pachedev.fishingconditions.model.domain.TideInfo;
import com.pachedev.fishingconditions.model.marine.MarineResponse;
import com.pachedev.fishingconditions.model.tidecheck.TideCheckExtreme;
import com.pachedev.fishingconditions.model.tidecheck.TideCheckNearestStationResponse;
import com.pachedev.fishingconditions.model.tidecheck.TideCheckResponse;
import com.pachedev.fishingconditions.model.weather.WeatherResponse;
import com.pachedev.fishingconditions.utils.FishingScoreCalculator;
import com.pachedev.fishingconditions.utils.MoonPhaseCalculator;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository responsible for loading and combining fishing condition data.
 *
 * It retrieves weather, marine and tide information from remote APIs, caches tide
 * data locally with Room, and builds a single FishingConditionsData object for the UI.
 */
public class FishingConditionsRepository {

    private final WeatherApiService weatherApiService;
    private final MarineApiService marineApiService;
    private final TideCheckApiService tideCheckApiService;
    private final TideDao tideDao;
    private final ExecutorService databaseExecutor;
    private final Handler mainHandler;

    /**
     * Creates a repository instance and initializes API services and local cache access.
     *
     * @param context application context used to access the Room database
     */
    public FishingConditionsRepository(Context context) {
        weatherApiService = WeatherRetrofitInstance
                .getRetrofitInstance()
                .create(WeatherApiService.class);

        marineApiService = MarineRetrofitInstance
                .getRetrofitInstance()
                .create(MarineApiService.class);

        tideCheckApiService = TideCheckRetrofitInstance
                .getRetrofitInstance()
                .create(TideCheckApiService.class);

        tideDao = TideDatabase
                .getInstance(context)
                .tideDao();

        databaseExecutor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Retrieves fishing conditions for the selected spot and date-time.
     *
     * @param spot selected fishing spot
     * @param selectedDateTime selected date and time
     * @param callback callback used to return the result or an error
     */
    public void getFishingConditions(FishingSpot spot,
                                     LocalDateTime selectedDateTime,
                                     FishingConditionsCallback callback) {
        String selectedDate = selectedDateTime.toLocalDate().toString();

        Call<WeatherResponse> weatherCall = weatherApiService.getWeatherData(
                spot.getLatitude(),
                spot.getLongitude(),
                "temperature_2m,wind_speed_10m,wind_direction_10m",
                "sunrise,sunset",
                "Atlantic/Canary",
                selectedDate,
                selectedDate
        );

        weatherCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call,
                                   Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loadMarineData(
                            spot,
                            selectedDateTime,
                            response.body(),
                            callback
                    );
                } else {
                    callback.onError("Weather response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                callback.onError("Weather request failed: " + t.getMessage());
            }
        });
    }

    /**
     * Loads marine data after weather data has been retrieved successfully.
     *
     * @param spot selected fishing spot
     * @param selectedDateTime selected date and time
     * @param weatherResponse weather API response
     * @param callback callback used to return the final result
     */
    private void loadMarineData(FishingSpot spot,
                                LocalDateTime selectedDateTime,
                                WeatherResponse weatherResponse,
                                FishingConditionsCallback callback) {
        String selectedDate = selectedDateTime.toLocalDate().toString();

        Call<MarineResponse> marineCall = marineApiService.getMarineData(
                spot.getLatitude(),
                spot.getLongitude(),
                "wave_height,wave_period",
                "Atlantic/Canary",
                selectedDate,
                selectedDate
        );

        marineCall.enqueue(new Callback<MarineResponse>() {
            @Override
            public void onResponse(Call<MarineResponse> call,
                                   Response<MarineResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loadTideData(
                            spot,
                            selectedDateTime,
                            weatherResponse,
                            response.body(),
                            callback
                    );
                } else {
                    callback.onError("Marine response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MarineResponse> call, Throwable t) {
                callback.onError("Marine request failed: " + t.getMessage());
            }
        });
    }

    /**
     * Loads tide data using a cache-first strategy.
     *
     * If tide data for the selected spot and date exists in Room, it is reused.
     * Otherwise, the data is requested from TideCheck.
     *
     * @param spot selected fishing spot
     * @param selectedDateTime selected date and time
     * @param weatherResponse weather API response
     * @param marineResponse marine API response
     * @param callback callback used to return the final result
     */
    private void loadTideData(FishingSpot spot,
                              LocalDateTime selectedDateTime,
                              WeatherResponse weatherResponse,
                              MarineResponse marineResponse,
                              FishingConditionsCallback callback) {
        String selectedDate = selectedDateTime.toLocalDate().toString();

        databaseExecutor.execute(() -> {
            TideEntity cachedTide = tideDao.findBySpotAndDate(
                    spot.getLatitude(),
                    spot.getLongitude(),
                    selectedDate
            );

            if (cachedTide != null) {
                TideInfo tideInfo = new TideInfo(
                        cachedTide.getNextHighTideTime(),
                        cachedTide.getNextHighTideHeight(),
                        cachedTide.getNextLowTideTime(),
                        cachedTide.getNextLowTideHeight()
                );

                mainHandler.post(() -> buildFishingConditions(
                        weatherResponse,
                        marineResponse,
                        tideInfo,
                        selectedDateTime,
                        callback
                ));
            } else {
                loadTideDataFromApi(
                        spot,
                        selectedDateTime,
                        weatherResponse,
                        marineResponse,
                        callback
                );
            }
        });
    }

    /**
     * Requests tide data from TideCheck when no cached data is available.
     *
     * @param spot selected fishing spot
     * @param selectedDateTime selected date and time
     * @param weatherResponse weather API response
     * @param marineResponse marine API response
     * @param callback callback used to return the final result
     */
    private void loadTideDataFromApi(FishingSpot spot,
                                     LocalDateTime selectedDateTime,
                                     WeatherResponse weatherResponse,
                                     MarineResponse marineResponse,
                                     FishingConditionsCallback callback) {

        Call<List<TideCheckNearestStationResponse>> stationCall =
                tideCheckApiService.getNearestStations(
                        spot.getLatitude(),
                        spot.getLongitude()
                );

        stationCall.enqueue(new Callback<List<TideCheckNearestStationResponse>>() {
            @Override
            public void onResponse(Call<List<TideCheckNearestStationResponse>> call,
                                   Response<List<TideCheckNearestStationResponse>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && !response.body().isEmpty()) {

                    String stationId = response.body().get(0).getId();

                    loadTideForecast(
                            spot,
                            stationId,
                            selectedDateTime,
                            weatherResponse,
                            marineResponse,
                            callback
                    );
                } else {
                    callback.onError("Tide station response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<TideCheckNearestStationResponse>> call,
                                  Throwable t) {
                callback.onError("Tide station request failed: " + t.getMessage());
            }
        });
    }

    /**
     * Loads tide forecast data for the nearest TideCheck station.
     *
     * @param spot selected fishing spot
     * @param stationId TideCheck station identifier
     * @param selectedDateTime selected date and time
     * @param weatherResponse weather API response
     * @param marineResponse marine API response
     * @param callback callback used to return the final result
     */
    private void loadTideForecast(FishingSpot spot,
                                  String stationId,
                                  LocalDateTime selectedDateTime,
                                  WeatherResponse weatherResponse,
                                  MarineResponse marineResponse,
                                  FishingConditionsCallback callback) {

        String selectedDate = selectedDateTime.toLocalDate().toString();

        Call<TideCheckResponse> tideCall =
                tideCheckApiService.getTides(
                        stationId,
                        "LAT",
                        2,
                        selectedDate
                );

        tideCall.enqueue(new Callback<TideCheckResponse>() {
            @Override
            public void onResponse(Call<TideCheckResponse> call,
                                   Response<TideCheckResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    TideInfo tideInfo = buildTideInfo(
                            response.body().getExtremes(),
                            selectedDateTime
                    );

                    saveTideCache(spot, selectedDate, tideInfo);

                    buildFishingConditions(
                            weatherResponse,
                            marineResponse,
                            tideInfo,
                            selectedDateTime,
                            callback
                    );
                } else {
                    callback.onError("Tide forecast response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TideCheckResponse> call,
                                  Throwable t) {
                callback.onError("Tide forecast request failed: " + t.getMessage());
            }
        });
    }

    /**
     * Saves tide information in the local Room cache.
     *
     * @param spot selected fishing spot
     * @param selectedDate selected date
     * @param tideInfo tide information to cache
     */
    private void saveTideCache(FishingSpot spot,
                               String selectedDate,
                               TideInfo tideInfo) {
        databaseExecutor.execute(() -> {
            TideEntity tideEntity = new TideEntity(
                    spot.getLatitude(),
                    spot.getLongitude(),
                    selectedDate,
                    tideInfo.getNextHighTideTime(),
                    tideInfo.getNextHighTideHeight(),
                    tideInfo.getNextLowTideTime(),
                    tideInfo.getNextLowTideHeight()
            );

            tideDao.insert(tideEntity);
        });
    }

    /**
     * Converts TideCheck tide extremes into the app domain tide model.
     *
     * It selects the next high tide and next low tide after the selected date-time.
     *
     * @param extremes TideCheck tide extremes
     * @param selectedDateTime selected date and time
     * @return tide information used by the app
     */
    private TideInfo buildTideInfo(List<TideCheckExtreme> extremes,
                                   LocalDateTime selectedDateTime) {
        TideCheckExtreme nextHighTide = null;
        TideCheckExtreme nextLowTide = null;

        for (TideCheckExtreme extreme : extremes) {
            LocalDateTime tideDateTime = OffsetDateTime
                    .parse(extreme.getTime())
                    .atZoneSameInstant(ZoneId.of("Atlantic/Canary"))
                    .toLocalDateTime();

            if (tideDateTime.isBefore(selectedDateTime)) {
                continue;
            }

            if ("high".equalsIgnoreCase(extreme.getType()) && nextHighTide == null) {
                nextHighTide = extreme;
            } else if ("low".equalsIgnoreCase(extreme.getType()) && nextLowTide == null) {
                nextLowTide = extreme;
            }

            if (nextHighTide != null && nextLowTide != null) {
                break;
            }
        }

        return new TideInfo(
                nextHighTide != null ? nextHighTide.getTime() : null,
                nextHighTide != null ? nextHighTide.getHeight() : null,
                nextLowTide != null ? nextLowTide.getTime() : null,
                nextLowTide != null ? nextLowTide.getHeight() : null
        );
    }

    /**
     * Builds the final fishing conditions object from weather, marine, tide and moon data.
     *
     * @param weatherResponse weather API response
     * @param marineResponse marine API response
     * @param tideInfo tide information
     * @param selectedDateTime selected date and time
     * @param callback callback used to return the result
     */
    private void buildFishingConditions(WeatherResponse weatherResponse,
                                        MarineResponse marineResponse,
                                        TideInfo tideInfo,
                                        LocalDateTime selectedDateTime,
                                        FishingConditionsCallback callback) {

        MoonPhase moonPhase = MoonPhaseCalculator.calculateMoonPhase(
                selectedDateTime.toLocalDate()
        );

        int weatherIndex;
        int marineIndex;

        try {
            weatherIndex = findHourlyIndex(
                    weatherResponse.getHourly().getTime(),
                    selectedDateTime
            );

            marineIndex = findHourlyIndex(
                    marineResponse.getHourly().getTime(),
                    selectedDateTime
            );
        } catch (IllegalArgumentException e) {
            callback.onError(e.getMessage());
            return;
        }

        Double temperature = weatherResponse.getHourly().getTemperature2m().get(weatherIndex);
        Double windSpeed = weatherResponse.getHourly().getWindSpeed10m().get(weatherIndex);
        Double waveHeight = marineResponse.getHourly().getWaveHeight().get(marineIndex);
        Double wavePeriod = marineResponse.getHourly().getWavePeriod().get(marineIndex);
        Double windDirection = weatherResponse.getHourly().getWindDirection10m().get(weatherIndex);

        int fishingScore = FishingScoreCalculator.calculateScore(
                windSpeed,
                waveHeight,
                wavePeriod,
                moonPhase,
                tideInfo,
                selectedDateTime
        );

        FishingConditionsData fishingConditionsData = new FishingConditionsData(
                temperature,
                windSpeed,
                weatherResponse.getDaily().getSunrise().get(0),
                weatherResponse.getDaily().getSunset().get(0),
                waveHeight,
                wavePeriod,
                moonPhase,
                tideInfo,
                fishingScore,
                windDirection
        );

        callback.onSuccess(fishingConditionsData);
    }

    /**
     * Finds the index of the hourly data matching the selected date and time.
     *
     * The selected date-time is rounded to the start of the hour before
     * searching for an exact match in the API response timestamps.
     *
     * @param times list of hourly timestamps returned by the API
     * @param selectedDateTime selected date and time
     * @return index of the matching hourly data
     * @throws IllegalArgumentException if no matching timestamp is found
     */
    private int findHourlyIndex(List<String> times, LocalDateTime selectedDateTime) {
        String targetTime = selectedDateTime
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toString();

        int index = times.indexOf(targetTime);

        if (index == -1) {
            throw new IllegalArgumentException(
                    "No hourly data found for selected time: " + targetTime
            );
        }

        return index;
    }

    /**
     * Callback used to return fishing conditions or an error message.
     */
    public interface FishingConditionsCallback {
        void onSuccess(FishingConditionsData fishingConditionsData);

        void onError(String errorMessage);
    }
}