package com.tele.ramitelecom.ui.select_for_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.tele.ramitelecom.R;

import static com.tele.ramitelecom.ui.helper.Constants.MyPREFERENCES;
import static com.tele.ramitelecom.ui.helper.Constants.syriatel_sim_code;

public class SelectForClientFragment extends Fragment {
    SharedPreferences sharedpreferences;
    private SelectForClientViewModel selectForClientViewModel;
    EditText editTextClientCode;
    TextView txtBalance;

    Button btnSelect;

    int selectedSelectionType = 0;
    Spinner selectType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        selectForClientViewModel =
                ViewModelProviders.of(this).get(SelectForClientViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_select_for_client, container, false);

        btnSelect = root.findViewById(R.id.btnSelect);
        editTextClientCode = root.findViewById(R.id.editTextClientCode);
        selectType = root.findViewById(R.id.spinSelectType);
        selectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) // Syriatel One
                {
                    selectedSelectionType = 0;
                }
                if (position == 1) // MTN One
                {
                    selectedSelectionType = 1;
                }
                if (position == 2) // Syriatel All
                {
                    selectedSelectionType = 2;
                }
                if (position == 3) // MTN All
                {
                    selectedSelectionType = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpreferences = requireContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String syriatel_sim_code_value = sharedpreferences.getString(syriatel_sim_code, "");
                String select_code = "";

                if (selectedSelectionType == 0)
                    select_code = "*150*2*m*c*2#";
                if (selectedSelectionType == 1)
                    select_code = "*155*1#";
                if (selectedSelectionType == 2)
                    select_code = "*150*2*m*c*1#";
                if (selectedSelectionType == 3)
                    select_code = "*155*1#";

                Intent phone_intent = new Intent(Intent.ACTION_CALL);

                String client_code = editTextClientCode.getText().toString();
                if (client_code.trim().isEmpty() && (selectedSelectionType == 0 || selectedSelectionType == 2)) {
                    editTextClientCode.setError(getString(R.string.required));
                    return;
                }
                select_code = select_code.replace("m", client_code);
                select_code = select_code.replace("c", syriatel_sim_code_value);
                phone_intent.setData(Uri.parse("tel:" + Uri.encode(select_code)));
                // start Intent
                startActivity(phone_intent);
                String myBalance = sharedpreferences.getString("my_balance", "");

                txtBalance = root.findViewById(R.id.txtBalance);
                txtBalance.setText(myBalance);

            }
        });
       /*
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("Send");
        for (AccessibilityNodeInfo node : list) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }*/

        return root;
    }


}