package com.pachedev.fishingconditions.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.pachedev.fishingconditions.R;
import com.pachedev.fishingconditions.data.repository.FishingConditionsRepository;
import com.pachedev.fishingconditions.model.domain.FishingConditionsData;
import com.pachedev.fishingconditions.utils.DisplayFormatter;


public class MainActivity extends AppCompatActivity {

    private TextView tvTemperature;
    private TextView tvWind;
    private TextView tvSunrise;
    private TextView tvSunset;
    private TextView tvWaveHeight;
    private TextView tvWavePeriod;
    private TextView tvMoonPhase;
    private TextView tvHighTide;
    private TextView tvLowTide;
    private FishingConditionsRepository fishingConditionsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvTemperature = findViewById(R.id.tvTemperature);
        tvWind = findViewById(R.id.tvWind);
        tvSunrise = findViewById(R.id.tvSunrise);
        tvSunset = findViewById(R.id.tvSunset);
        tvWaveHeight = findViewById(R.id.tvWaveHeight);
        tvWavePeriod = findViewById(R.id.tvWavePeriod);
        tvMoonPhase = findViewById(R.id.tvMoonPhase);
        tvHighTide = findViewById(R.id.tvHighTide);
        tvLowTide = findViewById(R.id.tvLowTide);

        fishingConditionsRepository = new FishingConditionsRepository();

        loadFishingConditions();
    }

    private void loadFishingConditions() {
        setLoadingState();

        fishingConditionsRepository.getFishingConditions(new FishingConditionsRepository.FishingConditionsCallback() {
            @Override
            public void onSuccess(FishingConditionsData fishingConditionsData) {
                showFishingConditions(fishingConditionsData);
            }

            @Override
            public void onError(String errorMessage) {
                showError(errorMessage);
            }
        });
    }

    private void setLoadingState() {
        tvTemperature.setText("Loading...");
        tvWind.setText("");
        tvSunrise.setText("");
        tvSunset.setText("");
        tvWaveHeight.setText("");
        tvWavePeriod.setText("");
        tvMoonPhase.setText("");
        tvHighTide.setText("");
        tvLowTide.setText("");
    }

    private void showFishingConditions(FishingConditionsData data) {
        String highTideTime = DisplayFormatter.formatTime(
                data.getTideInfo().getNextHighTideTime()
        );

        String highTideHeight = DisplayFormatter.formatDecimal(
                data.getTideInfo().getNextHighTideHeight(), "m"
        );

        String lowTideTime = DisplayFormatter.formatTime(
                data.getTideInfo().getNextLowTideTime()
        );

        String lowTideHeight = DisplayFormatter.formatDecimal(
                data.getTideInfo().getNextLowTideHeight(), "m"
        );

        tvTemperature.setText("Temperature: " +
                DisplayFormatter.formatDecimal(data.getTemperature(), "°C"));

        tvWind.setText("Wind: " +
                DisplayFormatter.formatDecimal(data.getWindSpeed(), "km/h"));

        tvSunrise.setText("Sunrise: " +
                DisplayFormatter.formatTime(data.getSunrise()));

        tvSunset.setText("Sunset: " +
                DisplayFormatter.formatTime(data.getSunset()));

        tvWaveHeight.setText("Wave height: " +
                DisplayFormatter.formatDecimal(data.getWaveHeight(), "m"));

        tvWavePeriod.setText("Wave period: " +
                DisplayFormatter.formatDecimal(data.getWavePeriod(), "s"));

        tvMoonPhase.setText("Moon phase: " +
                DisplayFormatter.formatMoonPhase(data.getMoonPhase()));

        tvHighTide.setText("Next high tide: " +
                highTideTime + " (" + highTideHeight + ")");

        tvLowTide.setText("Next low tide: " +
                lowTideTime + " (" + lowTideHeight + ")");
    }

    private void showError(String errorMessage) {
        tvTemperature.setText("Error: " + errorMessage);
    }
}