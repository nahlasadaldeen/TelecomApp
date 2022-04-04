package com.tele.ramitelecom.ui.center_details;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.tele.ramitelecom.R;

import static com.tele.ramitelecom.ui.helper.Constants.ACTIVE_CENTER_USERS_COUNT;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_ADDRESS;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_CITY;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_NAME;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_USERS_COUNT;
import static com.tele.ramitelecom.ui.helper.Constants.MyPREFERENCES;

public class CenterDetailsFragment extends Fragment {

    SharedPreferences sharedpreferences;
    private CenterDetailsViewModel centerDetailsViewModel;

    TextView txtCenterName, txtCenterCity, txtCenterAddress, txtAllCenterUsersCount, txtUsedCenterUsersCount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        centerDetailsViewModel =
                ViewModelProviders.of(this).get(CenterDetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_center, container, false);

        sharedpreferences = requireContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        txtCenterName = root.findViewById(R.id.txtCenterName);
        txtCenterCity = root.findViewById(R.id.txtCenterCity);
        txtCenterAddress = root.findViewById(R.id.txtCenterAddress);
        txtAllCenterUsersCount = root.findViewById(R.id.txtAllCenterUsersCount);
        txtUsedCenterUsersCount = root.findViewById(R.id.txtUsedCenterUsersCount);

        String center_name = sharedpreferences.getString(CENTER_NAME, "");
        String center_city = sharedpreferences.getString(CENTER_CITY, "");
        String center_address = sharedpreferences.getString(CENTER_ADDRESS, "");
        int center_users_count = sharedpreferences.getInt(CENTER_USERS_COUNT, 0);
        int active_center_users_count = sharedpreferences.getInt(ACTIVE_CENTER_USERS_COUNT, 0);

        txtCenterName.setText(center_name);
        txtCenterCity.setText(center_city);
        txtCenterAddress.setText(center_address);
        txtAllCenterUsersCount.setText(center_users_count + "");
        txtUsedCenterUsersCount.setText(active_center_users_count + "");

        return root;
    }
}