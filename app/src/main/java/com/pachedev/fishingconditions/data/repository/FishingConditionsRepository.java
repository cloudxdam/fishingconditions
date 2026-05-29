package com.pachedev.fishingconditions.data.repository;

import com.pachedev.fishingconditions.data.network.MarineApiService;
import com.pachedev.fishingconditions.data.network.MarineRetrofitInstance;
import com.pachedev.fishingconditions.data.network.TideApiService;
import com.pachedev.fishingconditions.data.network.TideRetrofitInstance;
import com.pachedev.fishingconditions.data.network.WeatherApiService;
import com.pachedev.fishingconditions.data.network.WeatherRetrofitInstance;
import com.pachedev.fishingconditions.model.domain.FishingConditionsData;
import com.pachedev.fishingconditions.model.domain.FishingSpot;
import com.pachedev.fishingconditions.model.domain.MoonPhase;
import com.pachedev.fishingconditions.model.domain.TideInfo;
import com.pachedev.fishingconditions.model.marine.MarineResponse;
import com.pachedev.fishingconditions.model.tides.TideExtreme;
import com.pachedev.fishingconditions.model.tides.TideResponse;
import com.pachedev.fishingconditions.model.tides.TideState;
import com.pachedev.fishingconditions.model.weather.WeatherResponse;
import com.pachedev.fishingconditions.utils.MoonPhaseCalculator;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FishingConditionsRepository {

    private final WeatherApiService weatherApiService;
    private final MarineApiService marineApiService;
    private final TideApiService tideApiService;

    public FishingConditionsRepository() {
        weatherApiService = WeatherRetrofitInstance
                .getRetrofitInstance()
                .create(WeatherApiService.class);

        marineApiService = MarineRetrofitInstance
                .getRetrofitInstance()
                .create(MarineApiService.class);

        tideApiService = TideRetrofitInstance
                .getInstance()
                .create(TideApiService.class);
    }

    public void getFishingConditions(FishingSpot spot, LocalDateTime selectedDateTime, FishingConditionsCallback callback) {
        String selectedDate = selectedDateTime.toLocalDate().toString();

        Call<WeatherResponse> weatherCall = weatherApiService.getWeatherData(
                spot.getLatitude(),
                spot.getLongitude(),
                "temperature_2m,wind_speed_10m",
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
        Call<TideResponse> tideCall = tideApiService.getTides(
                spot.getLatitude(),
                spot.getLongitude()
        );

        tideCall.enqueue(new Callback<TideResponse>() {
            @Override
            public void onResponse(Call<TideResponse> call, Response<TideResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TideResponse tideResponse = response.body();

                    TideInfo tideInfo = buildTideInfo(tideResponse.getExtremes());

                    buildFishingConditions(
                            weatherResponse,
                            marineResponse,
                            tideInfo,
                            selectedDateTime,
                            callback);
                } else {
                    callback.onError("Tide response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TideResponse> call, Throwable t) {
                callback.onError("Tide request failed: " + t.getMessage());
            }
        });
    }

    private TideInfo buildTideInfo(List<TideExtreme> extremes) {
        TideExtreme nextHighTide = null;
        TideExtreme nextLowTide = null;

        for (TideExtreme extreme : extremes) {
            if (extreme.getState() == TideState.HIGH_TIDE && nextHighTide == null) {
                nextHighTide = extreme;
            } else if (extreme.getState() == TideState.LOW_TIDE && nextLowTide == null) {
                nextLowTide = extreme;
            }

            if (nextHighTide != null && nextLowTide != null) {
                break;
            }
        }

        return new TideInfo(
                nextHighTide != null ? nextHighTide.getDateTime() : null,
                nextHighTide != null ? nextHighTide.getHeight() : null,
                nextLowTide != null ? nextLowTide.getDateTime() : null,
                nextLowTide != null ? nextLowTide.getHeight() : null
        );
    }

    private void buildFishingConditions(WeatherResponse weatherResponse,
                                        MarineResponse marineResponse,
                                        TideInfo tideInfo,
                                        LocalDateTime selectedDateTime,
                                        FishingConditionsCallback callback) {

        MoonPhase moonPhase = MoonPhaseCalculator.calculateMoonPhase(selectedDateTime.toLocalDate());

        int weatherIndex = findHourlyIndex(
                weatherResponse.getHourly().getTime(),
                selectedDateTime
        );

        int marineIndex = findHourlyIndex(
                marineResponse.getHourly().getTime(),
                selectedDateTime
        );

        FishingConditionsData fishingConditionsData = new FishingConditionsData(
                weatherResponse.getHourly().getTemperature2m().get(weatherIndex),
                weatherResponse.getHourly().getWindSpeed10m().get(weatherIndex),
                weatherResponse.getDaily().getSunrise().get(0),
                weatherResponse.getDaily().getSunset().get(0),
                marineResponse.getHourly().getWaveHeight().get(marineIndex),
                marineResponse.getHourly().getWavePeriod().get(marineIndex),
                moonPhase,
                tideInfo
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