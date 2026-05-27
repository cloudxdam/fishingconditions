package com.pachedev.fishingconditions.model.tides;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a tide extreme event (high tide or low tide).
 */
public class TideExtreme {

    /** Tide height in meters */
    @SerializedName("height")
    private Double height;

    /** Tide state (HIGH_TIDE / LOW_TIDE) */
    @SerializedName("state")
    private TideState state;

    /** Date and time of the tide event */
    @SerializedName("datetime")
    private String dateTime;

    public Double getHeight() {
        return height;
    }

    public TideState getState() {
        return state;
    }

    public String getDateTime() {
        return dateTime;
    }
}