package com.tele.ramitelecom.ui.users_loan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UsersLoanViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UsersLoanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Users Loan fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}