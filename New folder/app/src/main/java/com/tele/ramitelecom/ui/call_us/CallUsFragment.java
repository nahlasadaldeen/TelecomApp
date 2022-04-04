package com.tele.ramitelecom.ui.call_us;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.tele.ramitelecom.R;

public class CallUsFragment extends Fragment {

    private CallUsViewModel callUsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        callUsViewModel = ViewModelProviders.of(this).get(CallUsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logout, container, false);

        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=9630967283407 &text= "+getString(R.string.app_question));

        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(sendIntent);

        return root;
    }
}