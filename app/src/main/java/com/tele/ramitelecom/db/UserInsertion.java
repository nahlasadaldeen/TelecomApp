package com.tele.ramitelecom.db;

import android.content.Context;

import com.tele.ramitelecom.ui.users.UserModel;

public class UserInsertion {

    Context context;

    public UserInsertion(Context context) {
        this.context = context;
    }

    public boolean insertUser(UserModel user) {

        // do insert user data in database for first time
        try {
            UserTableOperations userTableOperations = new UserTableOperations(context);
            if (userTableOperations.insertUserData(user)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}