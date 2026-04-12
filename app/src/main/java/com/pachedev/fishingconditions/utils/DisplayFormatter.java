package com.pachedev.fishingconditions.utils;

import com.pachedev.fishingconditions.model.domain.MoonPhase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting values for display in the UI.
 */
public class DisplayFormatter {

    /**
     * Pasar de esto:
     *
     * 2026-04-11T07:45
     * WANING_CRESCENT
     * 14.812345
     * 5.399999
     *
     * a algo más limpio para el usuario:
     *
     * 07:45
     * Waning Crescent
     * 14.8 °C
     * 5.4 m
     */
private static final DateTimeFormatter INPUT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
private static final DateTimeFormatter OUTPUT_TIME = DateTimeFormatter.ofPattern("HH:mm");

private DisplayFormatter () {
    // Prevent instantiation
}

public static String formatTime(String dateTime) {
    LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime, INPUT_DATE_TIME);
    return parsedDateTime.format(OUTPUT_TIME);
}

public static String formatMoonPhase(MoonPhase moonPhase) {
    String[] words = moonPhase.name().toLowerCase().split("_");
    StringBuilder formatted = new StringBuilder();

    for (String word : words ) {
        formatted.append(Character.toUpperCase(word.charAt(0)))
                .append(word.substring(1))
                .append(" ");
    }

    return formatted.toString().trim();
}

    public static String formatDecimal(Double value, String unit) {
        return String.format("%.1f %s", value, unit);
    }

}
