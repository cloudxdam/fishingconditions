package com.pachedev.fishingconditions.model.domain;

/**
 * Represents a fishing spot
 */
public class FishingSpot {

    private String name;
    private Double latitude;
    private Double longitude;

    public FishingSpot(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return name;
    }
}
