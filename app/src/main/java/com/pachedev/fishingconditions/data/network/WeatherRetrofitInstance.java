package com.pachedev.fishingconditions.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides a Retrofit instance for API calls.
 *
 * Uses a Singleton pattern so the same instance is reused.
 */
public class WeatherRetrofitInstance {

    private static final String BASE_URL = "https://api.open-meteo.com/v1/";

    private static Retrofit retrofit;

    private WeatherRetrofitInstance() {
        // Prevent instantiation
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}