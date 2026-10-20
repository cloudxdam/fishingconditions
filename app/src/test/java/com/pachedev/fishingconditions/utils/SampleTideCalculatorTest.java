package com.pachedev.fishingconditions.utils;

import static org.junit.Assert.assertEquals;

import com.pachedev.fishingconditions.model.domain.TideInfo;

import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class SampleTideCalculatorTest {

    private static final ZoneId CANARY_ZONE = ZoneId.of("Atlantic/Canary");

    private static OffsetDateTime expected(LocalDateTime dateTime) {
        return dateTime.atZone(CANARY_ZONE).toOffsetDateTime();
    }

    @Test
    public void calculateNext_returnsRealReferenceEvents_whenSelectedBeforeThem() {
        TideInfo tideInfo = SampleTideCalculator.calculateNext(
                LocalDateTime.of(2026, 10, 19, 12, 0));

        assertEquals(expected(LocalDateTime.of(2026, 10, 19, 21, 44)),
                OffsetDateTime.parse(tideInfo.getNextHighTideTime()));
        assertEquals(expected(LocalDateTime.of(2026, 10, 19, 15, 21)),
                OffsetDateTime.parse(tideInfo.getNextLowTideTime()));
        assertEquals(1.525, tideInfo.getNextHighTideHeight(), 0.001);
        assertEquals(0.964, tideInfo.getNextLowTideHeight(), 0.001);
    }

    @Test
    public void calculateNext_returnsReferenceItself_whenSelectedAtReference() {
        TideInfo tideInfo = SampleTideCalculator.calculateNext(
                LocalDateTime.of(2026, 10, 19, 21, 44));

        assertEquals(expected(LocalDateTime.of(2026, 10, 19, 21, 44)),
                OffsetDateTime.parse(tideInfo.getNextHighTideTime()));
    }

    @Test
    public void calculateNext_returnsPreviousCycle_whenSelectedBeforeReference() {
        TideInfo tideInfo = SampleTideCalculator.calculateNext(
                LocalDateTime.of(2026, 10, 19, 8, 34));

        assertEquals(expected(LocalDateTime.of(2026, 10, 19, 9, 19)),
                OffsetDateTime.parse(tideInfo.getNextHighTideTime()));
        assertEquals(1.632, tideInfo.getNextHighTideHeight(), 0.001);
    }

    @Test
    public void calculateNext_rollsForwardOneCycle_afterMidnight() {
        TideInfo tideInfo = SampleTideCalculator.calculateNext(
                LocalDateTime.of(2026, 10, 20, 0, 0));

        assertEquals(expected(LocalDateTime.of(2026, 10, 20, 10, 9)),
                OffsetDateTime.parse(tideInfo.getNextHighTideTime()));
        assertEquals(expected(LocalDateTime.of(2026, 10, 20, 3, 46)),
                OffsetDateTime.parse(tideInfo.getNextLowTideTime()));
        assertEquals(1.632, tideInfo.getNextHighTideHeight(), 0.001);
        assertEquals(1.076, tideInfo.getNextLowTideHeight(), 0.001);
    }

    @Test
    public void calculateNext_successiveEvents_areTwelveHours25Apart() {
        TideInfo first = SampleTideCalculator.calculateNext(
                LocalDateTime.of(2026, 10, 20, 10, 0));
        TideInfo second = SampleTideCalculator.calculateNext(
                LocalDateTime.of(2026, 10, 20, 22, 25));

        Duration between = Duration.between(
                OffsetDateTime.parse(first.getNextHighTideTime()),
                OffsetDateTime.parse(second.getNextHighTideTime())
        );
        assertEquals(Duration.ofMinutes(12 * 60 + 25), between);
    }
}
