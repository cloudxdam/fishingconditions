package com.pachedev.fishingconditions.data.network;

import com.pachedev.fishingconditions.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit service interface for Open-Meteo Weather API.
 */
public interface OpenMeteoApiService {

    @GET("forecast")
    Call<WeatherResponse> getWeatherData(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("hourly") String hourly,
            @Query("daily") String daily,
            @Query("timezone") String timezone
    );
}