package com.tele.ramitelecom.ui.sim_all_settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.tele.ramitelecom.R;

import static com.tele.ramitelecom.ui.helper.Constants.ALL_MTN;
import static com.tele.ramitelecom.ui.helper.Constants.ALL_SYRIATEL;

public class SimAllSettingsFragment extends Fragment {

    SharedPreferences sharedpreferences;
    private SimAllSettingsViewModel simSettingsViewModel;
    EditText editTextCodeSyriatel, editTextDistributorCodeSyriatel, editTextCodeMTN;
    String syriatel_sim_code_value, syriatel_distributor_code_value, mtn_sim_code_value;
    int sim1_value, sim2_value; // 0 for syriatel, 1 for mtn

    RadioButton radio_syriatel, radio_syriatel2, radio_mtn, radio_mtn2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        simSettingsViewModel =
                ViewModelProviders.of(this).get(SimAllSettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sim_all_settings, container, false);

        editTextCodeSyriatel = root.findViewById(R.id.editTextCode);
        editTextDistributorCodeSyriatel = root.findViewById(R.id.editTextDistributorCode);
        editTextCodeMTN = root.findViewById(R.id.editTextCode2);
        radio_syriatel = root.findViewById(R.id.radio_syriatel);
        radio_syriatel2 = root.findViewById(R.id.radio_syriatel2);
        radio_mtn = root.findViewById(R.id.radio_mtn);
        radio_mtn2 = root.findViewById(R.id.radio_mtn2);
        readSettings();

        Button btnSaveSettings = root.findViewById(R.id.btnSaveSettings);
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_syriatel.isChecked() && radio_syriatel2.isChecked()) {
                    Toast.makeText(requireContext(), R.string.err_two_syriatel, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (radio_mtn.isChecked() && radio_mtn2.isChecked()) {
                    Toast.makeText(requireContext(), R.string.err_two_mtn, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (saveSettings()) {
                    NavHostFragment.findNavController(requireParentFragment()).navigateUp();
                }
            }
        });
        /*final TextView textView = root.findViewById(R.id.text_slideshow);
        simSettingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    private boolean saveSettings() {
        syriatel_sim_code_value = editTextCodeSyriatel.getText().toString();
        syriatel_distributor_code_value = editTextDistributorCodeSyriatel.getText().toString();
        mtn_sim_code_value = editTextCodeMTN.getText().toString();
        sim1_value = radio_syriatel.isChecked() ? 0 : 1;
        sim2_value = radio_syriatel2.isChecked() ? 0 : 1;
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(syriatel_sim_code, syriatel_sim_code_value);
        editor.putString(syriatel_distributor_code, syriatel_distributor_code_value);

        String allSyriatelTransferCode = "*150*3*" + syriatel_distributor_code_value + "*" + syriatel_sim_code_value + "*c*c*1*m#";
        editor.putString(ALL_SYRIATEL, allSyriatelTransferCode);


        editor.putString(mtn_sim_code, mtn_sim_code_value);
        String allMtnTransferCode = "*150*" + mtn_sim_code_value + "*c*p*m#";
        editor.putString(ALL_MTN, allMtnTransferCode);


        editor.putInt(sim1, sim1_value);

        editor.putInt(sim2, sim2_value);

        editor.putInt(attempts_count, 50);

        editor.apply();
        Toast.makeText(requireContext(), R.string.settings_saved, Toast.LENGTH_SHORT).show();
        return true;
    }

    private void readSettings() {
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String syriatel_sim_code_value = sharedpreferences.getString(syriatel_sim_code, "");
        String syriatel_distributor_code_value = sharedpreferences.getString(syriatel_distributor_code, "");
        String mtn_sim_code_value = sharedpreferences.getString(mtn_sim_code, "");
        int sim1_value = sharedpreferences.getInt(sim1, 0);
        int sim2_value = sharedpreferences.getInt(sim2, 1);
        int attempts_count_val = sharedpreferences.getInt(attempts_count, 0);
        editTextCodeSyriatel.setText(syriatel_sim_code_value);
        editTextDistributorCodeSyriatel.setText(syriatel_distributor_code_value);
        editTextCodeMTN.setText(mtn_sim_code_value);
        if (sim1_value == 0) radio_syriatel.setChecked(true);
        else radio_mtn.setChecked(true);
        if (sim2_value == 0) radio_syriatel2.setChecked(true);
        else radio_mtn2.setChecked(true);

    }

    public static final String MyPREFERENCES = "rami_telecom_prefs";
    public static final String syriatel_sim_code = "syriatel_sim_all_code";
    public static final String syriatel_distributor_code = "syriatel_distributor_all_code";
    public static final String mtn_sim_code = "mtn_sim_all_code";
    public static final String sim1 = "sim1_all";
    public static final String sim2 = "sim2_all";
    public static final String attempts_count = "attempts_count_all";

}