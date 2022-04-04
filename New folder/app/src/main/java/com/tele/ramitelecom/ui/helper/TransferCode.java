package com.tele.ramitelecom.ui.helper;

import android.content.Context;
import android.content.SharedPreferences;

import static com.tele.ramitelecom.ui.helper.Constants.ALL_MTN;
import static com.tele.ramitelecom.ui.helper.Constants.ALL_SYRIATEL;
import static com.tele.ramitelecom.ui.helper.Constants.MyPREFERENCES;
import static com.tele.ramitelecom.ui.helper.Constants.ONE_BALANCE_MTN;
import static com.tele.ramitelecom.ui.helper.Constants.ONE_BALANCE_SYRIATEL;
import static com.tele.ramitelecom.ui.helper.Constants.ONE_BILL_MTN;
import static com.tele.ramitelecom.ui.helper.Constants.ONE_BILL_SYRIATEL;

public class TransferCode {
    SharedPreferences sharedpreferences;

    Context ctx;

    public TransferCode(Context ctx) {
        this.ctx = ctx;
        sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    // جملة Syriatel
    public String getAllSyriatelTransferCode() {
        sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String allSyriatelTransferCode = sharedpreferences.getString(ALL_SYRIATEL, "");
        return allSyriatelTransferCode;
    }

    // جملة MTN
    public String getAllMtnTransferCode() {
        sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String allMtnTransferCode = sharedpreferences.getString(ALL_MTN, "");
        return allMtnTransferCode;
    }

    // وحدات Syriatel
    public String getOneBalanceSyriatelTransferCode() {
        sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String oneBalanceSyriatelTransferCode = sharedpreferences.getString(ONE_BALANCE_SYRIATEL, "");
        return oneBalanceSyriatelTransferCode;
    }

    // وحدات MTN
    public String getOneBalanceMtnTransferCode() {
        sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String oneBalanceMtnTransferCode = sharedpreferences.getString(ONE_BALANCE_MTN, "");
        return oneBalanceMtnTransferCode;
    }

    // وحدات Syriatel
    public String getOneBillSyriatelTransferCode() {
        sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String oneBillSyriatelTransferCode = sharedpreferences.getString(ONE_BILL_SYRIATEL, "");
        return oneBillSyriatelTransferCode;
    }

    // وحدات MTN
    public String getOneBillMtnTransferCode() {
        sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String oneBillMtnTransferCode = sharedpreferences.getString(ONE_BILL_MTN, "");
        return oneBillMtnTransferCode;
    }
}
