package com.pachedev.fishingconditions.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Cached tide data for a spot and date.
 */
@Entity(tableName = "tide_cache")
public class TideEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private double latitude;
    private double longitude;
    private String date;

    private String nextHighTideTime;
    private Double nextHighTideHeight;
    private String nextLowTideTime;
    private Double nextLowTideHeight;

    public TideEntity(double latitude,
                      double longitude,
                      String date,
                      String nextHighTideTime,
                      Double nextHighTideHeight,
                      String nextLowTideTime,
                      Double nextLowTideHeight) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.nextHighTideTime = nextHighTideTime;
        this.nextHighTideHeight = nextHighTideHeight;
        this.nextLowTideTime = nextLowTideTime;
        this.nextLowTideHeight = nextLowTideHeight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDate() {
        return date;
    }

    public String getNextHighTideTime() {
        return nextHighTideTime;
    }

    public Double getNextHighTideHeight() {
        return nextHighTideHeight;
    }

    public String getNextLowTideTime() {
        return nextLowTideTime;
    }

    public Double getNextLowTideHeight() {
        return nextLowTideHeight;
    }
}