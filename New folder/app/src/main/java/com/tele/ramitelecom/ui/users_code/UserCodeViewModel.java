package com.tele.ramitelecom.ui.users_code;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserCodeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserCodeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}