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

    private TextView tvResult;
    private FishingConditionsRepository fishingConditionsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        fishingConditionsRepository = new FishingConditionsRepository();

        loadFishingConditions();
    }

    private void loadFishingConditions() {
        tvResult.setText("Loading fishing conditions...");

        fishingConditionsRepository.getFishingConditions(new FishingConditionsRepository.FishingCoinditionsCallback() {
            @Override
            public void onSuccess(FishingConditionsData fishingConditionsData) {

                tvResult.setText(
                        String.format("Temperature: %s°C\nWind: %s\nSunrise: %s\nSunset: %s\nWave height: %s m\nWave period: %s s\nMoon phase: %s", DisplayFormatter.formatDecimal(fishingConditionsData.getTemperature(), "°C"), DisplayFormatter.formatDecimal(fishingConditionsData.getWindSpeed(), "km/h"), DisplayFormatter.formatTime(fishingConditionsData.getSunrise()), DisplayFormatter.formatTime(fishingConditionsData.getSunset()), DisplayFormatter.formatDecimal(fishingConditionsData.getWaveHeight(), "m"), DisplayFormatter.formatDecimal(fishingConditionsData.getWavePeriod(), "s"), DisplayFormatter.formatMoonPhase(fishingConditionsData.getMoonPhase())));
            }

            @Override
            public void onError(String errorMessage) {
                tvResult.setText(String.format("Error: %s", errorMessage));
            }
        });
    }
}