package com.pachedev.fishingconditions.model.marine;

import com.google.gson.annotations.SerializedName;

/**
 * Main response from Open-Meteo Marine API.
 *
 * Contains hourly marine data.
 */
public class MarineResponse {

    /** Hourly marine data */
    @SerializedName("hourly")
    private MarineHourlyData hourly;

    public MarineHourlyData getHourly() {
        return hourly;
    }
}