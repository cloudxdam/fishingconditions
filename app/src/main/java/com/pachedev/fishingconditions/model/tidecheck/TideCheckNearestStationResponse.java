package com.pachedev.fishingconditions.model.tidecheck;

import com.google.gson.annotations.SerializedName;

/**
 * Nearest tide station response from TideCheck API.
 */
public class TideCheckNearestStationResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}