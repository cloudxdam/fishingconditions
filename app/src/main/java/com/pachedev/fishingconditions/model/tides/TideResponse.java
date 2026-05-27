package com.pachedev.fishingconditions.model.tides;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Main response from Marea API.
 *
 * Contains tide extremes.
 */
public class TideResponse {

    /** High and low tide events */
    @SerializedName("extremes")
    private List<TideExtreme> extremes;

    public List<TideExtreme> getExtremes() {
        return extremes;
    }
}
