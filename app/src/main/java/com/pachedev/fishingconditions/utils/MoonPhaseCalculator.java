package com.pachedev.fishingconditions.utils;

import com.pachedev.fishingconditions.model.domain.MoonPhase;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Utility class to calculate the moon phase for a given date.
 *
 * Uses an approximate lunar cycle based on a known new moon reference date.
 */
public class MoonPhaseCalculator {
    private static final LocalDate KNOWN_NEW_MOON = LocalDate.of(2000, 1, 06);
    private static final double LUNAR_CYCLE = 29.53058867;

    private MoonPhaseCalculator () {
        // Prevent instantiation
    }

    public static MoonPhase calculateMoonPhase (LocalDate date) {
        long daysSinceNewMoon = ChronoUnit.DAYS.between(KNOWN_NEW_MOON, date);

        // Position of the moon within the current lunar cycle
        double moonAge = daysSinceNewMoon % LUNAR_CYCLE;

        // Adjust negative values to keep moonAge within cycle range
        if (moonAge < 0) {
            moonAge += LUNAR_CYCLE;
        }

        if (moonAge < 1.84566) {
            return MoonPhase.NEW_MOON;
        } else if (moonAge < 5.53699) {
            return MoonPhase.WAXING_CRESCENT;
        } else if (moonAge < 9.22831) {
            return MoonPhase.FIRST_QUARTER;
        } else if (moonAge < 12.91963) {
            return MoonPhase.WAXING_GIBBOUS;
        } else if (moonAge < 16.61096) {
            return MoonPhase.FULL_MOON;
        } else if (moonAge < 20.30228) {
            return MoonPhase.WANING_GIBBOUS;
        } else if (moonAge < 23.99361) {
            return MoonPhase.LAST_QUARTER;
        } else if (moonAge < 27.68493) {
            return MoonPhase.WANING_CRESCENT;
        } else {
            return MoonPhase.NEW_MOON;
        }
    }
}
