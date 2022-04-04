package com.tele.ramitelecom.ui.logout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.tele.ramitelecom.R;
import com.tele.ramitelecom.api_connection.UserApi;
import com.tele.ramitelecom.api_connection.VolleyCallback;
import com.tele.ramitelecom.ui.LoginActivity;

import static com.tele.ramitelecom.ui.helper.Constants.IS_LOGGED_IN;
import static com.tele.ramitelecom.ui.helper.Constants.MyPREFERENCES;
import static com.tele.ramitelecom.ui.helper.Constants.USER_ID;

public class LogoutFragment extends Fragment {

    SharedPreferences sharedpreferences;

    private LogoutViewModel logoutViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        logoutViewModel = ViewModelProviders.of(this).get(LogoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logout, container, false);

        sharedpreferences = requireContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        UserApi userApi = new UserApi(requireContext());

        if (!isNetworkAvailable()) {
            Toast.makeText(requireContext(), getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
            return root;
        }
        int id = sharedpreferences.getInt(USER_ID, 0);


       /* List<TransferModel> transfersList;
        transfersList = transferTableOperations.getOneAndAllTransferData();
        int doneAttemptsCount = transfersList.size();
        int allAttempts = sharedpreferences.getInt(ALL_ATTEMPTS_NUM, 0);

        int remainedAttemptsCount = allAttempts - doneAttemptsCount;

        userApi.update_user_attempts_num(id, remainedAttemptsCount, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("ok")) {
                    Toast.makeText(requireContext(), "attempts updated", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(requireContext(), R.string.err_somthing_wrong, Toast.LENGTH_SHORT).show();
            }
        });*/

        userApi.user_logout(id, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("ok")) {
                    boolean is_logged_in = sharedpreferences.getBoolean(IS_LOGGED_IN, false);
                    if (is_logged_in) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.apply();
                        Toast.makeText(requireContext(), getString(R.string.user_logged_out), Toast.LENGTH_SHORT).show();
                        Intent homeIntent = new Intent(requireContext(), LoginActivity.class);
                        startActivity(homeIntent);
                        requireActivity().finish();
                    }
                } else
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}