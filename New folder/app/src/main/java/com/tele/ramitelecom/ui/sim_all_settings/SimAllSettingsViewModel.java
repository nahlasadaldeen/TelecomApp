package com.tele.ramitelecom.ui.sim_all_settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SimAllSettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SimAllSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Sim All fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}