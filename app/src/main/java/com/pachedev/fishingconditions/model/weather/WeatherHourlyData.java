package com.pachedev.fishingconditions.model.weather;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Hourly weather data from Open-Meteo API.
 *
 * Each list contains values for the same hours.
 * For example, index 0 in all lists refers to the same time.
 */
public class WeatherHourlyData {

    /** Hour timestamps (e.g. "2026-04-05T00:00") */
    @SerializedName("time")
    private List<String> time;

    /** Temperature at 2 meters (°C) */
    @SerializedName("temperature_2m")
    private List<Double> temperature2m;

    /** Wind speed at 10 meters */
    @SerializedName("wind_speed_10m")
    private List<Double> windSpeed10m;

    public List<String> getTime() {
        return time;
    }

    public List<Double> getTemperature2m() {
        return temperature2m;
    }

    public List<Double> getWindSpeed10m() {
        return windSpeed10m;
    }
}