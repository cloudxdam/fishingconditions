package com.pachedev.fishingconditions.model.tidecheck;

import com.google.gson.annotations.SerializedName;

/**
 * Tide extreme data from TideCheck API.
 */
public class TideCheckExtreme {

    @SerializedName("time")
    private String time;

    @SerializedName("height")
    private Double height;

    @SerializedName("type")
    private String type;

    public String getTime() {
        return time;
    }

    public Double getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }
}