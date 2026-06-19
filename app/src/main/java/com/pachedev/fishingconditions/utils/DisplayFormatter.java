package com.pachedev.fishingconditions.utils;

import com.pachedev.fishingconditions.R;
import com.pachedev.fishingconditions.model.domain.MoonPhase;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting values for display in the UI.
 */
public class DisplayFormatter {

    private static final DateTimeFormatter OUTPUT_TIME = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter OUTPUT_DATE_TIME =
            DateTimeFormatter.ofPattern("dd/MM HH:mm");

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

            return parsedDateTime
                    .atZoneSameInstant(ZoneId.of("Atlantic/Canary"))
                    .format(OUTPUT_TIME);
        }
    }

    /**
     * Formats a moon phase enum for display.
     *
     * @param moonPhase moon phase
     * @return formatted moon phase name
     */
    public static int getMoonPhaseStringRes(MoonPhase moonPhase) {
        switch (moonPhase) {
            case NEW_MOON:
                return R.string.new_moon;

            case WAXING_CRESCENT:
                return R.string.waxing_crescent;

            case FIRST_QUARTER:
                return R.string.first_quarter;

            case WAXING_GIBBOUS:
                return R.string.waxing_gibbous;

            case FULL_MOON:
                return R.string.full_moon;

            case WANING_GIBBOUS:
                return R.string.waning_gibbous;

            case LAST_QUARTER:
                return R.string.last_quarter;

            case WANING_CRESCENT:
                return R.string.waning_crescent;

            default:
                return R.string.new_moon;
        }
    }

    /**
     * Formats a decimal value with its unit.
     *
     * @param value numeric value
     * @param unit unit suffix
     * @return formatted value with unit
     */
    public static String formatDecimal(Double value, String unit) {
        if (value == null) {
            return "N/A";
        }

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
    public static int getFishingScoreDescriptionRes(int score) {
        if (score >= 80) {
            return R.string.excellent_conditions;
        } else if (score >= 60) {
            return R.string.good_conditions;
        } else if (score >= 40) {
            return R.string.fair_conditions;
        } else {
            return R.string.poor_conditions;
        }
    }

    public static String formatDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return "N/A";
        }

        try {
            LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);
            return parsedDateTime.format(OUTPUT_DATE_TIME);

        } catch (Exception e) {
            OffsetDateTime parsedDateTime = OffsetDateTime.parse(dateTime);

            return parsedDateTime
                    .atZoneSameInstant(ZoneId.of("Atlantic/Canary"))
                    .format(OUTPUT_DATE_TIME);
        }
    }
    public static String formatMoonPhaseIcon(MoonPhase moonPhase) {
        switch (moonPhase) {
            case NEW_MOON:
                return "🌑";
            case WAXING_CRESCENT:
                return "🌒";
            case FIRST_QUARTER:
                return "🌓";
            case WAXING_GIBBOUS:
                return "🌔";
            case FULL_MOON:
                return "🌕";
            case WANING_GIBBOUS:
                return "🌖";
            case LAST_QUARTER:
                return "🌗";
            case WANING_CRESCENT:
                return "🌘";
            default:
                return "🌙";
        }
    }
}
