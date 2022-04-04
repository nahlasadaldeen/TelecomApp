package com.tele.ramitelecom.api_connection;

import com.tele.ramitelecom.ui.users.UnitPriceModel;

import java.util.ArrayList;

public interface UnitsCallback {
    void onSuccess(ArrayList<UnitPriceModel> unitPriceModels);

    void onFail(int errorCode);
}