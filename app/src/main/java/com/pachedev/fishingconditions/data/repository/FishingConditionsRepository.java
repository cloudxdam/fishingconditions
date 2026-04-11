package com.pachedev.fishingconditions.data.repository;

import com.pachedev.fishingconditions.data.network.MarineApiService;
import com.pachedev.fishingconditions.data.network.MarineRetrofitInstance;
import com.pachedev.fishingconditions.data.network.WeatherApiService;
import com.pachedev.fishingconditions.data.network.WeatherRetrofitInstance;
import com.pachedev.fishingconditions.model.domain.FishingConditionsData;
import com.pachedev.fishingconditions.model.domain.MoonPhase;
import com.pachedev.fishingconditions.model.marine.MarineResponse;
import com.pachedev.fishingconditions.model.weather.WeatherResponse;
import com.pachedev.fishingconditions.utils.MoonPhaseCalculator;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FishingConditionsRepository {

    private final WeatherApiService weatherApiService;
    private final MarineApiService marineApiService;

    public FishingConditionsRepository() {
        weatherApiService = WeatherRetrofitInstance
                .getRetrofitInstance()
                .create(WeatherApiService.class);
        marineApiService = MarineRetrofitInstance
                .getRetrofitInstance()
                .create(MarineApiService.class);
    }

    public void getFishingConditions(FishingCoinditionsCallback callback) {
        Call<WeatherResponse> weathercall = weatherApiService.getWeatherData(
                28.12,
                -16.73,
                "temperature_2m,wind_speed_10m",
                "sunrise,sunset",
                "Atlantic/Canary"
        );

        weathercall.enqueue(new Callback<WeatherResponse>() {
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

    private void loadMarineData (WeatherResponse weatherResponse, FishingCoinditionsCallback callback) {
        Call<MarineResponse> marineCall = marineApiService.getMarineData(
                28.12,
                -17.73,
                "wave_height,wave_period",
                "Atlantic/Canary"
        );

        marineCall.enqueue(new Callback<MarineResponse>() {
            @Override
            public void onResponse(Call<MarineResponse> call, Response<MarineResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MarineResponse marineResponse = response.body();
                    MoonPhase moonPhase = MoonPhaseCalculator.calculateMoonPhase(LocalDate.now());
                    FishingConditionsData fishingConditionsData = new FishingConditionsData(
                            weatherResponse.getHourly().getTemperature2m().get(0),
                            weatherResponse.getHourly().getWindSpeed10m().get(0),
                            weatherResponse.getDaily().getSunrise().get(0),
                            weatherResponse.getDaily().getSunset().get(0),
                            marineResponse.getHourly().getWaveHeight().get(0),
                            marineResponse.getHourly().getWavePeriod().get(0),
                            moonPhase
                    );

                    callback.onSuccess(fishingConditionsData);
                } else {
                    callback.onError("Marine request failer: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MarineResponse> call, Throwable t) {
                callback.onError("Marine request failef" + t.getMessage());
            }
        });
    }

    public interface FishingCoinditionsCallback {
        void onSuccess(FishingConditionsData fishingConditionsData);
        void onError(String errorMessage);
    }
}
