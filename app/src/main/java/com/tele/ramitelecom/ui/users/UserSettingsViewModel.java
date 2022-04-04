package com.tele.ramitelecom.ui.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserSettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Users fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}