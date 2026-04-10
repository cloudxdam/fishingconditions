package com.pachedev.fishingconditions.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.pachedev.fishingconditions.R;
import com.pachedev.fishingconditions.data.repository.FishingConditionsRepository;
import com.pachedev.fishingconditions.model.domain.FishingConditionsData;


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
                        String.format("Temperature: %s°C\nWind: %s\nSunrise: %s\nSunset: %s\nWave height: %s m\nWave period: %s s", fishingConditionsData.getTemperature(), fishingConditionsData.getWindSpeed(), fishingConditionsData.getSunrise(), fishingConditionsData.getSunset(), fishingConditionsData.getWaveHeight(), fishingConditionsData.getWavePeriod())
                );
            }

            @Override
            public void onError(String errorMessage) {
                tvResult.setText(String.format("Error: %s", errorMessage));
            }
        });
    }
}