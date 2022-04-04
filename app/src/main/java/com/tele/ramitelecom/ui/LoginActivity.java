package com.tele.ramitelecom.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tele.ramitelecom.MainActivity2;
import com.tele.ramitelecom.R;
import com.tele.ramitelecom.api_connection.UserApi;
import com.tele.ramitelecom.api_connection.UserCallback;
import com.tele.ramitelecom.ui.users.UserModel;

import java.util.Locale;
import java.util.UUID;

import static com.tele.ramitelecom.ui.helper.Constants.ACTIVE_CENTER_USERS_COUNT;
import static com.tele.ramitelecom.ui.helper.Constants.ALL_ATTEMPTS_NUM;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_ADDRESS;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_CITY;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_ID;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_NAME;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_USERS_COUNT;
import static com.tele.ramitelecom.ui.helper.Constants.IS_LOGGED_IN;
import static com.tele.ramitelecom.ui.helper.Constants.MyPREFERENCES;
import static com.tele.ramitelecom.ui.helper.Constants.USERNAME;
import static com.tele.ramitelecom.ui.helper.Constants.USERPHONE;
import static com.tele.ramitelecom.ui.helper.Constants.USER_ID;
import static com.tele.ramitelecom.ui.helper.Constants.USER_SUBSCRIPTION_TYPE;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int REQUEST_PHONE_STATE = 2;

    EditText username, password;
    Button btnLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale(this);
        setContentView(R.layout.activity_login);

        checkCallPermission();
        checkForPhoneStatePermission();
        username = findViewById(R.id.editTextUserName);
        password = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar1);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name, pass_word;
                user_name = username.getText().toString();
                pass_word = password.getText().toString();
                if (user_name.isEmpty()) {
                    username.setError(getString(R.string.required));
                    return;
                }
                if (pass_word.isEmpty()) {
                    password.setError(getString(R.string.required));
                    return;
                }
                if (!isNetworkAvailable()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                UserApi userApi = new UserApi(LoginActivity.this);
                userApi.user_login(user_name, pass_word, new UserCallback() {
                    @Override
                    public void onSuccess(UserModel user) {
                        saveUserData(user);
                        Intent i = new Intent(LoginActivity.this, MainActivity2.class);
                        startActivity(i);
                        // close this activity
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode) {
                        progressBar.setVisibility(View.GONE);
                        if (errorCode == 1) {
                            Toast.makeText(LoginActivity.this, R.string.user_logged_in_on_second_device, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(LoginActivity.this, R.string.err_in_login, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void checkCallPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            //callPhone();
            // Toast.makeText(this, getString(R.string.perm_granted), Toast.LENGTH_LONG).show();
        }
    }

    private boolean saveUserData(UserModel user) {
        sharedpreferences = LoginActivity.this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(USERNAME, user.name);
        editor.putString(USERPHONE, user.mobileNum);
        editor.putInt(USER_ID, user.id);
        editor.putInt(ALL_ATTEMPTS_NUM, user.attemptsNum);
        editor.putInt(USER_SUBSCRIPTION_TYPE, user.subscribeType);
        if (user.is_center != 0) {
            editor.putInt(CENTER_ID, user.center_id);
            editor.putString(CENTER_NAME, user.center_details.center_name);
            editor.putString(CENTER_CITY, user.center_details.center_city);
//            editor.putString(CENTER_PHONE, user.center_details.ce);

            editor.putString(CENTER_ADDRESS, user.center_details.center_address);
            editor.putInt(CENTER_USERS_COUNT, user.center_details.users_count);
            editor.putInt(ACTIVE_CENTER_USERS_COUNT, user.center_details.active_users_count);
        }

        if (user.center_id != 0) {
            editor.putInt(CENTER_ID, user.center_id);
            editor.putString(CENTER_NAME, user.center_details.center_name);
            editor.putString(CENTER_CITY, user.center_details.center_city);
//            editor.putString(CENTER_PHONE, user.center_details.ce);

            editor.putString(CENTER_ADDRESS, user.center_details.center_address);
            editor.putInt(CENTER_USERS_COUNT, user.center_details.users_count);
            editor.putInt(ACTIVE_CENTER_USERS_COUNT, user.center_details.active_users_count);
        }

        editor.putBoolean(IS_LOGGED_IN, true);

        editor.apply();
        Toast.makeText(LoginActivity.this, R.string.user_logged_in, Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkForPhoneStatePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(LoginActivity.this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                        Manifest.permission.READ_PHONE_STATE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    showPermissionMessage();

                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            REQUEST_PHONE_STATE);
                }
            }
        } else {
            //... Permission has already been granted, obtain the UUID
            getDeviceUuId(LoginActivity.this);
        }

    }

    public static String getDeviceUuId(Activity context) {
        String UUID = "";
        String android_id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        final TelephonyManager tm = (TelephonyManager) context.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        if (tm != null) {
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            java.util.UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            UUID = deviceUuid.toString();
            return UUID;
        }
        return UUID;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.perm_granted), Toast.LENGTH_LONG).show();
                    //callPhone();
                }
            }
            case REQUEST_PHONE_STATE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // .. Can now obtain the UUID
                    getDeviceUuId(LoginActivity.this);
                } else {
                    Toast.makeText(LoginActivity.this, "Unable to continue without granting permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showPermissionMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Read phone state")
                .setMessage("This app requires the permission to read phone state to continue")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LoginActivity.this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                REQUEST_PHONE_STATE);
                    }
                }).create().show();
    }

    public static void setLocale(Activity context) {
        Locale locale;

        locale = new Locale("ar");
        Configuration config = new Configuration(context.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);

        context.getBaseContext().getResources().updateConfiguration(config,
                context.getBaseContext().getResources().getDisplayMetrics());
    }
}