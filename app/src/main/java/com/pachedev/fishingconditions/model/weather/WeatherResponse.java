package com.pachedev.fishingconditions.model.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Main weather response from Open-Meteo API.
 *
 * Contains hourly and daily weather data.
 */
public class WeatherResponse {

    /** Hourly weather data */
    @SerializedName("hourly")
    private WeatherHourlyData hourly;

    /** Daily weather data */
    @SerializedName("daily")
    private WeatherDailyData daily;

    public WeatherHourlyData getHourly() {
        return hourly;
    }

    public WeatherDailyData getDaily() {
        return daily;
    }
}