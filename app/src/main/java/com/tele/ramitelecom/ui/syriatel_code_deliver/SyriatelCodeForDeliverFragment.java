package com.tele.ramitelecom.ui.syriatel_code_deliver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.tele.ramitelecom.R;

import static com.tele.ramitelecom.ui.helper.Constants.ALL_SYRIATEL;
import static com.tele.ramitelecom.ui.helper.Constants.MyPREFERENCES;
import static com.tele.ramitelecom.ui.helper.Constants.ONE_BALANCE_SYRIATEL;
import static com.tele.ramitelecom.ui.helper.Constants.ONE_BILL_SYRIATEL;

public class SyriatelCodeForDeliverFragment extends Fragment {

    SharedPreferences sharedpreferences;
    private SyriatelCodeForDeliverViewModel syriatelCodeForDeliverViewModel;
    EditText syriatelCodeForDeliver;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        syriatelCodeForDeliverViewModel =
                ViewModelProviders.of(this).get(SyriatelCodeForDeliverViewModel.class);
        View root = inflater.inflate(R.layout.fragment_syriatel_code_for_deliver, container, false);

        syriatelCodeForDeliver = root.findViewById(R.id.syriatelCode);

        Button btnSaveSettings = root.findViewById(R.id.btnSaveSettings);
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String syriatelCode = syriatelCodeForDeliver.getText().toString();
                if (syriatelCode != null && !syriatelCode.isEmpty()) {
                    if (syriatelCode.length() != 3) {
                        Toast.makeText(requireContext(), getString(R.string.err_check_syriatel_code), Toast.LENGTH_LONG).show();
                    } else {
                        if (saveSettings(syriatelCode)) {
                            NavHostFragment.findNavController(requireParentFragment()).navigateUp();
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), getString(R.string.err_specify_syriatel_code), Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }

    private boolean saveSettings(String syriatelCode) {
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        String one_balance_syriatel = sharedpreferences.getString(ONE_BALANCE_SYRIATEL, "");
        String one_bill_syriatel = sharedpreferences.getString(ONE_BILL_SYRIATEL, "");
        String all_syriatel = sharedpreferences.getString(ALL_SYRIATEL, "");

        String oneBalanceSyriatelTransferCode = "*" + syriatelCode + one_balance_syriatel.substring(4);
        editor.putString(ONE_BALANCE_SYRIATEL, oneBalanceSyriatelTransferCode);
        String oneBillSyriatelTransferCode = "*" + syriatelCode + one_bill_syriatel.substring(4);
        editor.putString(ONE_BILL_SYRIATEL, oneBillSyriatelTransferCode);
        String allSyriatelTransferCode = "*" + syriatelCode + all_syriatel.substring(4);
        editor.putString(ALL_SYRIATEL, allSyriatelTransferCode);

        editor.apply();
        Toast.makeText(requireContext(), R.string.settings_saved, Toast.LENGTH_SHORT).show();
        return true;
    }
}