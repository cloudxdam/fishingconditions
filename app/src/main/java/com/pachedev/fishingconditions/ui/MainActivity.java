package com.pachedev.fishingconditions.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.pachedev.fishingconditions.R;
import com.pachedev.fishingconditions.data.repository.FishingConditionsRepository;
import com.pachedev.fishingconditions.model.domain.FishingConditionsData;
import com.pachedev.fishingconditions.model.domain.FishingSpot;
import com.pachedev.fishingconditions.utils.DisplayFormatter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


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

    private List<FishingSpot> fishingSpots;

    private Spinner spinnerSpot;
    private Button btnLoadConditions;

    private Button btnSelectDate;

    private TextView tvSelectedDate;

    private LocalDateTime selectedDateTime = LocalDateTime.now();
    private Button btnSelectTime;
    private TextView tvSelectedTime;
    private TextView tvFishingScore;

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
        spinnerSpot = findViewById(R.id.spinnerSpot);
        btnLoadConditions = findViewById(R.id.btnLoadConditions);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        tvFishingScore = findViewById(R.id.tvFishingScore);

        btnSelectTime.setOnClickListener(v -> showTimePicker());

        setupFishingSpots();

        btnLoadConditions.setOnClickListener(v -> loadFishingConditions());
        btnSelectDate.setOnClickListener(v -> showDatePicker());

        fishingConditionsRepository = new FishingConditionsRepository();

        loadFishingConditions();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedDateTime = selectedDateTime
                            .withHour(hourOfDay)
                            .withMinute(0)
                            .withSecond(0)
                            .withNano(0);

                    tvSelectedTime.setText(
                            String.format(
                                    Locale.getDefault(),
                                    "Selected time: %02d:00",
                                    hourOfDay)
                    );
                },
                selectedDateTime.getHour(),
                0,
                true
        );

        timePickerDialog.show();
    }

    /**
     * Loads fishing conditions for the selected spot and date.
     */
    private void loadFishingConditions() {
        setLoadingState();

        FishingSpot selectedSpot = (FishingSpot) spinnerSpot.getSelectedItem();

        fishingConditionsRepository.getFishingConditions(
                selectedSpot,
                selectedDateTime,
                new FishingConditionsRepository.FishingConditionsCallback() {
                    @Override
                    public void onSuccess(FishingConditionsData fishingConditionsData) {
                        showFishingConditions(fishingConditionsData);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        showError(errorMessage);
                    }
                }
        );
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

    /**
     * Displays fishing conditions in the UI.
     *
     * @param data fishing conditions data
     */
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

        tvTemperature.setText(String.format("Temperature: %s", DisplayFormatter.formatDecimal(data.getTemperature(), "°C")));

        tvWind.setText(
                String.format(
                        Locale.getDefault(),
                        "Wind: %s - %s",
                        DisplayFormatter.formatWindDirection(data.getWindDirection()),
                        DisplayFormatter.formatDecimal(data.getWindSpeed(), "km/h")
                )
        );

        tvSunrise.setText(String.format("Sunrise: %s", DisplayFormatter.formatTime(data.getSunrise())));

        tvSunset.setText(String.format("Sunset: %s", DisplayFormatter.formatTime(data.getSunset())));

        tvWaveHeight.setText(String.format("Wave height: %s", DisplayFormatter.formatDecimal(data.getWaveHeight(), "m")));

        tvWavePeriod.setText(String.format("Wave period: %s", DisplayFormatter.formatDecimal(data.getWavePeriod(), "s")));

        tvMoonPhase.setText(String.format("Moon phase: %s", DisplayFormatter.formatMoonPhase(data.getMoonPhase())));

        tvHighTide.setText(String.format("Next high tide: %s (%s)", highTideTime, highTideHeight));

        tvLowTide.setText(String.format("Next low tide: %s (%s)", lowTideTime, lowTideHeight));

        tvFishingScore.setText(String.format(Locale.getDefault(),"Fishing Score: %d / 100",data.getFishingScore()));

    }

    private void showError(String errorMessage) {
        tvTemperature.setText(String.format("Error: %s", errorMessage));
    }

    /**
     * Initializes the available fishing spots.
     */
    private void setupFishingSpots() {
        fishingSpots = Arrays.asList(
                new FishingSpot("Palm Mar", 28.0244, -16.6417),
                new FishingSpot("Alcalá", 28.2086, -16.8404),
                new FishingSpot("Abades", 28.1403, -16.4325),
                new FishingSpot("El Médano", 28.0453, -16.5361),
                new FishingSpot("Las Galletas", 28.0064, -16.6538),
                new FishingSpot("La Caleta", 28.0931, -16.7552),
                new FishingSpot("Los Cristianos", 28.0506, -16.7200)
        );

        ArrayAdapter<FishingSpot> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                fishingSpots
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerSpot.setAdapter(adapter);
    }

    private void showDatePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    selectedDateTime = LocalDateTime.of(
                            year,
                            month + 1,
                            dayOfMonth,
                            selectedDateTime.getHour(),
                            selectedDateTime.getMinute()
                    );

                    tvSelectedDate.setText(
                            String.format("Selected date: %s", selectedDateTime.toLocalDate())
                    );

                },
                selectedDateTime.getYear(),
                selectedDateTime.getMonthValue() - 1,
                selectedDateTime.getDayOfMonth()
        );

        datePickerDialog.show();
    }
}