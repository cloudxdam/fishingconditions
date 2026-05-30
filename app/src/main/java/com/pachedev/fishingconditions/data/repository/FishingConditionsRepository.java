package com.pachedev.fishingconditions.data.repository;

import com.pachedev.fishingconditions.data.network.MarineApiService;
import com.pachedev.fishingconditions.data.network.MarineRetrofitInstance;
import com.pachedev.fishingconditions.data.network.WeatherApiService;
import com.pachedev.fishingconditions.data.network.WeatherRetrofitInstance;
import com.pachedev.fishingconditions.model.domain.FishingConditionsData;
import com.pachedev.fishingconditions.model.domain.FishingSpot;
import com.pachedev.fishingconditions.model.domain.MoonPhase;
import com.pachedev.fishingconditions.model.domain.TideInfo;
import com.pachedev.fishingconditions.model.marine.MarineResponse;
import com.pachedev.fishingconditions.model.weather.WeatherResponse;
import com.pachedev.fishingconditions.utils.FishingScoreCalculator;
import com.pachedev.fishingconditions.utils.MoonPhaseCalculator;
import com.pachedev.fishingconditions.data.network.TideCheckApiService;
import com.pachedev.fishingconditions.data.network.TideCheckRetrofitInstance;
import com.pachedev.fishingconditions.model.tidecheck.TideCheckExtreme;
import com.pachedev.fishingconditions.model.tidecheck.TideCheckNearestStationResponse;
import com.pachedev.fishingconditions.model.tidecheck.TideCheckResponse;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FishingConditionsRepository {

    private final WeatherApiService weatherApiService;
    private final MarineApiService marineApiService;
    private final TideCheckApiService tideCheckApiService;

    public FishingConditionsRepository() {
        weatherApiService = WeatherRetrofitInstance
                .getRetrofitInstance()
                .create(WeatherApiService.class);

        marineApiService = MarineRetrofitInstance
                .getRetrofitInstance()
                .create(MarineApiService.class);

        tideCheckApiService = TideCheckRetrofitInstance
                .getRetrofitInstance()
                .create(TideCheckApiService.class);
    }

    public void getFishingConditions(FishingSpot spot, LocalDateTime selectedDateTime, FishingConditionsCallback callback) {
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
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    loadMarineData(
                            spot,
                            selectedDateTime,
                            weatherResponse,
                            callback);
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

    private void loadMarineData(FishingSpot spot, LocalDateTime selectedDateTime, WeatherResponse weatherResponse,
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
            public void onResponse(Call<MarineResponse> call, Response<MarineResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MarineResponse marineResponse = response.body();
                    loadTideData(
                            spot,
                            selectedDateTime,
                            weatherResponse,
                            marineResponse,
                            callback);
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

    private void loadTideData(FishingSpot spot,
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
                            stationId,
                            selectedDateTime,
                            weatherResponse,
                            marineResponse,
                            callback
                    );

                    android.util.Log.d("DATE_DEBUG", "Tide start date: " + selectedDateTime);

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

    private void loadTideForecast(String stationId,
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

    private void buildFishingConditions(WeatherResponse weatherResponse,
                                        MarineResponse marineResponse,
                                        TideInfo tideInfo,
                                        LocalDateTime selectedDateTime,
                                        FishingConditionsCallback callback) {

        android.util.Log.d("DATE_DEBUG", "SelectedDateTime: " + selectedDateTime);
        android.util.Log.d("DATE_DEBUG", "Moon date: " + selectedDateTime.toLocalDate());

        MoonPhase moonPhase = MoonPhaseCalculator.calculateMoonPhase(selectedDateTime.toLocalDate());

        int weatherIndex = findHourlyIndex(
                weatherResponse.getHourly().getTime(),
                selectedDateTime
        );

        int marineIndex = findHourlyIndex(
                marineResponse.getHourly().getTime(),
                selectedDateTime
        );

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

    public interface FishingConditionsCallback {
        void onSuccess(FishingConditionsData fishingConditionsData);

        void onError(String errorMessage);
    }

    private int findHourlyIndex(List<String> times, LocalDateTime selectedDateTime) {
        String targetTime = selectedDateTime
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .toString();

        int index = times.indexOf(targetTime);

        if (index != -1) {
            return index;
        }

        return 0;
    }
}