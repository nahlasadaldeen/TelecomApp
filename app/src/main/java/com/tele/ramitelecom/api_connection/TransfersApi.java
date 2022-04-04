package com.tele.ramitelecom.api_connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tele.ramitelecom.ui.users.TransferModel;
import com.tele.ramitelecom.ui.users.UnitPriceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransfersApi {
    Context ctx;

//    String serverUrl = "http://192.168.43.22/telecom";

    String serverUrl = "https://sharbektelecom.com/Deliver";
    String allTransfersForUser = serverUrl + "/transfer_for_user.php?user_id="; // user id;
    String allTransfersForDate = serverUrl + "/transfers_for_date.php?transfer_date="; // transfer_date;

    String insertTransferUrl = serverUrl + "/insert_transfer.php";


    String allUnitPrices = "http://sharbektelecom.com/services/chargimg_units/charging_units.php";

    RequestQueue requestQueue;

    public TransfersApi(Context context) {
        ctx = context;
    }

    public void transfer_insert(final TransferModel transferModel, final VolleyCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                insertTransferUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Toast.makeText(ctx, "" + response, Toast.LENGTH_SHORT).show();
                if (response.contains("1")) {
                    Log.e("result", "ok");
                    callback.onSuccess("ok");
                } else {
                    Log.e("result", "no");
                    callback.onSuccess("no");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ctx, "" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();

                params.put("mobile_num", transferModel.mobileNum);
                params.put("balance_or_bill", transferModel.balanceOrBill + "");
                params.put("money_amount", transferModel.money_amount + "");
                params.put("company", transferModel.company + "");
                params.put("transfer_date", transferModel.transfer_date);
                params.put("one_or_all", transferModel.one_or_all + "");
                params.put("transfer_code", transferModel.transfer_code);
                params.put("user_id", transferModel.client_id + "");

                Log.e("hhhh", params.size() + "");
                return params;
            }

        };
        strReq.setShouldCache(false);

        requestQueue.add(strReq);

    }

    public ArrayList<UnitPriceModel> getUnitsPrice(final UnitsCallback callback) {
        final ArrayList<UnitPriceModel> allUnitsPrice = new ArrayList<>(0);
        requestQueue = Volley.newRequestQueue(ctx);

        JsonArrayRequest arrayreq = new JsonArrayRequest(allUnitPrices,
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int s = response.length();
                            Log.e("here unitsssss", response.toString() + "");
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int unit_class = jsonObject.getInt("class");
                                int unit_price = jsonObject.getInt("price");
                                int unit_profit = jsonObject.getInt("profit");

                                UnitPriceModel j = new UnitPriceModel(unit_class, unit_price, unit_profit);

                                allUnitsPrice.add(j);
                            }

                            callback.onSuccess(allUnitsPrice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error" + error.getMessage());
                    }
                }
        );
        arrayreq.setShouldCache(false);

        requestQueue.add(arrayreq);
        return allUnitsPrice;
    }

    public ArrayList<TransferModel> getAllTransfersForUser(int user_id) {
        final ArrayList<TransferModel> transfersForUser = new ArrayList<>(0);
        requestQueue = Volley.newRequestQueue(ctx);

        JsonArrayRequest arrayreq = new JsonArrayRequest(allTransfersForUser + user_id,
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int s = response.length();
                            Log.e("here", s + "");
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String mobile_num = jsonObject.getString("mobile_num");
                                int balance_or_bill = jsonObject.getInt("balance_or_bill");
                                int money_amount = jsonObject.getInt("money_amount");
                                int company = jsonObject.getInt("company");
                                String transfer_date = jsonObject.getString("transfer_date");
                                int one_or_all = jsonObject.getInt("one_or_all");
                                String transfer_code = jsonObject.getString("transfer_code");

                                TransferModel j = new TransferModel(mobile_num, balance_or_bill, money_amount, company, transfer_date, one_or_all, transfer_code);

                                transfersForUser.add(j);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error" + error.getMessage());
                    }
                }
        );
        arrayreq.setShouldCache(false);

        requestQueue.add(arrayreq);
        return transfersForUser;
    }

    public ArrayList<TransferModel> getAllTransfersForDate(String date) {
        final ArrayList<TransferModel> transfersForUser = new ArrayList<>(0);
        requestQueue = Volley.newRequestQueue(ctx);

        JsonArrayRequest arrayreq = new JsonArrayRequest(allTransfersForDate + date,
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int s = response.length();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String mobile_num = jsonObject.getString("mobile_num");
                                int balance_or_bill = jsonObject.getInt("balance_or_bill");
                                int money_amount = jsonObject.getInt("money_amount");
                                int company = jsonObject.getInt("company");
                                String transfer_date = jsonObject.getString("transfer_date");
                                int one_or_all = jsonObject.getInt("one_or_all");
                                String transfer_code = jsonObject.getString("transfer_code");

                                TransferModel j = new TransferModel(mobile_num, balance_or_bill, money_amount, company, transfer_date, one_or_all, transfer_code);

                                transfersForUser.add(j);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        arrayreq.setShouldCache(false);

        requestQueue.add(arrayreq);
        return transfersForUser;
    }


}
