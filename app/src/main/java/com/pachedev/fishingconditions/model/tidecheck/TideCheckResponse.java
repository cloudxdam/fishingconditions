package com.pachedev.fishingconditions.model.tidecheck;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Tide forecast response from TideCheck API.
 */
public class TideCheckResponse {

    @SerializedName("extremes")
    private List<TideCheckExtreme> extremes;

    public List<TideCheckExtreme> getExtremes() {
        return extremes;
    }
}