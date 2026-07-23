package com.pachedev.fishingconditions.data.local;

import com.pachedev.fishingconditions.model.domain.FishingSpot;

import java.util.Arrays;
import java.util.List;

/**
 * Provides predefined fishing spots for the app.
 */
public class FishingSpotProvider {

    private FishingSpotProvider() {
        // Prevent instantiation
    }

    public static List<FishingSpot> getDefaultSpots() {
        return Arrays.asList(
                new FishingSpot("Palm Mar", 28.0244, -16.6417),
                new FishingSpot("Alcalá", 28.2086, -16.8404),
                new FishingSpot("Abades", 28.1403, -16.4325),
                new FishingSpot("El Médano", 28.0453, -16.5361),
                new FishingSpot("Las Galletas", 28.0064, -16.6538),
                new FishingSpot("La Caleta", 28.0931, -16.7552),
                new FishingSpot("Los Cristianos", 28.0506, -16.7200)
        );
    }
}
