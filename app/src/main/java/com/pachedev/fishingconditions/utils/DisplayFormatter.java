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

    private static final DateTimeFormatter OUTPUT_TIME = DateTimeFormatter.ofPattern("HH:mm");

private DisplayFormatter () {
    // Prevent instantiation
}

    /**
     * Formats a date-time string as a time.
     *
     * @param dateTime date-time string
     * @return formatted time or N/A if unavailable
     */
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

    /**
     * Formats a moon phase enum for display.
     *
     * @param moonPhase moon phase
     * @return formatted moon phase name
     */
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

    /**
     * Formats a decimal value with its unit.
     *
     * @param value numeric value
     * @param unit unit suffix
     * @return formatted value with unit
     */
    public static String formatDecimal(Double value, String unit) {
        return String.format("%.1f %s", value, unit);
    }

    /**
     * Formats wind direction in degrees as a cardinal direction.
     *
     * @param degrees wind direction in degrees
     * @return formatted wind direction
     */
    public static String formatWindDirection(double degrees) {

        if (degrees >= 337.5 || degrees < 22.5) {
            return "↑ N";
        } else if (degrees < 67.5) {
            return "↗ NE";
        } else if (degrees < 112.5) {
            return "→ E";
        } else if (degrees < 157.5) {
            return "↘ SE";
        } else if (degrees < 202.5) {
            return "↓ S";
        } else if (degrees < 247.5) {
            return "↙ SW";
        } else if (degrees < 292.5) {
            return "← W";
        } else {
            return "↖ NW";
        }
    }

    /**
     * Formats the fishing score as a readable condition label.
     *
     * @param score fishing score
     * @return condition label
     */
    public static String formatFishingScoreDescription(int score) {
        if (score >= 80) {
            return "Excellent Conditions";
        } else if (score >= 60) {
            return "Good Conditions";
        } else if (score >= 40) {
            return "Fair Conditions";
        } else {
            return "Poor Conditions";
        }
    }
}
