package com.tele.ramitelecom.ui.mobile_permit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.tele.ramitelecom.R;

public class MobilePermitFragment extends Fragment {

    private MobilePermitViewModel mobilePermitViewModel;
    EditText editTextId;

    Button btnVerify;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mobilePermitViewModel =
                ViewModelProviders.of(this).get(MobilePermitViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mobile_permit, container, false);

        btnVerify = root.findViewById(R.id.btnVerify);
        editTextId = root.findViewById(R.id.editTextId);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String permit_code = "*134*1*2*p#";

                Intent phone_intent = new Intent(Intent.ACTION_CALL);

                String mobile_emie = editTextId.getText().toString();
                if (mobile_emie.trim().isEmpty()) {
                    editTextId.setError(getString(R.string.required));
                    return;
                }
                String permit_cod = permit_code.replace("p", mobile_emie);
                phone_intent.setData(Uri.parse("tel:" + Uri.encode(permit_cod)));
                // start Intent
                startActivity(phone_intent);

            }
        });

        return root;
    }
}