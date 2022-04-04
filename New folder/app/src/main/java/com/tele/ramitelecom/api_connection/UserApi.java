package com.tele.ramitelecom.api_connection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tele.ramitelecom.ui.users.CenterModel;
import com.tele.ramitelecom.ui.users.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserApi {
    Context ctx;
    //    String serverUrl = "http://192.168.43.22/telecom";
    String serverUrl = "https://sharbektelecom.com/Deliver";
    String allUsers = serverUrl + "/api/";
    String loginURL = serverUrl + "/user_login.php";
    String logoutURL = serverUrl + "/user_logout.php";
    String insertUserURL = serverUrl + "/insert_user.php";
    String permUserEnableCodeURL = serverUrl + "/perm_user_enable_code.php";
    String tempUserEnableCodeURL = serverUrl + "/temp_user_enable_code.php";
    String updateUserAttemptsNumURL = serverUrl + "/update_user_attempts_num.php";

    public UserApi(Context context) {
        ctx = context;
    }

    RequestQueue requestQueue;

    public ArrayList<UserModel> getAllUsers(Context context) {
        final ArrayList<UserModel> users = new ArrayList<>(0);
        // Creates the Volley request queue
        requestQueue = Volley.newRequestQueue(context);

        // Creating the JsonArrayRequest class called arrayreq, passing the required parameters
        //JsonURL is the URL to be fetched from
        JsonArrayRequest arrayreq = new JsonArrayRequest(allUsers,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Retrieves first JSON object in outer array
                            int s = response.length();
                            for (int i = 0; i < response.length(); i++) {
                                //gets each JSON object within the JSON array
                                JSONObject jsonObject = response.getJSONObject(i);

                                // Retrieves the string labeled "colorName" and "hexValue",
                                // and converts them into javascript objects
                                int id = jsonObject.getInt("id");

                                UserModel j = new UserModel();

                                users.add(j);
                            }
                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        requestQueue.add(arrayreq);
        return users;
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {

        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }

    public void user_login(final String username, final String pass, final UserCallback callback) {

        final String deviceId = getDeviceId(ctx);

        Log.e("deviceId........", deviceId);
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                loginURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("ggg", response);
//                Toast.makeText(ctx, "" + response, Toast.LENGTH_SHORT).show();
                if (response.contains("User is Logged in")) {
                    callback.onFail(1);
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject user = new JSONObject(object.get("user").toString());
                    int id = Integer.parseInt(user.getString("id"));
                    String name = user.getString("name");
                    String password = user.getString("password");
                    String mobile_num = user.getString("mobile_num");
                    int is_center = Integer.parseInt(user.getString("is_center"));
                    int center_id = Integer.parseInt(user.getString("center_id"));

                    CenterModel centerModel = null;
                    if (is_center != 0) {
                        // the account is for a deliver
                        JSONObject center = new JSONObject(object.get("center").toString());
                        int centerId = Integer.parseInt(center.getString("id"));
                        String centerName = center.getString("center_name");
                        String centerCity = center.getString("center_city");
                        String centerAddress = center.getString("center_address");
                        int usersCount = Integer.parseInt(center.getString("users_count"));
                        int active_users_count = Integer.parseInt(center.getString("active_users_count"));
                        centerModel = new CenterModel(centerId, centerName, centerCity, centerAddress, usersCount, active_users_count);
                    }

                    if (center_id != 0) {
                        // the account is for a deliver
                        JSONObject center = new JSONObject(object.get("center").toString());
                        int centerId = Integer.parseInt(center.getString("id"));
                        String centerName = center.getString("center_name");
                        String centerCity = center.getString("center_city");
                        String centerAddress = center.getString("center_address");
                        int usersCount = Integer.parseInt(center.getString("users_count"));
                        int active_users_count = Integer.parseInt(center.getString("active_users_count"));
                        centerModel = new CenterModel(centerId, centerName, centerCity, centerAddress, usersCount, active_users_count);
                    }

                    String email = null;
                    if (!user.isNull("email"))
                        email = user.getString("email");

                    int subscribe_type = 0;
                    if (!user.isNull("subscribe_type"))
                        subscribe_type = Integer.parseInt(user.getString("subscribe_type"));

                    String enable_code = null;
                    if (!user.isNull("enable_code"))
                        enable_code = user.getString("enable_code");

                    int attempt_num = 0;
                    if (!user.isNull("attempt_num"))
                        attempt_num = Integer.parseInt(user.getString("attempt_num"));

                    int money_amount = 0;
                    if (!user.isNull("money_amount"))
                        money_amount = Integer.parseInt(user.getString("money_amount"));

                    UserModel userModel = new UserModel(id, name, mobile_num, email, enable_code, attempt_num, money_amount, subscribe_type, is_center, center_id, centerModel);

                    Log.e("hhh", "" + user.toString());

                    callback.onSuccess(userModel);
//                    Log.e("hhh", "" + user.get("mobile_num"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFail(2);
                    Log.e("eeee", "1" + e.getMessage());
                }
                /*if (response.contains("1")) {
                    Log.e("result", "ok");
                    callback.onSuccess("ok");
                } else {
                    Log.e("result", "no");
                    callback.onSuccess("no");
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("hhh", "2" + error.toString());
                callback.onFail(3);
//                Toast.makeText(ctx, "" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", pass);
                params.put("user_device_id", deviceId);
                return params;
            }
        };
        strReq.setShouldCache(false);

        requestQueue.add(strReq);
    }

    public void user_logout(final int id, final VolleyCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                logoutURL, new Response.Listener<String>() {

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
                Log.e("hhh", "2" + error.toString());
//                Toast.makeText(ctx, "" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id + "");
                return params;
            }
        };
        strReq.setShouldCache(false);

        requestQueue.add(strReq);
    }

    public void user_insert(final UserModel userModel, final VolleyCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                insertUserURL, new Response.Listener<String>() {

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
                Map<String, String> params = new HashMap<String, String>();


                params.put("username", userModel.name);
                params.put("mobilenum", userModel.mobileNum);

                if (userModel.mobileNum != null)
                    params.put("password", userModel.mobileNum);
                if (userModel.email != null)
                    params.put("email", userModel.email);
                if (userModel.subscribeType > 0)
                    params.put("subscribe_type", userModel.subscribeType + "");
                if (userModel.enableCode != null)
                    params.put("enable_code", userModel.enableCode);
                if (userModel.attemptsNum > 0)
                    params.put("attempt_num", userModel.attemptsNum + "");
                if (userModel.money_amount > 0)
                    params.put("money_amount", userModel.money_amount + "");

                return params;
            }

        };
        strReq.setShouldCache(false);
        requestQueue.add(strReq);

    }

    public void update_user_attempts_num(final int userId, final int attemptsNum, final VolleyCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                updateUserAttemptsNumURL, new Response.Listener<String>() {

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
                Map<String, String> params = new HashMap<String, String>();

                params.put("attempts_num", attemptsNum + "");
                params.put("user_id", userId + "");

                return params;
            }

        };
        strReq.setShouldCache(false);
        requestQueue.add(strReq);

    }

    public void user_fixed_enable(final int userId, final String enableCode, final VolleyCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                permUserEnableCodeURL, new Response.Listener<String>() {

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
                Map<String, String> params = new HashMap<String, String>();

                params.put("enable_code", enableCode);
                params.put("user_id", userId + "");

                return params;
            }

        };
        strReq.setShouldCache(false);
        requestQueue.add(strReq);

    }

    public void user_temp_enable(final int userId, final String enableCode, final UserCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                tempUserEnableCodeURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Toast.makeText(ctx, "" + response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject user = new JSONObject(object.get("user").toString());
                    int id = Integer.parseInt(user.getString("id"));
                    String name = user.getString("name");
                    String password = user.getString("password");
                    String mobile_num = user.getString("mobile_num");
                    String email = null;
                    if (!user.isNull("email"))
                        email = user.getString("email");

                    int subscribe_type = 0;
                    if (!user.isNull("subscribe_type"))
                        subscribe_type = Integer.parseInt(user.getString("subscribe_type"));

                    String enable_code = null;
                    if (!user.isNull("enable_code"))
                        enable_code = user.getString("enable_code");

                    int attempt_num = 0;
                    if (!user.isNull("attempt_num"))
                        attempt_num = Integer.parseInt(user.getString("attempt_num"));

                    int money_amount = 0;
                    if (!user.isNull("money_amount"))
                        money_amount = Integer.parseInt(user.getString("money_amount"));

                    UserModel userModel = new UserModel(id, name, mobile_num, email, enable_code, attempt_num, money_amount, subscribe_type);

                    Log.e("hhh", "" + user.toString());

                    callback.onSuccess(userModel);
//                    Log.e("hhh", "" + user.get("mobile_num"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFail(2);
                    Log.e("eeee", "1" + e.getMessage());
                }
               /* if (response.contains("1")) {
                    Log.e("result", "ok");
                    callback.onSuccess("ok");
                } else {
                    Log.e("result", "no");
                    callback.onSuccess("no");
                }*/
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFail(3);
//                Toast.makeText(ctx, "" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("enable_code", enableCode);
                params.put("user_id", userId + "");
                return params;
            }
        };
        strReq.setShouldCache(false);
        requestQueue.add(strReq);
    }
}
