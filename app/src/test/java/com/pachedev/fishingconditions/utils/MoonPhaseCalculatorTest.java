package com.pachedev.fishingconditions.utils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.pachedev.fishingconditions.model.domain.MoonPhase;
import org.junit.Test;
import java.time.LocalDate;
public class MoonPhaseCalculatorTest {
    @Test
    public void calculateMoonPhase_returnsNewMoon_forKnownNewMoonDate() {
        MoonPhase result = MoonPhaseCalculator.calculateMoonPhase(
                LocalDate.of(2000, 1, 6)
        );
        assertEquals(MoonPhase.NEW_MOON, result);
    }
    @Test
    public void calculateMoonPhase_returnsFirstQuarter_forKnownFirstQuarterDate() {
        MoonPhase result = MoonPhaseCalculator.calculateMoonPhase(
                LocalDate.of(2000, 1, 14)
        );
        assertEquals(MoonPhase.FIRST_QUARTER, result);
    }
    @Test
    public void calculateMoonPhase_returnsFullMoon_forKnownFullMoonDate() {
        MoonPhase result = MoonPhaseCalculator.calculateMoonPhase(
                LocalDate.of(2000, 1, 21)
        );
        assertEquals(MoonPhase.FULL_MOON, result);
    }
    @Test
    public void calculateMoonPhase_returnsLastQuarter_forKnownLastQuarterDate() {
        MoonPhase result = MoonPhaseCalculator.calculateMoonPhase(
                LocalDate.of(2000, 1, 28)
        );
        assertEquals(MoonPhase.LAST_QUARTER, result);
    }
    @Test
    public void calculateMoonPhase_returnsStableResult_forSameDate() {
        LocalDate date = LocalDate.of(2026, 6, 20);
        MoonPhase firstResult = MoonPhaseCalculator.calculateMoonPhase(date);
        MoonPhase secondResult = MoonPhaseCalculator.calculateMoonPhase(date);
        assertNotNull(firstResult);
        assertEquals(firstResult, secondResult);
    }
}