package com.tele.ramitelecom.ui.users;

import java.io.Serializable;

public class CenterModel implements Serializable {
    public int id;
    public String center_name;
    public String center_city;
    public String center_phone;
    public String center_address;
    public int users_count;
    public int active_users_count;
    public int user_id; // secondary key for user id in  users table (if the user account is for deliver center)

    public CenterModel(int id, String center_name, String center_city, String center_address, int users_count, int active_users_count, int user_id) {
        this.id = id;
        this.center_name = center_name;
        this.center_city = center_city;
        this.center_address = center_address;
        this.users_count = users_count;
        this.active_users_count = active_users_count;

        this.user_id = user_id;
    }

    public CenterModel(int id, String center_name, String center_city, String center_address, int users_count, int active_users_count) {
        this.id = id;
        this.center_name = center_name;
        this.center_city = center_city;
        this.center_address = center_address;
        this.users_count = users_count;
        this.active_users_count = active_users_count;
    }
}
