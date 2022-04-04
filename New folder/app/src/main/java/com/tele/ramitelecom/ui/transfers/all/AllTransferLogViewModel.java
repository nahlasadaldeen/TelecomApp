package com.tele.ramitelecom.ui.transfers.all;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AllTransferLogViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AllTransferLogViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is All Transfer Log fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}