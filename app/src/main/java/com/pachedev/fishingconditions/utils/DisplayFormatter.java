package com.pachedev.fishingconditions.utils;

import com.pachedev.fishingconditions.model.domain.MoonPhase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting values for display in the UI.
 */
public class DisplayFormatter {

private static final DateTimeFormatter INPUT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
private static final DateTimeFormatter OUTPUT_TIME = DateTimeFormatter.ofPattern("HH:mm");

private DisplayFormatter () {
    // Prevent instantiation
}

    public static String formatTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return "N/A";
        }

        try {
            LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);
            return parsedDateTime.format(OUTPUT_TIME);
        } catch (Exception e) {
            OffsetDateTime parsedDateTime = OffsetDateTime.parse(dateTime);
            return parsedDateTime.format(OUTPUT_TIME);
        }
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
