package com.pachedev.fishingconditions.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Daily weather data from Open-Meteo API.
 *
 * Each list contains values for the same day.
 */
public class DailyData {

    /** Dates (e.g. "2026-04-05") */
    @SerializedName("time")
    private List<String> time;

    /** Sunrise time for each day */
    @SerializedName("sunrise")
    private List<String> sunrise;

    /** Sunset time for each day */
    @SerializedName("sunset")
    private List<String> sunset;

    public List<String> getTime() {
        return time;
    }

    public List<String> getSunrise() {
        return sunrise;
    }

    public List<String> getSunset() {
        return sunset;
    }
}