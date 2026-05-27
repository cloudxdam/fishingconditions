package com.pachedev.fishingconditions.model.tides;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the possible tide states.
 */
public enum TideState {

    @SerializedName("HIGH TIDE")
    HIGH_TIDE,

    @SerializedName("LOW TIDE")
    LOW_TIDE
}