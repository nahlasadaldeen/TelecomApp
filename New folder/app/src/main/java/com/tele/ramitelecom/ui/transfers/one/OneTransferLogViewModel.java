package com.tele.ramitelecom.ui.transfers.one;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OneTransferLogViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OneTransferLogViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is One Transfer Log fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}