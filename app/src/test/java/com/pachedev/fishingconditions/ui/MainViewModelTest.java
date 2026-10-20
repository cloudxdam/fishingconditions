package com.pachedev.fishingconditions.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.pachedev.fishingconditions.model.domain.FishingConditionsData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;

public class MainViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MainViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new MainViewModel();
    }

    @Test
    public void selectionStateIsPreserved() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 10, 16, 7, 0);
        viewModel.setSelectedDateTime(dateTime);
        viewModel.setSelectedSpotName("Roca Negra");
        viewModel.setDateTouched(true);
        viewModel.setTimeTouched(true);

        assertEquals(dateTime, viewModel.getSelectedDateTime());
        assertEquals("Roca Negra", viewModel.getSelectedSpotName());
        assertTrue(viewModel.isDateTouched());
        assertTrue(viewModel.isTimeTouched());
    }

    @Test
    public void startLoadingClearsPreviousResults() {
        viewModel.publishSuccess(newConditions());
        viewModel.publishError("stale error");

        viewModel.startLoading();

        assertTrue(viewModel.getLoading().getValue());
        assertNull(viewModel.getData().getValue());
        assertNull(viewModel.getError().getValue());
    }

    @Test
    public void publishSuccessStopsLoadingAndClearsError() {
        viewModel.publishError("Tide station response error: 500");
        viewModel.startLoading();

        viewModel.publishSuccess(newConditions());

        assertFalse(viewModel.getLoading().getValue());
        assertNull(viewModel.getError().getValue());
        assertNotNull(viewModel.getData().getValue());
    }

    @Test
    public void publishErrorStopsLoadingAndClearsData() {
        viewModel.publishSuccess(newConditions());
        viewModel.startLoading();

        viewModel.publishError("Weather request failed");

        assertFalse(viewModel.getLoading().getValue());
        assertNull(viewModel.getData().getValue());
        assertEquals("Weather request failed", viewModel.getError().getValue());
    }

    private FishingConditionsData newConditions() {
        return new FishingConditionsData(
                20.0, 10.0, "07:30", "19:00", 1.2, 8.0, null, null, 75, 180.0);
    }
}
