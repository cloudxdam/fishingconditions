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
import com.pachedev.fishingconditions.data.local.FishingSpotProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Main screen of the application.
 * Allows the user to select a fishing spot, date and time,
 * then displays weather, sea, tide and fishing score information.
 */
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
    private TextView tvSelectedDate;
    private TextView tvSelectedTime;
    private TextView tvFishingScore;
    private TextView tvFishingScoreDescription;
    private FishingConditionsRepository fishingConditionsRepository;
    private Spinner spinnerSpot;
    private LocalDateTime selectedDateTime = LocalDateTime.now();


    /**
     * Initializes the user interface and event listeners.
     *
     * @param savedInstanceState previously saved activity state
     */
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
        Button btnLoadConditions = findViewById(R.id.btnLoadConditions);
        Button btnSelectDate = findViewById(R.id.btnSelectDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        Button btnSelectTime = findViewById(R.id.btnSelectTime);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        tvFishingScore = findViewById(R.id.tvFishingScore);
        tvFishingScoreDescription = findViewById(R.id.tvFishingScoreDescription);

        btnSelectTime.setOnClickListener(v -> showTimePicker());

        setupFishingSpots();

        btnLoadConditions.setOnClickListener(v -> loadFishingConditions());
        btnSelectDate.setOnClickListener(v -> showDatePicker());

        fishingConditionsRepository = new FishingConditionsRepository(this);
    }

    /**
     * Displays a time picker dialog and updates the selected time.
     */
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
                                    getString(R.string.selected_time_02d_00),
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
     * Requests fishing conditions for the selected spot, date and time.
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

    /**
     * Updates the UI to indicate that data is being loaded.
     */
    private void setLoadingState() {
        tvTemperature.setText(R.string.loading);
        tvWind.setText("");
        tvSunrise.setText("");
        tvSunset.setText("");
        tvWaveHeight.setText("");
        tvWavePeriod.setText("");
        tvMoonPhase.setText("");
        tvHighTide.setText("");
        tvLowTide.setText("");
        tvFishingScoreDescription.setText("");
    }

    /**
     * Displays the retrieved fishing conditions in the user interface.
     *
     * @param data fishing conditions data
     */
    private void showFishingConditions(FishingConditionsData data) {
        String highTideTime = DisplayFormatter.formatDateTime(
                data.getTideInfo().getNextHighTideTime()
        );

        String highTideHeight = DisplayFormatter.formatDecimal(
                data.getTideInfo().getNextHighTideHeight(), "m"
        );

        String lowTideTime = DisplayFormatter.formatDateTime(
                data.getTideInfo().getNextLowTideTime()
        );

        String lowTideHeight = DisplayFormatter.formatDecimal(
                data.getTideInfo().getNextLowTideHeight(), "m"
        );

        tvTemperature.setText(String.format(getString(R.string.temperature_s), DisplayFormatter.formatDecimal(data.getTemperature(), "°C")));

        tvWind.setText(
                String.format(
                        Locale.getDefault(),
                        getString(R.string.wind_s_s),
                        DisplayFormatter.formatWindDirection(data.getWindDirection()),
                        DisplayFormatter.formatDecimal(data.getWindSpeed(), "km/h")
                )
        );

        tvSunrise.setText(String.format(getString(R.string.sunrise_s), DisplayFormatter.formatTime(data.getSunrise())));

        tvSunset.setText(String.format(getString(R.string.sunset_s), DisplayFormatter.formatTime(data.getSunset())));

        tvWaveHeight.setText(String.format(getString(R.string.wave_height_s), DisplayFormatter.formatDecimal(data.getWaveHeight(), "m")));

        tvWavePeriod.setText(String.format(getString(R.string.wave_period_s), DisplayFormatter.formatDecimal(data.getWavePeriod(), "s")));

        tvMoonPhase.setText(getString(R.string.moon_phase_s,getString(DisplayFormatter.getMoonPhaseStringRes(data.getMoonPhase()))));

        tvHighTide.setText(String.format(getString(R.string.next_high_tide_s_s), highTideTime, highTideHeight));

        tvLowTide.setText(String.format(getString(R.string.next_low_tide_s_s), lowTideTime, lowTideHeight));

        tvFishingScore.setText(String.format(Locale.getDefault(),getString(R.string.fishing_score_d_100),data.getFishingScore()));

        int score = data.getFishingScore();

        tvFishingScoreDescription.setText(DisplayFormatter.getFishingScoreDescriptionRes(score));

        if (score >= 80) {
            tvFishingScoreDescription.setTextColor(getColor(android.R.color.holo_green_dark));
        } else if (score >= 60) {
            tvFishingScoreDescription.setTextColor(getColor(android.R.color.holo_blue_dark));
        } else if (score >= 40) {
            tvFishingScoreDescription.setTextColor(getColor(android.R.color.holo_orange_dark));
        } else {
            tvFishingScoreDescription.setTextColor(getColor(android.R.color.holo_red_dark));
        }
    }

    /**
     * Displays an error message in the UI.
     *
     * @param errorMessage error message to display
     */
    private void showError(String errorMessage) {
        tvTemperature.setText(String.format(getString(R.string.error_s), errorMessage));
    }

    /**
     * Initializes the available fishing spots.
     */
    private void setupFishingSpots() {
        List<FishingSpot> fishingSpots = FishingSpotProvider.getDefaultSpots();

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

    /**
     * Displays a date picker dialog and updates the selected date.
     */
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
                            String.format(getString(R.string.selected_date_s), selectedDateTime.toLocalDate())
                    );

                },
                selectedDateTime.getYear(),
                selectedDateTime.getMonthValue() - 1,
                selectedDateTime.getDayOfMonth()
        );

        datePickerDialog.show();
    }
}