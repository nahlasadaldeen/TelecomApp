package com.tele.ramitelecom.ui.select_for_client;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SelectForClientViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SelectForClientViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is select for client fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}