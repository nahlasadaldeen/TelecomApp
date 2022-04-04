package com.tele.ramitelecom.ui.syriatel_code_deliver;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SyriatelCodeForDeliverViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SyriatelCodeForDeliverViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is syriatel code for deliver fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}