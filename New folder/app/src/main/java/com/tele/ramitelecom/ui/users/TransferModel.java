package com.tele.ramitelecom.ui.users;

import java.io.Serializable;

public class TransferModel implements Serializable {
    public int id;

    public String mobileNum;
    public int balanceOrBill;  // 1 Balance, 2 Bill
    public int money_amount;
    public int company; // 1 SYR, 2 MTN

    public String transfer_date;

    public int one_or_all; // 1 one, 2 all

    public String transfer_code;

    public int client_id;

    public int is_sync; // 0 synced, 1 (need sync)


    //constructors
    public TransferModel(String mobileNum, int balanceOrBill, int money_amount, int company, String transfer_date, int one_or_all, String transfer_code, int is_sync) {
        this.mobileNum = mobileNum;
        this.balanceOrBill = balanceOrBill;
        this.money_amount = money_amount;
        this.company = company;
        this.transfer_date = transfer_date;
        this.one_or_all = one_or_all;
        this.transfer_code = transfer_code;
        this.is_sync = is_sync;

    }

    public TransferModel(String mobileNum, int balanceOrBill, int money_amount, int company, String transfer_date, int one_or_all, String transfer_code) {
        this.mobileNum = mobileNum;
        this.balanceOrBill = balanceOrBill;
        this.money_amount = money_amount;
        this.company = company;
        this.transfer_date = transfer_date;
        this.one_or_all = one_or_all;
        this.transfer_code = transfer_code;
    }

    public TransferModel(int id, String mobileNum, int balanceOrBill, int money_amount, int company, String transfer_date, int one_or_all, String transfer_code, int is_sync) {
        this.id = id;
        this.mobileNum = mobileNum;
        this.balanceOrBill = balanceOrBill;
        this.money_amount = money_amount;
        this.company = company;
        this.transfer_date = transfer_date;
        this.one_or_all = one_or_all;
        this.transfer_code = transfer_code;
        this.is_sync = is_sync;
    }

    public TransferModel(int id, String mobileNum, int balanceOrBill, int money_amount, int company, String transfer_date, int one_or_all, String transfer_code) {
        this.id = id;
        this.mobileNum = mobileNum;
        this.balanceOrBill = balanceOrBill;
        this.money_amount = money_amount;
        this.company = company;
        this.transfer_date = transfer_date;
        this.one_or_all = one_or_all;
        this.transfer_code = transfer_code;
    }

    public TransferModel() {
    }


    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public int getMoney_amount() {
        return money_amount;
    }

    public void setMoney_amount(int money_amount) {
        this.money_amount = money_amount;
    }

    public int getBalanceOrBill() {
        return balanceOrBill;
    }

    public void setBalanceOrBill(int balanceOrBill) {
        this.balanceOrBill = balanceOrBill;
    }

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }

    public String getTransfer_date() {
        return transfer_date;
    }

    public void setTransfer_date(String transfer_date) {
        this.transfer_date = transfer_date;
    }

    public int getOne_or_all() {
        return one_or_all;
    }

    public void setOne_or_all(int one_or_all) {
        this.one_or_all = one_or_all;
    }

    public String getTransfer_code() {
        return transfer_code;
    }

    public void setTransfer_code(String transfer_code) {
        this.transfer_code = transfer_code;
    }

    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }
}