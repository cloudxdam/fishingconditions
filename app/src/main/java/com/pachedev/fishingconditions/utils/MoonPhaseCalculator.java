package com.pachedev.fishingconditions.utils;

import com.pachedev.fishingconditions.model.domain.MoonPhase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Utility class to calculate the moon phase for a given date.
 */
public class MoonPhaseCalculator {

    private static final LocalDateTime KNOWN_NEW_MOON =
            LocalDateTime.of(2000, 1, 6, 18, 14);

    private static final double LUNAR_CYCLE = 29.53058867;

    private MoonPhaseCalculator() {
        // Prevent instantiation
    }

    public static MoonPhase calculateMoonPhase(LocalDate date) {
        LocalDateTime dateTime = date.atTime(12, 0);

        long minutesSinceNewMoon = ChronoUnit.MINUTES.between(
                KNOWN_NEW_MOON,
                dateTime
        );

        double daysSinceNewMoon = minutesSinceNewMoon / 1440.0;
        double moonAge = daysSinceNewMoon % LUNAR_CYCLE;

        if (moonAge < 1.0) {
            return MoonPhase.NEW_MOON;
        } else if (moonAge < 6.5) {
            return MoonPhase.WAXING_CRESCENT;
        } else if (moonAge < 8.5) {
            return MoonPhase.FIRST_QUARTER;
        } else if (moonAge < 14.2) {
            return MoonPhase.WAXING_GIBBOUS;
        } else if (moonAge < 15.2) {
            return MoonPhase.FULL_MOON;
        } else if (moonAge < 21.0) {
            return MoonPhase.WANING_GIBBOUS;
        } else if (moonAge < 23.0) {
            return MoonPhase.LAST_QUARTER;
        } else if (moonAge < 28.5) {
            return MoonPhase.WANING_CRESCENT;
        } else {
            return MoonPhase.NEW_MOON;
        }
    }
}