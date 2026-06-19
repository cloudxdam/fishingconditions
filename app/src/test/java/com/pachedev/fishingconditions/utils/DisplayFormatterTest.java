package com.pachedev.fishingconditions.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DisplayFormatterTest {

    @Test
    public void formatTime_returnsNA_whenValueIsNull() {
        assertEquals("N/A", DisplayFormatter.formatTime(null));
    }

    @Test
    public void formatTime_returnsNA_whenValueIsEmpty() {
        assertEquals("N/A", DisplayFormatter.formatTime(""));
    }

    @Test
    public void formatDateTime_returnsNA_whenValueIsNull() {
        assertEquals("N/A", DisplayFormatter.formatDateTime(null));
    }
    @Test
    public void formatDateTime_returnsNA_whenValueIsEmpty() {
        assertEquals("N/A", DisplayFormatter.formatDateTime(""));
    }

    @Test
    public void formatDecimal_formatsValueWithUnit() {
        assertEquals("1,5 m", DisplayFormatter.formatDecimal(1.5, "m"));
    }

    @Test
    public void formatDecimal_returnsNA_whenValueIsNull() {
        assertEquals("N/A", DisplayFormatter.formatDecimal(null, "m"));
    }

    @Test
    public void formatWindDirection_returnsNorthForZeroDegrees() {
        assertEquals("↑ N", DisplayFormatter.formatWindDirection(0));
    }

    @Test
    public void formatWindDirection_returnsEastForNinetyDegrees() {
        assertEquals("→ E", DisplayFormatter.formatWindDirection(90));
    }
}
