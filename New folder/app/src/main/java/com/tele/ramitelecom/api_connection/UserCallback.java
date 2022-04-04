package com.tele.ramitelecom.api_connection;

import com.tele.ramitelecom.ui.users.UserModel;

public interface UserCallback {
    void onSuccess(UserModel user);

    void onFail(int errorCode);
}