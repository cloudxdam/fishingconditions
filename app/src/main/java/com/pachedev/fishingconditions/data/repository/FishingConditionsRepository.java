package com.pachedev.fishingconditions.data.repository;

import com.pachedev.fishingconditions.data.network.MarineApiService;
import com.pachedev.fishingconditions.data.network.MarineRetrofitInstance;
import com.pachedev.fishingconditions.data.network.TideApiService;
import com.pachedev.fishingconditions.data.network.TideRetrofitInstance;
import com.pachedev.fishingconditions.data.network.WeatherApiService;
import com.pachedev.fishingconditions.data.network.WeatherRetrofitInstance;
import com.pachedev.fishingconditions.model.domain.FishingConditionsData;
import com.pachedev.fishingconditions.model.domain.MoonPhase;
import com.pachedev.fishingconditions.model.domain.TideInfo;
import com.pachedev.fishingconditions.model.marine.MarineResponse;
import com.pachedev.fishingconditions.model.tides.TideExtreme;
import com.pachedev.fishingconditions.model.tides.TideResponse;
import com.pachedev.fishingconditions.model.tides.TideState;
import com.pachedev.fishingconditions.model.weather.WeatherResponse;
import com.pachedev.fishingconditions.utils.MoonPhaseCalculator;

import java.time.LocalDate;
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

    public void getFishingConditions(FishingConditionsCallback callback) {
        Call<WeatherResponse> weatherCall = weatherApiService.getWeatherData(
                28.12,
                -16.73,
                "temperature_2m,wind_speed_10m",
                "sunrise,sunset",
                "Atlantic/Canary"
        );

        weatherCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    loadMarineData(weatherResponse, callback);
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

    private void loadMarineData(WeatherResponse weatherResponse,
                                FishingConditionsCallback callback) {
        Call<MarineResponse> marineCall = marineApiService.getMarineData(
                28.12,
                -16.73,
                "wave_height,wave_period",
                "Atlantic/Canary"
        );

        marineCall.enqueue(new Callback<MarineResponse>() {
            @Override
            public void onResponse(Call<MarineResponse> call, Response<MarineResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MarineResponse marineResponse = response.body();
                    loadTideData(weatherResponse, marineResponse, callback);
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

    private void loadTideData(WeatherResponse weatherResponse,
                              MarineResponse marineResponse,
                              FishingConditionsCallback callback) {
        Call<TideResponse> tideCall = tideApiService.getTides(
                28.12,
                -16.73
        );

        tideCall.enqueue(new Callback<TideResponse>() {
            @Override
            public void onResponse(Call<TideResponse> call, Response<TideResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TideResponse tideResponse = response.body();

                    TideInfo tideInfo = buildTideInfo(tideResponse.getExtremes());

                    buildFishingConditions(weatherResponse, marineResponse, tideInfo, callback);
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
                                        FishingConditionsCallback callback) {

        MoonPhase moonPhase = MoonPhaseCalculator.calculateMoonPhase(LocalDate.now());

        FishingConditionsData fishingConditionsData = new FishingConditionsData(
                weatherResponse.getHourly().getTemperature2m().get(0),
                weatherResponse.getHourly().getWindSpeed10m().get(0),
                weatherResponse.getDaily().getSunrise().get(0),
                weatherResponse.getDaily().getSunset().get(0),
                marineResponse.getHourly().getWaveHeight().get(0),
                marineResponse.getHourly().getWavePeriod().get(0),
                moonPhase,
                tideInfo
        );

        callback.onSuccess(fishingConditionsData);
    }

    public interface FishingConditionsCallback {
        void onSuccess(FishingConditionsData fishingConditionsData);

        void onError(String errorMessage);
    }
}