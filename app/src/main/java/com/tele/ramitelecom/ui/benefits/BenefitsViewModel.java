package com.tele.ramitelecom.ui.benefits;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BenefitsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BenefitsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is benefits fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}