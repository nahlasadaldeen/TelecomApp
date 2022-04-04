package com.tele.ramitelecom.ui.center_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CenterDetailsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CenterDetailsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is center details fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}