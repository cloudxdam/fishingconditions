package com.pachedev.fishingconditions.utils;

import com.pachedev.fishingconditions.model.domain.TideInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Builds illustrative tide data for demo builds, which never call the TideCheck API.
 *
 * <p>Tides are anchored to real TideCheck extremes for Santa Cruz de Tenerife on
 * 2026-10-19 and extrapolated with the mean lunar semidiurnal period (M2, 12h25m),
 * alternating the two observed heights per cycle. The result is a labeled sample,
 * not a real tide prediction.
 */
public final class SampleTideCalculator {

    private static final ZoneId CANARY_ZONE = ZoneId.of("Atlantic/Canary");
    private static final Duration SEMIDIURNAL_PERIOD = Duration.ofMinutes(12 * 60 + 25);

    // Real TideCheck extremes — Santa Cruz de Tenerife, 2026-10-19 (local time).
    private static final ZonedDateTime REF_HIGH =
            LocalDateTime.of(2026, 10, 19, 21, 44).atZone(CANARY_ZONE);
    private static final ZonedDateTime REF_LOW =
            LocalDateTime.of(2026, 10, 19, 15, 21).atZone(CANARY_ZONE);

    private static final double[] HIGH_HEIGHTS = {1.525, 1.632};
    private static final double[] LOW_HEIGHTS = {0.964, 1.076};

    private SampleTideCalculator() {
        // Prevent instantiation
    }

    /**
     * Calculates the next sample high tide and low tide for the selected date-time.
     *
     * @param selectedDateTime selected date and time (local, Atlantic/Canary)
     * @return tide information with the next high tide and next low tide
     */
    public static TideInfo calculateNext(LocalDateTime selectedDateTime) {
        long highCycles = calculateCyclesAfter(REF_HIGH, selectedDateTime);
        long lowCycles = calculateCyclesAfter(REF_LOW, selectedDateTime);

        return new TideInfo(
                formatEventTime(REF_HIGH, highCycles),
                selectHeightForCycle(HIGH_HEIGHTS, highCycles),
                formatEventTime(REF_LOW, lowCycles),
                selectHeightForCycle(LOW_HEIGHTS, lowCycles)
        );
    }

    /**
     * Returns the number of 12h25m cycles from the reference event to the
     * selected date-time, rounded up so the result is never before the selection.
     */
    private static long calculateCyclesAfter(ZonedDateTime reference, LocalDateTime selectedDateTime) {
        long difference = Duration.between(
                reference.toInstant(),
                selectedDateTime.atZone(CANARY_ZONE).toInstant()
        ).toNanos();
        long period = SEMIDIURNAL_PERIOD.toNanos();

        long cycles = Math.floorDiv(difference, period);
        if (cycles * period < difference) {
            cycles++;
        }
        return cycles;
    }

    private static String formatEventTime(ZonedDateTime reference, long cycles) {
        return OffsetDateTime.ofInstant(
                reference.toInstant().plusNanos(cycles * SEMIDIURNAL_PERIOD.toNanos()),
                CANARY_ZONE
        ).toString();
    }

    private static double selectHeightForCycle(double[] heights, long cycles) {
        return heights[(int) Math.floorMod(cycles, heights.length)];
    }
}
