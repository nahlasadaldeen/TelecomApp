package com.tele.ramitelecom.ui.users;

import java.io.Serializable;

public interface UserDelegate extends Serializable {
    void getUser(UserModel user);
}
