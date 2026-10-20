package com.pachedev.fishingconditions.model.domain;

/**
 * Combined data used by the UI to display current fishing conditions.
 */
public class FishingConditionsData {

    private final Double temperature;
    private final Double windSpeed;
    private final String sunrise;
    private final String sunset;
    private final Double waveHeight;
    private final Double wavePeriod;
    private final MoonPhase moonPhase;
    private final TideInfo tideInfo;
    private final int fishingScore;

    private final Double windDirection;

    public FishingConditionsData(Double temperature,
                                 Double windSpeed,
                                 String sunrise,
                                 String sunset,
                                 Double waveHeight,
                                 Double wavePeriod,
                                 MoonPhase moonPhase,
                                 TideInfo tideInfo,
                                 int fishingScore,
                                 Double windDirection) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.waveHeight = waveHeight;
        this.wavePeriod = wavePeriod;
        this.moonPhase = moonPhase;
        this.tideInfo = tideInfo;
        this.fishingScore = fishingScore;
        this.windDirection = windDirection;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public Double getWaveHeight() {
        return waveHeight;
    }

    public Double getWavePeriod() {
        return wavePeriod;
    }

    public MoonPhase getMoonPhase() {
        return moonPhase;
    }

    public TideInfo getTideInfo() {
        return tideInfo;
    }

    public int getFishingScore() {
        return fishingScore;
    }

    public Double getWindDirection() {
        return windDirection;
    }
}
