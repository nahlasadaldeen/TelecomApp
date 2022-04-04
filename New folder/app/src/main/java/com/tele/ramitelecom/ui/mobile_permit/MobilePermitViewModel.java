package com.tele.ramitelecom.ui.mobile_permit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MobilePermitViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MobilePermitViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mobile permit fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}