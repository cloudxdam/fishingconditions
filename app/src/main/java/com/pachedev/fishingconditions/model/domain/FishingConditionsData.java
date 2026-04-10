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

    public FishingConditionsData(Double temperature, Double windSpeed, String sunrise, String sunset, Double waveHeight, Double wavePeriod) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.waveHeight = waveHeight;
        this.wavePeriod = wavePeriod;
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
}
