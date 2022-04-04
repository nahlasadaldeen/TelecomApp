package com.tele.ramitelecom.ui.users;

import java.io.Serializable;

public class UserModel implements Serializable {
    public int id;
    public String name;
    public String mobileNum;
    public String email;
    public String enableCode;
    public int attemptsNum;
    public int money_amount;
    public int subscribeType;
    public int is_center; // 0 the user normal user, // 1 it is a center
    public int center_id; // 1 the account is for a deliver, else the account is a user follow to center with this id

    public CenterModel center_details;

    public boolean is_sync; // true(0) synced, false(1) (need sync)


    //constructors

    public UserModel(String name, String mobileNum) {
        this.name = name;
        this.mobileNum = mobileNum;
    }

    public UserModel(String name, String mobileNum, boolean is_sync) {
        this.name = name;
        this.mobileNum = mobileNum;
        this.is_sync = is_sync;
    }

    public UserModel(String name, String mobileNum, int money_amount) {
        this.name = name;
        this.mobileNum = mobileNum;
        this.money_amount = money_amount;
    }

    public UserModel(String name, String mobileNum, int money_amount, boolean is_sync) {
        this.name = name;
        this.mobileNum = mobileNum;
        this.money_amount = money_amount;
        this.is_sync = is_sync;
    }

    public UserModel(String name, int money_amount, String enableCode) {
        this.name = name;
        this.money_amount = money_amount;
        this.enableCode = enableCode;
    }

    public UserModel(String name, int money_amount, String enableCode, boolean is_sync) {
        this.name = name;
        this.money_amount = money_amount;
        this.enableCode = enableCode;
        this.is_sync = is_sync;
    }

    public UserModel(String name, String mobileNum, String enableCode) {
        this.name = name;
        this.mobileNum = mobileNum;
        this.enableCode = enableCode;
    }

    public UserModel(String name, String mobileNum, String enableCode, boolean is_sync) {
        this.name = name;
        this.mobileNum = mobileNum;
        this.enableCode = enableCode;
        this.is_sync = is_sync;
    }

    public UserModel(String name, String mobileNum, int money_amount, String enableCode) {
        this.name = name;
        this.mobileNum = mobileNum;
        this.money_amount = money_amount;
        this.enableCode = enableCode;
    }

    public UserModel(String name, String mobileNum, int money_amount, String enableCode, boolean is_sync) {
        this.name = name;
        this.mobileNum = mobileNum;
        this.money_amount = money_amount;
        this.enableCode = enableCode;
        this.is_sync = is_sync;

    }

    public UserModel(int id, String name, String mobileNum, String email, String enableCode, int attemptsNum, int money_amount, int subscribeType) {
        this.id = id;
        this.name = name;
        this.mobileNum = mobileNum;
        this.email = email;
        this.enableCode = enableCode;
        this.attemptsNum = attemptsNum;
        this.money_amount = money_amount;
        this.subscribeType = subscribeType;
    }

    public UserModel(int id, String name, String mobileNum, String email, String enableCode, int attemptsNum, int money_amount, int subscribeType, int is_center, int center_id, CenterModel center_details) {
        this.id = id;
        this.name = name;
        this.mobileNum = mobileNum;
        this.email = email;
        this.enableCode = enableCode;
        this.attemptsNum = attemptsNum;
        this.money_amount = money_amount;
        this.subscribeType = subscribeType;
        this.is_center = is_center;
        this.center_id = center_id;
        this.center_details = center_details;

    }

    public UserModel(int id, String name, String mobileNum, String email, String enableCode, int attemptsNum, int money_amount, int subscribeType, int is_center, int center_id) {
        this.id = id;
        this.name = name;
        this.mobileNum = mobileNum;
        this.email = email;
        this.enableCode = enableCode;
        this.attemptsNum = attemptsNum;
        this.money_amount = money_amount;
        this.subscribeType = subscribeType;
        this.is_center = is_center;
        this.center_id = center_id;
    }

    public UserModel(int id, String name, String mobileNum, String email, String enableCode, int attemptsNum, int money_amount, int subscribeType, boolean is_sync) {
        this.id = id;
        this.name = name;
        this.mobileNum = mobileNum;
        this.email = email;
        this.enableCode = enableCode;
        this.attemptsNum = attemptsNum;
        this.money_amount = money_amount;
        this.subscribeType = subscribeType;
        this.is_sync = is_sync;

    }

    public UserModel(String name, String mobileNum, String email, String enableCode, int attemptsNum, int money_amount, int subscribeType) {
        this.name = name;
        this.mobileNum = mobileNum;
        this.email = email;
        this.enableCode = enableCode;
        this.attemptsNum = attemptsNum;
        this.money_amount = money_amount;
        this.subscribeType = subscribeType;
    }

    public UserModel(String name, String mobileNum, String email, String enableCode, int attemptsNum, int money_amount, int subscribeType, boolean is_sync) {
        this.name = name;
        this.mobileNum = mobileNum;
        this.email = email;
        this.enableCode = enableCode;
        this.attemptsNum = attemptsNum;
        this.money_amount = money_amount;
        this.subscribeType = subscribeType;
        this.is_sync = is_sync;
    }

    public UserModel() {
    }


    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMoney_amount() {
        return money_amount;
    }

    public void setMoney_amount(int money_amount) {
        this.money_amount = money_amount;
    }

    public String getEnableCode() {
        return enableCode;
    }

    public void setEnableCode(String enableCode) {
        this.enableCode = enableCode;
    }

    public int getAttemptsNum() {
        return attemptsNum;
    }

    public void setAttemptsNum(int attemptsNum) {
        this.attemptsNum = attemptsNum;
    }

    public int getSubscribeType() {
        return subscribeType;
    }

    public void setSubscribeType(int subscribeType) {
        this.subscribeType = subscribeType;
    }

    public boolean getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(boolean is_sync) {
        this.is_sync = is_sync;
    }
}