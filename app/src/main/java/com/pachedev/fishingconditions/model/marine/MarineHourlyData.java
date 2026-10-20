package com.pachedev.fishingconditions.model.marine;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Hourly marine data from Open-Meteo Marine API.
 *
 * Each list contains values for the same hour.
 */
public class MarineHourlyData {

    /** Hour timestamps (e.g. "2026-04-09T00:00") */
    @SerializedName("time")
    private List<String> time;

    /** Wave height in meters */
    @SerializedName("wave_height")
    private List<Double> waveHeight;

    /** Wave period in seconds */
    @SerializedName("wave_period")
    private List<Double> wavePeriod;

    public List<String> getTime() {
        return time;
    }

    public List<Double> getWaveHeight() {
        return waveHeight;
    }

    public List<Double> getWavePeriod() {
        return wavePeriod;
    }
}