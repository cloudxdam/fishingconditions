package com.pachedev.fishingconditions.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

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
    private MainViewModel viewModel;


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

        View scrollRoot = findViewById(R.id.scrollRoot);
        if (scrollRoot == null) {
            scrollRoot = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        final View scrollRootView = scrollRoot;
        ViewCompat.setOnApplyWindowInsetsListener(scrollRootView, (v, windowInsets) -> {
            boolean isLandscape = getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE;
            if (isLandscape) {
                Insets bars = windowInsets.getInsets(
                        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            } else {
                Insets nav = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars());
                v.setPadding(0, 0, 0, nav.bottom);
            }
            return WindowInsetsCompat.CONSUMED;
        });

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

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        restoreSelectionFromViewModel();
        observeConditions();
        setupSpotSelectionTracking();
    }

    /**
     * Displays a time picker dialog and updates the selected time.
     */
    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    viewModel.setSelectedDateTime(
                            viewModel.getSelectedDateTime()
                                    .withHour(hourOfDay)
                                    .withMinute(0)
                                    .withSecond(0)
                                    .withNano(0)
                    );
                    viewModel.setTimeTouched(true);

                    tvSelectedTime.setText(
                            String.format(
                                    Locale.getDefault(),
                                    getString(R.string.selected_time_02d_00),
                                    hourOfDay)
                    );
                },
                viewModel.getSelectedDateTime().getHour(),
                0,
                true
        );

        timePickerDialog.show();
    }

    /**
     * Restores spot selection, date and time labels after a configuration change.
     */
    private void restoreSelectionFromViewModel() {
        String savedSpotName = viewModel.getSelectedSpotName();
        if (savedSpotName != null) {
            for (int i = 0; i < spinnerSpot.getCount(); i++) {
                if (savedSpotName.equals(String.valueOf(spinnerSpot.getItemAtPosition(i)))) {
                    spinnerSpot.setSelection(i);
                    break;
                }
            }
        }

        if (viewModel.isDateTouched()) {
            tvSelectedDate.setText(String.format(
                    getString(R.string.selected_date_s),
                    viewModel.getSelectedDateTime().toLocalDate()
            ));
        }

        if (viewModel.isTimeTouched()) {
            tvSelectedTime.setText(String.format(
                    Locale.getDefault(),
                    getString(R.string.selected_time_02d_00),
                    viewModel.getSelectedDateTime().getHour()
            ));
        }
    }

    /**
     * Rebinds loaded conditions, errors and loading state whenever they change,
     * including the values emitted again after the activity is recreated.
     */
    private void observeConditions() {
        viewModel.getData().observe(this, fishingConditionsData -> {
            if (fishingConditionsData != null) {
                showFishingConditions(fishingConditionsData);
            }
        });

        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                showError(errorMessage);
            }
        });

        viewModel.getLoading().observe(this, isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                setLoadingState();
            }
        });
    }

    /**
     * Keeps the selected spot in the view model so it can be restored after rotation.
     */
    private void setupSpotSelectionTracking() {
        spinnerSpot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setSelectedSpotName(((FishingSpot) parent.getItemAtPosition(position)).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Requests fishing conditions for the selected spot, date and time.
     */
    private void loadFishingConditions() {
        FishingSpot selectedSpot = (FishingSpot) spinnerSpot.getSelectedItem();

        viewModel.setSelectedSpotName(selectedSpot.getName());
        viewModel.startLoading();

        fishingConditionsRepository.getFishingConditions(
                selectedSpot,
                viewModel.getSelectedDateTime(),
                new FishingConditionsRepository.FishingConditionsCallback() {
                    @Override
                    public void onSuccess(FishingConditionsData fishingConditionsData) {
                        viewModel.publishSuccess(fishingConditionsData);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        viewModel.publishError(errorMessage);
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
        tvWaveHeight.setText(R.string.loading);
        tvWavePeriod.setText("");
        tvMoonPhase.setText(R.string.loading);
        tvHighTide.setText(R.string.loading);
        tvLowTide.setText("");
        tvFishingScoreDescription.setText(R.string.calculating);
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

        tvMoonPhase.setText(getString(R.string.moon_phase_s,DisplayFormatter.formatMoonPhaseIcon(data.getMoonPhase()) + " " + getString(DisplayFormatter.getMoonPhaseStringRes(data.getMoonPhase()))
                )
        );

        tvHighTide.setText(String.format(getString(R.string.next_high_tide_s_s), highTideTime, highTideHeight));

        tvLowTide.setText(String.format(getString(R.string.next_low_tide_s_s), lowTideTime, lowTideHeight));

        tvFishingScore.setText(String.format(Locale.getDefault(),getString(R.string.fishing_score_d_100),data.getFishingScore()));

        int score = data.getFishingScore();

        tvFishingScoreDescription.setText(DisplayFormatter.getFishingScoreDescriptionRes(score));

        if (score >= 80) {
            tvFishingScoreDescription.setTextColor(getColor(android.R.color.holo_green_dark));
            tvFishingScore.setTextColor(getColor(android.R.color.holo_green_dark));
        } else if (score >= 60) {
            tvFishingScoreDescription.setTextColor(getColor(android.R.color.holo_blue_dark));
            tvFishingScore.setTextColor(getColor(android.R.color.holo_blue_dark));
        } else if (score >= 40) {
            tvFishingScoreDescription.setTextColor(getColor(android.R.color.holo_orange_dark));
            tvFishingScore.setTextColor(getColor(android.R.color.holo_orange_dark));
        } else {
            tvFishingScoreDescription.setTextColor(getColor(android.R.color.holo_red_dark));
            tvFishingScore.setTextColor(getColor(android.R.color.holo_red_dark));
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
                R.layout.spinner_item,
                fishingSpots
        );

        adapter.setDropDownViewResource(
                R.layout.spinner_dropdown_item
        );

        spinnerSpot.setAdapter(adapter);
    }

    /**
     * Displays a date picker dialog and updates the selected date.
     */
    private void showDatePicker() {

        LocalDateTime currentDateTime = viewModel.getSelectedDateTime();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    LocalDateTime updatedDateTime = LocalDateTime.of(
                            year,
                            month + 1,
                            dayOfMonth,
                            currentDateTime.getHour(),
                            currentDateTime.getMinute()
                    );
                    viewModel.setSelectedDateTime(updatedDateTime);
                    viewModel.setDateTouched(true);

                    tvSelectedDate.setText(
                            String.format(getString(R.string.selected_date_s), updatedDateTime.toLocalDate())
                    );

                },
                currentDateTime.getYear(),
                currentDateTime.getMonthValue() - 1,
                currentDateTime.getDayOfMonth()
        );

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime maxDate = today.plusDays(9);

        datePickerDialog.getDatePicker().setMinDate(
                today.atZone(java.time.ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
        );

        datePickerDialog.getDatePicker().setMaxDate(
                maxDate.atZone(java.time.ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
        );

        datePickerDialog.show();
    }
}