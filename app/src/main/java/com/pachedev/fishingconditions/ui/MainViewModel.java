package com.pachedev.fishingconditions.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pachedev.fishingconditions.model.domain.FishingConditionsData;

import java.time.LocalDateTime;

/**
 * Holds the state of the main screen that must survive configuration changes
 * (portrait/landscape rotation): selected spot, selected date and time, and the
 * last loaded fishing conditions, so the recreated activity can rebuild the UI
 * from this state without re-fetching data.
 */
public class MainViewModel extends ViewModel {

    private LocalDateTime selectedDateTime = LocalDateTime.now();
    private String selectedSpotName;
    private boolean dateTouched;
    private boolean timeTouched;

    private final MutableLiveData<FishingConditionsData> data = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LocalDateTime getSelectedDateTime() {
        return selectedDateTime;
    }

    public void setSelectedDateTime(LocalDateTime selectedDateTime) {
        this.selectedDateTime = selectedDateTime;
    }

    public String getSelectedSpotName() {
        return selectedSpotName;
    }

    public void setSelectedSpotName(String selectedSpotName) {
        this.selectedSpotName = selectedSpotName;
    }

    public boolean isDateTouched() {
        return dateTouched;
    }

    public void setDateTouched(boolean dateTouched) {
        this.dateTouched = dateTouched;
    }

    public boolean isTimeTouched() {
        return timeTouched;
    }

    public void setTimeTouched(boolean timeTouched) {
        this.timeTouched = timeTouched;
    }

    public LiveData<FishingConditionsData> getData() {
        return data;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    /**
     * Marks a new request as in progress, clearing previous results so the UI
     * shows the loading placeholders.
     */
    public void startLoading() {
        loading.setValue(true);
        data.setValue(null);
        error.setValue(null);
    }

    /**
     * Publishes a successful response and stops the loading state.
     *
     * @param conditionsData retrieved fishing conditions
     */
    public void publishSuccess(FishingConditionsData conditionsData) {
        error.postValue(null);
        data.postValue(conditionsData);
        loading.postValue(false);
    }

    /**
     * Publishes an error message and stops the loading state.
     *
     * @param errorMessage error to display
     */
    public void publishError(String errorMessage) {
        data.postValue(null);
        error.postValue(errorMessage);
        loading.postValue(false);
    }
}
