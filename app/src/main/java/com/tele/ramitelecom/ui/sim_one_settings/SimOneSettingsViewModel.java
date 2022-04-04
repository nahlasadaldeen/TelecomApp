package com.tele.ramitelecom.ui.sim_one_settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SimOneSettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SimOneSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}