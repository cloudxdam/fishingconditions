package com.pachedev.fishingconditions.model;

import com.google.gson.annotations.SerializedName;

/**
 * Main weather response from Open-Meteo API.
 *
 * Contains hourly and daily weather data.
 */
public class WeatherResponse {

    /** Hourly weather data */
    @SerializedName("hourly")
    private HourlyData hourly;

    /** Daily weather data */
    @SerializedName("daily")
    private DailyData daily;

    public HourlyData getHourly() {
        return hourly;
    }

    public DailyData getDaily() {
        return daily;
    }
}