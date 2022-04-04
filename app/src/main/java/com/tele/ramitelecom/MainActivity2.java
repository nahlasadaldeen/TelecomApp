package com.tele.ramitelecom;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.tele.ramitelecom.api_connection.TransfersApi;
import com.tele.ramitelecom.api_connection.VolleyCallback;
import com.tele.ramitelecom.db.TransferTableOperations;
import com.tele.ramitelecom.ui.LoginActivity;
import com.tele.ramitelecom.ui.users.TransferModel;

import java.util.List;

import static com.tele.ramitelecom.ui.helper.Constants.CENTER_ADDRESS;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_CITY;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_NAME;
import static com.tele.ramitelecom.ui.helper.Constants.IS_LOGGED_IN;
import static com.tele.ramitelecom.ui.helper.Constants.MyPREFERENCES;
import static com.tele.ramitelecom.ui.helper.Constants.USERNAME;
import static com.tele.ramitelecom.ui.helper.Constants.USERPHONE;

public class MainActivity2 extends AppCompatActivity {
    NavigationView navigationView;
    NavController navController;
    SharedPreferences sharedpreferences;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        boolean is_logged_in = sharedpreferences.getBoolean(IS_LOGGED_IN, false);
        Intent i;
        if (!is_logged_in) {
            i = new Intent(MainActivity2.this, LoginActivity.class);
            startActivity(i);

            finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkCallPermission();

        syncTransfersAuto();
       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.userName);
        String userName = sharedpreferences.getString(USERNAME, "");
        navUsername.setText(userName);

        TextView navUserPhone = headerView.findViewById(R.id.userPhone);
        String userPhone = sharedpreferences.getString(USERPHONE, "");
        navUserPhone.setText(userPhone);

        TextView navCenterName = headerView.findViewById(R.id.centerName);
        String centerName = sharedpreferences.getString(CENTER_NAME, getString(R.string.main_app));
        navCenterName.setText(centerName);

        /*TextView navCenterPhone = headerView.findViewById(R.id.centerPhone);
        String centerPhone = sharedpreferences.getString(CENTER_, "");
        navCenterPhone.setText(centerPhone);*/

        TextView navCenterAddress = headerView.findViewById(R.id.centerAddress);
        String centerAddress = sharedpreferences.getString(CENTER_ADDRESS, getString(R.string.dablan));
        navCenterAddress.setText(centerAddress);

        TextView navCenterCity = headerView.findViewById(R.id.centerCity);
        String centerCity = sharedpreferences.getString(CENTER_CITY, getString(R.string.homs_city));
        navCenterCity.setText(centerCity);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, /*R.id.nav_users_settings,*/ R.id.nav_sim_one_settings, R.id.nav_sim_all_settings,R.id.nav_syriatel_code_for_deliver_settings,
                R.id.nav_users_loans, R.id.nav_user_code, R.id.nav_mobile_permit, R.id.nav_benefits, R.id.nav_share, R.id.nav_logout, R.id.nav_call_us, R.id.nav_about_us)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    private void syncTransfersAuto() {
        if (!isNetworkAvailable()) {
            Toast.makeText(MainActivity2.this, getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        // get non syned records from transfer table
        final TransferTableOperations transferTableOperations1 = new TransferTableOperations(MainActivity2.this);

        final List<TransferModel> unSyncTransfers = transferTableOperations1.getAllUnSyncTransferData();

//        Toast.makeText(MainActivity2.this, unSyncTransfers.size() + "", Toast.LENGTH_SHORT).show();

        if (!unSyncTransfers.isEmpty()) {
            // send all to server
            for (int i = 0; i < unSyncTransfers.size(); i++) {
                TransfersApi transfersApi = new TransfersApi(MainActivity2.this);
                final int finalI = i;
                transfersApi.transfer_insert(unSyncTransfers.get(i), new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("ok")) {
                            Toast.makeText(MainActivity2.this, R.string.transfer_saved_on_server, Toast.LENGTH_SHORT).show();

                            int up = transferTableOperations1.updateIsSync(unSyncTransfers.get(finalI).id);
                            if (up != -1)
                                Toast.makeText(MainActivity2.this, R.string.syc_upated, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(MainActivity2.this, R.string.err_somthing_wrong, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    public void checkCallPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            //callPhone();
            // Toast.makeText(this, getString(R.string.perm_granted), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.perm_granted), Toast.LENGTH_LONG).show();
                    //callPhone();
                }
            }
        }
    }

    public void onSim1RadioButtonClicked(View view) {
        // Is the button now checked?
       /* boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_syriatel:
                if (checked) {
                    // syriatel
                    Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_LONG).show();
                    break;
                }
            case R.id.radio_mtn: {
                if (checked)
                    // mtn
                    Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
                break;
            }
        }*/
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) MainActivity2.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onSim2RadioButtonClicked(View view) {
        // Is the button now checked?
       /* boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_syriatel2:
                if (checked) {
                    // syriatel2
                    Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_LONG).show();
                    break;
                }
            case R.id.radio_mtn2: {
                if (checked)
                    // mtn2
                    Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
                break;
            }
        }*/
    }

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_logout) {
            sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            boolean is_logged_in = sharedpreferences.getBoolean(IS_LOGGED_IN, false);
            if (is_logged_in) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(MainActivity2.this, getString(R.string.user_logged_out), Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(MainActivity2.this, LoginActivity.class);
                startActivity(homeIntent);
                finish();
                return false;
            }

        } else {
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
            return true;

        }
        return false;
    }*/
}