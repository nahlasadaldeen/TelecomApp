package com.tele.ramitelecom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.tele.ramitelecom.ui.LoginActivity;

import java.util.Locale;

import static com.tele.ramitelecom.ui.helper.Constants.IS_LOGGED_IN;
import static com.tele.ramitelecom.ui.helper.Constants.MyPREFERENCES;

public class LoadingActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale(this);
        setContentView(R.layout.activity_loading);

        int SPLASH_TIME_OUT = 2000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                boolean is_logged_in = sharedpreferences.getBoolean(IS_LOGGED_IN, false);
                Intent i;
                if (!is_logged_in) {
                    i = new Intent(LoadingActivity.this, LoginActivity.class);
                } else {
                    i = new Intent(LoadingActivity.this, MainActivity2.class);
                }
                startActivity(i);
               /* Intent i = new Intent(LoadingActivity.this, MainActivity2.class);
                startActivity(i);*/
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

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