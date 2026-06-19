package com.pachedev.fishingconditions.utils;
import static org.junit.Assert.assertEquals;
import com.pachedev.fishingconditions.model.domain.MoonPhase;
import com.pachedev.fishingconditions.model.domain.TideInfo;
import org.junit.Test;
import java.time.LocalDateTime;
import java.time.ZoneId;
public class FishingScoreCalculatorTest {
    private static final ZoneId CANARY_ZONE = ZoneId.of("Atlantic/Canary");
    @Test
    public void calculateScore_returnsMaximumScore_forFavorableConditions() {
        LocalDateTime selectedDateTime = LocalDateTime.of(2026, 6, 20, 8, 0);
        String highTideTime = selectedDateTime
                .plusHours(1)
                .atZone(CANARY_ZONE)
                .toOffsetDateTime()
                .toString();
        TideInfo tideInfo = new TideInfo(highTideTime, 2.1, null, null);
        int score = FishingScoreCalculator.calculateScore(
                4.0,
                1.0,
                10.0,
                MoonPhase.FULL_MOON,
                tideInfo,
                selectedDateTime
        );
        assertEquals(100, score);
    }
    @Test
    public void calculateScore_returnsLowScore_forPoorConditions() {
        LocalDateTime selectedDateTime = LocalDateTime.of(2026, 6, 20, 8, 0);
        TideInfo tideInfo = new TideInfo(null, null, null, null);
        int score = FishingScoreCalculator.calculateScore(
                28.0,
                2.5,
                5.0,
                MoonPhase.WANING_CRESCENT,
                tideInfo,
                selectedDateTime
        );
        assertEquals(15, score);
    }
    @Test
    public void calculateScore_handlesNullTideInfo() {
        LocalDateTime selectedDateTime = LocalDateTime.of(2026, 6, 20, 8, 0);
        int score = FishingScoreCalculator.calculateScore(
                10.0,
                1.5,
                8.0,
                MoonPhase.FIRST_QUARTER,
                null,
                selectedDateTime
        );
        assertEquals(65, score);
    }
    @Test
    public void calculateScore_handlesTideInfoWithoutHighTideTime() {
        LocalDateTime selectedDateTime = LocalDateTime.of(2026, 6, 20, 8, 0);
        TideInfo tideInfo = new TideInfo(null, 1.8, null, null);
        int score = FishingScoreCalculator.calculateScore(
                10.0,
                1.5,
                8.0,
                MoonPhase.FIRST_QUARTER,
                tideInfo,
                selectedDateTime
        );
        assertEquals(65, score);
    }
    @Test
    public void calculateScore_givesBetterScore_whenHighTideIsNear() {
        LocalDateTime selectedDateTime = LocalDateTime.of(2026, 6, 20, 8, 0);
        String highTideTime = selectedDateTime
                .plusHours(1)
                .atZone(CANARY_ZONE)
                .toOffsetDateTime()
                .toString();
        TideInfo tideInfo = new TideInfo(highTideTime, 2.0, null, null);
        int score = FishingScoreCalculator.calculateScore(
                10.0,
                1.5,
                8.0,
                MoonPhase.FIRST_QUARTER,
                tideInfo,
                selectedDateTime
        );
        assertEquals(75, score);
    }
    @Test
    public void calculateScore_givesLowerScore_whenHighTideIsFar() {
        LocalDateTime selectedDateTime = LocalDateTime.of(2026, 6, 20, 8, 0);
        String highTideTime = selectedDateTime
                .plusHours(6)
                .atZone(CANARY_ZONE)
                .toOffsetDateTime()
                .toString();
        TideInfo tideInfo = new TideInfo(highTideTime, 2.0, null, null);
        int score = FishingScoreCalculator.calculateScore(
                10.0,
                1.5,
                8.0,
                MoonPhase.FIRST_QUARTER,
                tideInfo,
                selectedDateTime
        );
        assertEquals(65, score);
    }
}