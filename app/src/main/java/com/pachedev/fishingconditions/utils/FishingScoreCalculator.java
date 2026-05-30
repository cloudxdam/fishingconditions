package com.pachedev.fishingconditions.utils;

import com.pachedev.fishingconditions.model.domain.MoonPhase;
import com.pachedev.fishingconditions.model.domain.TideInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Calculates a fishing score based on weather,
 * sea, tide and moon conditions.
 */
public class FishingScoreCalculator {

    private FishingScoreCalculator() {
        // Prevent instantiation
    }

    /**
     * Calculates the fishing score for the given conditions.
     *
     * @param windSpeed wind speed in km/h
     * @param waveHeight wave height in meters
     * @param wavePeriod wave period in seconds
     * @param moonPhase moon phase
     * @param tideInfo tide information
     * @param selectedDateTime selected date and time
     * @return fishing score from 0 to 100
     */
    public static int calculateScore(
            double windSpeed,
            double waveHeight,
            double wavePeriod,
            MoonPhase moonPhase,
            TideInfo tideInfo,
            LocalDateTime selectedDateTime) {

        int score = 0;

        score += calculateWindScore(windSpeed);
        score += calculateWaveHeightScore(waveHeight);
        score += calculateWavePeriodScore(wavePeriod);
        score += calculateMoonScore(moonPhase);
        score += calculateTideScore(tideInfo, selectedDateTime);

        return score;
    }

    private static int calculateWindScore(double windSpeed) {
        if (windSpeed <= 5) {
            return 25;
        } else if (windSpeed <= 10) {
            return 20;
        } else if (windSpeed <= 15) {
            return 15;
        } else if (windSpeed <= 20) {
            return 10;
        } else if (windSpeed <= 25) {
            return 5;
        } else {
            return 0;
        }
    }

    private static int calculateWaveHeightScore(double waveHeight) {
        if (waveHeight <= 0.5) {
            return 15;
        } else if (waveHeight <= 1) {
            return 25;
        } else if (waveHeight <= 1.5) {
            return 20;
        } else if (waveHeight <= 2) {
            return 5;
        } else {
            return 0;
        }
    }

    private static int calculateWavePeriodScore(double wavePeriod) {
        if (wavePeriod >= 10) {
            return 20;
        } else if (wavePeriod >= 8) {
            return 10;
        } else {
            return 5;
        }
    }

    private static int calculateMoonScore(MoonPhase moonPhase) {
        switch (moonPhase) {
            case FULL_MOON:
            case NEW_MOON:
                return 15;

            case FIRST_QUARTER:
            case LAST_QUARTER:
                return 10;

            default:
                return 5;
        }
    }

    private static int calculateTideScore(TideInfo tideInfo,
                                          LocalDateTime selectedDateTime) {

        if (tideInfo == null || tideInfo.getNextHighTideTime() == null) {
            return 5;
        }

        OffsetDateTime nextHighTide = OffsetDateTime.parse(
                tideInfo.getNextHighTideTime()
        );

        OffsetDateTime selectedDateTimeUtc = selectedDateTime
                .atOffset(ZoneOffset.UTC);

        long hoursUntilHighTide = Duration.between(
                selectedDateTimeUtc,
                nextHighTide
        ).toHours();

        if (hoursUntilHighTide >= 0 && hoursUntilHighTide <= 2) {
            return 15;
        } else if (hoursUntilHighTide > 2 && hoursUntilHighTide <= 4) {
            return 10;
        } else {
            return 5;
        }
    }
}