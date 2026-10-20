package com.pachedev.fishingconditions.model.domain;

/**
 * Tide information used by the app.
 */
public class TideInfo {

    private final String nextHighTideTime;
    private final Double nextHighTideHeight;
    private final String nextLowTideTime;
    private final Double nextLowTideHeight;

    public TideInfo(String nextHighTideTime, Double nextHighTideHeight, String nextLowTideTime, Double nextLowTideHeight) {
        this.nextHighTideTime = nextHighTideTime;
        this.nextHighTideHeight = nextHighTideHeight;
        this.nextLowTideTime = nextLowTideTime;
        this.nextLowTideHeight = nextLowTideHeight;
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
