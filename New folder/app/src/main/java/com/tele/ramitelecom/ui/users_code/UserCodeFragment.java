package com.tele.ramitelecom.ui.users_code;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.tele.ramitelecom.R;
import com.tele.ramitelecom.api_connection.TransfersApi;
import com.tele.ramitelecom.api_connection.VolleyCallback;
import com.tele.ramitelecom.db.TransferTableOperations;
import com.tele.ramitelecom.db.UserTableOperations;
import com.tele.ramitelecom.ui.helper.TransferCode;
import com.tele.ramitelecom.ui.users.TransferModel;
import com.tele.ramitelecom.ui.users.UserModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.tele.ramitelecom.ui.helper.Constants.ALL_ATTEMPTS_NUM;
import static com.tele.ramitelecom.ui.helper.Constants.MyPREFERENCES;
import static com.tele.ramitelecom.ui.helper.Constants.REMAIN_ATTEMPTS_NUM;
import static com.tele.ramitelecom.ui.helper.Constants.USER_ID;
import static com.tele.ramitelecom.ui.helper.Constants.USER_SUBSCRIPTION_TYPE;

public class UserCodeFragment extends Fragment {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    AlertDialog dialog;

    MaterialCardView pnlMobilePermit, pnlAll;
    boolean validBalance = true;
    boolean validMobileNum = true;
    MaterialCheckBox btnByLate;
    private UserCodeViewModel userCodeViewModel;
    EditText editTextCode, editTextBalance, editTextPrice, editTextMobileMtn;

    Button btnCall, btnLog, btnAdd, btnReCall;

    int selectedCompanyType = 0;
    String code = "", balance = "", mobileMtn = "";
    String allSyriatelCode = "";
    String allMtnCode = "";
    String allBillMtnCode = "";
    String priceStr = "";
    String transfer_code = "";
    Spinner companyType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userCodeViewModel =
                ViewModelProviders.of(this).get(UserCodeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user_code, container, false);
        sharedpreferences = requireContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        final TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());

        companyType = root.findViewById(R.id.spinCompanyType);

        pnlMobilePermit = root.findViewById(R.id.pnlMobilePermit);
        pnlAll = root.findViewById(R.id.pnlAll);


        btnByLate = root.findViewById(R.id.btnByLate);

        btnCall = root.findViewById(R.id.btnCall);
        btnLog = root.findViewById(R.id.btnLog);
        btnAdd = root.findViewById(R.id.btnAdd);
        btnReCall = root.findViewById(R.id.btnReCall);

        editTextCode = root.findViewById(R.id.editTextCode);
        editTextBalance = root.findViewById(R.id.editTextBalance);
        editTextPrice = root.findViewById(R.id.editTextPrice);

        editTextMobileMtn = root.findViewById(R.id.editTextMobileMtn);

        companyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) // syriatel
                {
                    pnlAll.setVisibility(View.VISIBLE);
                    pnlMobilePermit.setVisibility(View.GONE);

                    selectedCompanyType = 1;

                    editTextMobileMtn.setText("");
                    editTextCode.setText("");
                    editTextBalance.setText("");
                    editTextPrice.setText("");
                    editTextMobileMtn.setVisibility(View.GONE);
                } else if (position == 1) // mtn
                {
                    pnlAll.setVisibility(View.VISIBLE);
                    pnlMobilePermit.setVisibility(View.GONE);

                    selectedCompanyType = 2;

                    editTextMobileMtn.setText("");
                    editTextCode.setText("");
                    editTextBalance.setText("");
                    editTextPrice.setText("");
                    editTextMobileMtn.setVisibility(View.VISIBLE);
                } else if (position == 2) // mtn bills
                {
                    pnlAll.setVisibility(View.VISIBLE);
                    pnlMobilePermit.setVisibility(View.GONE);

                    selectedCompanyType = 3;

                    editTextMobileMtn.setText("");
                    editTextCode.setText("");
                    editTextBalance.setText("");
                    editTextPrice.setText("");
                    editTextMobileMtn.setVisibility(View.VISIBLE);
                } else // mobile phone permit
                {
                    pnlAll.setVisibility(View.GONE);
                    pnlMobilePermit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] lastChar = {" "};
        editTextBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = editTextBalance.getText().toString().length();
                if (digits > 1)
                    lastChar[0] = editTextBalance.getText().toString().substring(digits - 1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextPrice.setText("");
                int digits = editTextBalance.getText().toString().length();
                Log.d("LENGTH", "" + digits);
                if (digits == 1) {
                    String num = s.toString();
                    int numm = Integer.parseInt(num);
                    if (numm < 1) {
                        validBalance = false;
                        editTextBalance.setError(getString(R.string.err_balance));
                    }
                    int res = calculatePrice(numm);
                    editTextPrice.setText(res + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                editTextPrice.setText("");

                String num = s.toString();
                if (!num.equals("")) {
                    int numm = Integer.parseInt(num);
                    if (numm < 1) {
                        validBalance = false;
                        editTextBalance.setError(getString(R.string.err_balance));
                    }

                    int res = calculatePrice(numm);
                    editTextPrice.setText(res + "");
                }
            }
        });

        editTextMobileMtn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = editTextMobileMtn.getText().toString().length();
                if (digits > 1)
                    lastChar[0] = editTextMobileMtn.getText().toString().substring(digits - 1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = editTextMobileMtn.getText().toString().length();
                Log.d("LENGTH", "" + digits);
                if (digits == 1) {
                    String num = s.toString();
                    int numm = Integer.parseInt(num);
                    if (numm != 0) {
                        validMobileNum = false;
                        editTextMobileMtn.setError(getString(R.string.err_not_phone));
                    }
                }
                if (digits == 2) {
                    String num = s.toString();
                    int numm = Integer.parseInt(num);
                    if (numm != 9) {
                        validMobileNum = false;
                        editTextMobileMtn.setError(getString(R.string.err_not_phone));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(requireParentFragment()).navigate(
                        UserCodeFragmentDirections.actionNavUserCodeToNavAllTransfersLog());
            }
        });

        btnReCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int allAttempts = sharedpreferences.getInt(ALL_ATTEMPTS_NUM, 0);
                List<TransferModel> transfersList;
                transfersList = transferTableOperations.getOneAndAllTransferData();
                int doneAttemptsCount = transfersList.size();
                int remainedAttemptsCount = allAttempts - doneAttemptsCount;
                editor.putInt(REMAIN_ATTEMPTS_NUM, remainedAttemptsCount);
                editTextMobileMtn.setText(mobileMtn);
                editTextPrice.setText(priceStr);
                editTextCode.setText(transfer_code);
                editTextBalance.setText(balance);
                final int subType = sharedpreferences.getInt(USER_SUBSCRIPTION_TYPE, 0);
                if (subType == 1) {
                    if (remainedAttemptsCount > 0) {
                        Intent phone_intent = new Intent(Intent.ACTION_CALL);
                        if (selectedCompanyType == 1) // syriatel (get syriatel all code from helper class)
                        {
                            allSyriatelCode = new TransferCode(requireContext()).getAllSyriatelTransferCode();
                            if (allSyriatelCode.isEmpty()) {
                                Toast.makeText(requireContext(), getString(R.string.err_not_settings), Toast.LENGTH_LONG).show();
                                return;
                            }
                            Log.e("allSyriatelCode", allSyriatelCode);

                            String allSyriatelCodeForTransfer = allSyriatelCode.replaceAll("c", code);
                            String allSyriatelCodeForTransferFinal = allSyriatelCodeForTransfer.replace("m", balance);
                            Log.e("allSyriatelCodeTransfer", allSyriatelCodeForTransferFinal);

                            phone_intent.setData(Uri.parse("tel:" + Uri.encode(allSyriatelCodeForTransferFinal)));

                            //  startActivity(phone_intent);
                        }
                        if (selectedCompanyType == 2) // mtn (get mtn all code from helper class)
                        {
                            allMtnCode = new TransferCode(requireContext()).getAllMtnTransferCode();
                            if (allMtnCode.isEmpty()) {
                                Toast.makeText(requireContext(), getString(R.string.err_not_settings), Toast.LENGTH_LONG).show();
                                return;
                            }
                            Log.e("allMtnCode", allMtnCode);

                            String allMtnCodeForTransfer = allMtnCode.replaceAll("c", code);
                            String allMtnCodeForTransfer2 = allMtnCodeForTransfer.replace("p", mobileMtn);
                            String allMtnCodeForTransferFinal = allMtnCodeForTransfer2.replace("m", balance);
                            Log.e("allMTNCodeTransfer", allMtnCodeForTransferFinal);
                            phone_intent.setData(Uri.parse("tel:" + Uri.encode(allMtnCodeForTransferFinal)));

                            // startActivity(phone_intent);
                        }
                        if (selectedCompanyType == 3) // mtn all bill (get mtn all code from helper class)
                        {
                            allBillMtnCode = "*154*c*p*m#";
                            if (allBillMtnCode.isEmpty()) {
                                Toast.makeText(requireContext(), getString(R.string.err_not_settings), Toast.LENGTH_LONG).show();
                                return;
                            }
                            Log.e("allBillMtnCode", allBillMtnCode);

                            String allMtnCodeForTransfer = allBillMtnCode.replaceAll("c", code);
                            String allMtnCodeForTransfer2 = allMtnCodeForTransfer.replace("p", mobileMtn);
                            String allMtnCodeForTransferFinal = allMtnCodeForTransfer2.replace("m", balance);
                            Log.e("allBillMTNCodeTransfer", allMtnCodeForTransferFinal);
                            phone_intent.setData(Uri.parse("tel:" + Uri.encode(allMtnCodeForTransferFinal)));

                            // startActivity(phone_intent);
                        }

                    } else {
                        Toast.makeText(requireContext(), R.string.must_activate, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // ( get syriatel all code from helper class, get mtn all code too)

                    Intent phone_intent = new Intent(Intent.ACTION_CALL);
                    if (selectedCompanyType == 1) // syriatel (get syriatel all code from helper class)
                    {
                        allSyriatelCode = new TransferCode(requireContext()).getAllSyriatelTransferCode();
                        if (allSyriatelCode.isEmpty()) {
                            Toast.makeText(requireContext(), getString(R.string.err_not_settings), Toast.LENGTH_LONG).show();
                            return;
                        }
                        Log.e("allSyriatelCode", allSyriatelCode);

                        String allSyriatelCodeForTransfer = allSyriatelCode.replaceAll("c", code);
                        String allSyriatelCodeForTransferFinal = allSyriatelCodeForTransfer.replace("m", balance);
                        Log.e("allSyriatelCodeTransfer", allSyriatelCodeForTransferFinal);

                        phone_intent.setData(Uri.parse("tel:" + Uri.encode(allSyriatelCodeForTransferFinal)));

//                        startActivity(phone_intent);
                    }

                    if (selectedCompanyType == 2) // mtn (get mtn all code from helper class)
                    {
                        allMtnCode = new TransferCode(requireContext()).getAllMtnTransferCode();
                        if (allMtnCode.isEmpty()) {
                            Toast.makeText(requireContext(), getString(R.string.err_not_settings), Toast.LENGTH_LONG).show();
                            return;
                        }
                        Log.e("allMtnCode", allMtnCode);

                        String allMtnCodeForTransfer = allMtnCode.replaceAll("c", code);
                        String allMtnCodeForTransfer2 = allMtnCodeForTransfer.replace("p", mobileMtn);
                        String allMtnCodeForTransferFinal = allMtnCodeForTransfer2.replace("m", balance);
                        Log.e("allMTNCodeTransfer", allMtnCodeForTransferFinal);
                        phone_intent.setData(Uri.parse("tel:" + Uri.encode(allMtnCodeForTransferFinal)));

//                        startActivity(phone_intent);

                    }
                    if (selectedCompanyType == 3) // mtn bills (get mtn all code from helper class)
                    {
                        allBillMtnCode = "*154*c*p*m#";
                        if (allBillMtnCode.isEmpty()) {
                            Toast.makeText(requireContext(), getString(R.string.err_not_settings), Toast.LENGTH_LONG).show();
                            return;
                        }
                        Log.e("allBillMtnCode", allBillMtnCode);

                        String allMtnCodeForTransfer = allBillMtnCode.replaceAll("c", code);
                        String allMtnCodeForTransfer2 = allMtnCodeForTransfer.replace("p", mobileMtn);
                        String allMtnCodeForTransferFinal = allMtnCodeForTransfer2.replace("m", balance);
                        Log.e("allBillMTNCodeTransfer", allMtnCodeForTransferFinal);
                        phone_intent.setData(Uri.parse("tel:" + Uri.encode(allMtnCodeForTransferFinal)));

//                        startActivity(phone_intent);

                    }

                    /*if (!mobileMtn.equals(""))
                        addAllMTNTransferDetails(price, mobileMtn, 2, transfer_code);
                    else
                        addAllSyriatelTransferDetails(price, 2, transfer_code);*/
                }
            }
        });


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCompanyType == 2) {
                    mobileMtn = editTextMobileMtn.getText().toString();
                    if (mobileMtn.trim().equals("")) {
                        editTextMobileMtn.setError(getString(R.string.mobile_num_required));
//                        Toast.makeText(requireContext(), getString(R.string.mobile_num_required), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!validMobileNum) {
                        editTextMobileMtn.setError(getString(R.string.err_not_phone));
//                        Toast.makeText(requireContext(), getString(R.string.err_not_phone), Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                balance = editTextBalance.getText().toString();
                if (balance.trim().equals("")) {
                    editTextBalance.setError(getString(R.string.balance_required));
//                    Toast.makeText(requireContext(), getString(R.string.balance_required), Toast.LENGTH_LONG).show();
                    return;
                }

                code = editTextCode.getText().toString();
                if (code.trim().equals("")) {
                    editTextCode.setError(getString(R.string.code_required));
//                    Toast.makeText(requireContext(), getString(R.string.code_required), Toast.LENGTH_LONG).show();
                    return;
                }
                int allAttempts = sharedpreferences.getInt(ALL_ATTEMPTS_NUM, 0);
                List<TransferModel> transfersList;
                transfersList = transferTableOperations.getOneAndAllTransferData();
                int doneAttemptsCount = transfersList.size();
                int remainedAttemptsCount = allAttempts - doneAttemptsCount;
                editor.putInt(REMAIN_ATTEMPTS_NUM, remainedAttemptsCount);

                final int subType = sharedpreferences.getInt(USER_SUBSCRIPTION_TYPE, 0);
                if (subType == 1) {
                    if (remainedAttemptsCount > 0) {

                        // ( get syriatel all code from helper class, get mtn all code too)
                        priceStr = editTextPrice.getText().toString();
                        transfer_code = editTextCode.getText().toString();

                        Intent phone_intent = new Intent(Intent.ACTION_CALL);
                        int price = Integer.parseInt(priceStr);
                        if (selectedCompanyType == 1) // syriatel (get syriatel all code from helper class)
                        {
                            allSyriatelCode = new TransferCode(requireContext()).getAllSyriatelTransferCode();
                            if (allSyriatelCode.isEmpty()) {
                                Toast.makeText(requireContext(), getString(R.string.err_not_settings), Toast.LENGTH_LONG).show();
                                return;
                            }
                            Log.e("allSyriatelCode", allSyriatelCode);

                            String allSyriatelCodeForTransfer = allSyriatelCode.replaceAll("c", code);
                            String allSyriatelCodeForTransferFinal = allSyriatelCodeForTransfer.replace("m", balance);
                            Log.e("allSyriatelCodeTransfer", allSyriatelCodeForTransferFinal);

                            phone_intent.setData(Uri.parse("tel:" + Uri.encode(allSyriatelCodeForTransferFinal)));

//                    Toast.makeText(requireContext(), "Syriatel: " + allSyriatelCodeForTransferFinal, Toast.LENGTH_LONG).show();

                            // start Intent
                            startActivity(phone_intent);
                        }
                        if (selectedCompanyType == 2) // mtn (get mtn all code from helper class)
                        {
                            allMtnCode = new TransferCode(requireContext()).getAllMtnTransferCode();
                            if (allMtnCode.isEmpty()) {
                                Toast.makeText(requireContext(), getString(R.string.err_not_settings), Toast.LENGTH_LONG).show();
                                return;
                            }
                            Log.e("allMtnCode", allMtnCode);

                            String allMtnCodeForTransfer = allMtnCode.replaceAll("c", code);
                            String allMtnCodeForTransfer2 = allMtnCodeForTransfer.replace("p", mobileMtn);
                            String allMtnCodeForTransferFinal = allMtnCodeForTransfer2.replace("m", balance);
                            Log.e("allMTNCodeTransfer", allMtnCodeForTransferFinal);
                            phone_intent.setData(Uri.parse("tel:" + Uri.encode(allMtnCodeForTransferFinal)));
//                    Toast.makeText(requireContext(), "MTN: " + allMtnCodeForTransferFinal, Toast.LENGTH_LONG).show();

                            // start Intent
                            startActivity(phone_intent);
                        }

                        if (!mobileMtn.equals(""))
                            addAllMTNTransferDetails(price, mobileMtn, 2, transfer_code);
                        else
                            addAllSyriatelTransferDetails(price, 2, transfer_code);

                        if (btnByLate.isChecked()) {
                            // save paying by late
                    /*String priceStr = editTextPrice.getText().toString();
                    String mobileNum = editTextPhone.getText().toString();
                    int price = Integer.parseInt(priceStr);*/
                            // update user record in db to set loan money amount
                            addLoanMoneyAmount(price, mobileMtn);
                        } else {
                        }
              /*  phone_intent
                        .setData(Uri.parse("tel:"
                                + mobileMtn));

                // start Intent
                startActivity(phone_intent);*/
                    } else {
                        Toast.makeText(requireContext(), R.string.must_activate, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // ( get syriatel all code from helper class, get mtn all code too)

                    priceStr = editTextPrice.getText().toString();
                    transfer_code = editTextCode.getText().toString();

                    Intent phone_intent = new Intent(Intent.ACTION_CALL);
                    int price = Integer.parseInt(priceStr);
                    if (selectedCompanyType == 1) // syriatel (get syriatel all code from helper class)
                    {
                        allSyriatelCode = new TransferCode(requireContext()).getAllSyriatelTransferCode();
                        if (allSyriatelCode.isEmpty()) {
                            Toast.makeText(requireContext(), getString(R.string.err_not_settings), Toast.LENGTH_LONG).show();
                            return;
                        }
                        Log.e("allSyriatelCode", allSyriatelCode);

                        String allSyriatelCodeForTransfer = allSyriatelCode.replaceAll("c", code);
                        String allSyriatelCodeForTransferFinal = allSyriatelCodeForTransfer.replace("m", balance);
                        Log.e("allSyriatelCodeTransfer", allSyriatelCodeForTransferFinal);

                        phone_intent.setData(Uri.parse("tel:" + Uri.encode(allSyriatelCodeForTransferFinal)));

//                    Toast.makeText(requireContext(), "Syriatel: " + allSyriatelCodeForTransferFinal, Toast.LENGTH_LONG).show();

                        // start Intent
                        startActivity(phone_intent);
                        editTextCode.setText("");
                    }

                    if (selectedCompanyType == 2) // mtn (get mtn all code from helper class)
                    {
                        allMtnCode = new TransferCode(requireContext()).getAllMtnTransferCode();
                        if (allMtnCode.isEmpty()) {
                            Toast.makeText(requireContext(), getString(R.string.err_not_settings), Toast.LENGTH_LONG).show();
                            return;
                        }
                        Log.e("allMtnCode", allMtnCode);

                        String allMtnCodeForTransfer = allMtnCode.replaceAll("c", code);
                        String allMtnCodeForTransfer2 = allMtnCodeForTransfer.replace("p", mobileMtn);
                        String allMtnCodeForTransferFinal = allMtnCodeForTransfer2.replace("m", balance);
                        Log.e("allMTNCodeTransfer", allMtnCodeForTransferFinal);
                        phone_intent.setData(Uri.parse("tel:" + Uri.encode(allMtnCodeForTransferFinal)));
//                    Toast.makeText(requireContext(), "MTN: " + allMtnCodeForTransferFinal, Toast.LENGTH_LONG).show();

                        // start Intent
                        startActivity(phone_intent);
                        editTextCode.setText("");
                    }

                    if (!mobileMtn.equals(""))
                        addAllMTNTransferDetails(price, mobileMtn, 2, transfer_code);
                    else
                        addAllSyriatelTransferDetails(price, 2, transfer_code);

                    if (btnByLate.isChecked()) {
                        // save paying by late
                        // update user record in db to set loan money amount
                        addLoanMoneyAmount(price, mobileMtn);
                    } else {
                    }
                }
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validMobileNum = true;
                validBalance = true;
                String mtnMobileNum = "";
                if (selectedCompanyType == 2) {
                    mtnMobileNum = editTextMobileMtn.getText().toString();
                    if (mtnMobileNum.trim().equals("")) {
                        editTextMobileMtn.setError(getString(R.string.mobile_num_required));
                        validMobileNum = false;
                        editTextMobileMtn.requestFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                    }
                    if (mtnMobileNum.trim().length() < 10) {
                        editTextMobileMtn.setError(getString(R.string.err_not_phone));
                        validMobileNum = false;
                        editTextMobileMtn.requestFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                    }
                    if (!validMobileNum) return;
                }
                balance = editTextBalance.getText().toString();
                code = editTextCode.getText().toString();

                if (balance.equals("")) {
                    editTextBalance.setError(getString(R.string.balance_required));
                    validBalance = false;
                }
                if (!validBalance) return;
                showAlertDialog(code, mtnMobileNum);
            }
        });


        return root;
    }

    private int calculatePrice(int num) {
        editTextPrice.setText(" ");

        if (num >= 50 && num < 100) {
            double sum = num + 1;
            return (int) sum;
        } else if (num >= 100) {
            double sum = num + (num * 0.01);
            return (int) sum;
        } else if (num < 50 && num > 1) return num;
        else {
            editTextPrice.setText("");
            return 0;
        }
    }

    public void showAlertDialog(String code, String mtnMobileNum) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.save_number);
        // set the custom layout
        View customLayout = null;
        if (selectedCompanyType == 2) // mtn
            customLayout = getLayoutInflater().inflate(R.layout.save_all_mtn_user_layout, null);

        if (selectedCompanyType == 1) // syriatel
            customLayout = getLayoutInflater().inflate(R.layout.save_all_syriatel_user_layout, null);
        builder.setView(customLayout);

        // add a button
        Button btnSaveClient = customLayout.findViewById(R.id.btnSaveClient);
        final EditText editTextName = customLayout.findViewById(R.id.editTextName);
        final EditText editTextPhone = customLayout.findViewById(R.id.editTextPhone);
        final EditText editTextCode = customLayout.findViewById(R.id.editTextCode);
        editTextCode.setText(code);
        if (!mtnMobileNum.trim().equals(""))
            editTextPhone.setText(mtnMobileNum);
        btnSaveClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clientName = editTextName.getText().toString();
                if (clientName.equals("")) {
                    editTextName.setError(getString(R.string.err_client_name));
                    return;
                }

                String clientCode = editTextCode.getText().toString();
                if (clientCode.equals("")) {
                    editTextCode.setError(getString(R.string.code_required));
                    return;
                }


                UserModel newUser = new UserModel();

                if (btnByLate.isChecked()) {
                    priceStr = editTextPrice.getText().toString();
                    int price = Integer.parseInt(priceStr);
                    if (selectedCompanyType == 2)
                        newUser = new UserModel(editTextName.getText().toString(), editTextPhone.getText().toString(), price, clientCode);
                    if (selectedCompanyType == 1)
                        newUser = new UserModel(editTextName.getText().toString(), price, clientCode);
                } else {
                    if (selectedCompanyType == 2)
                        newUser = new UserModel(editTextName.getText().toString(), editTextPhone.getText().toString(), clientCode);
                    if (selectedCompanyType == 1)
                        newUser = new UserModel(editTextName.getText().toString(), clientCode);

                }
                sendDialogDataToActivity(newUser);

                dialog.dismiss();

            }
        });
        // create and show the alert dialog
        dialog = builder.create();
        dialog.show();
    }
    // do something with the data coming from the AlertDialog

    private void sendDialogDataToActivity(UserModel user) {
        try {
            UserTableOperations userTableOperations = new UserTableOperations(requireContext());
            if (!userTableOperations.checkIfMobileExists(user.mobileNum)) {
                boolean success = userTableOperations.insertUserData(user);
                Toast.makeText(requireContext(), R.string.user_saved, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(requireContext(), R.string.mobile_already_registered, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), R.string.err_somthing_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    private void addLoanMoneyAmount(int amount, String mobileNum) {
        try {
            UserTableOperations userTableOperations = new UserTableOperations(requireContext());
            if (userTableOperations.checkIfMobileExists(mobileNum)) {
                int moneyLoanBefore = userTableOperations.getLoanOfUserByMobileNum(mobileNum);
                int allMoney = moneyLoanBefore + amount;
                int result = userTableOperations.updateUserLoanMoney(allMoney, mobileNum);
                Toast.makeText(requireContext(), R.string.saved, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), R.string.err_somthing_wrong, Toast.LENGTH_SHORT).show();
        }
    }

  /*  private int convertCompanyNameToCode(String mobileNum) {
        int companyCode = 0;
        if (mobileNum.startsWith("093") || mobileNum.startsWith("098") || mobileNum.startsWith("099")) {
            // the company is syriatel, code=1
            companyCode = 1;
        }
        if (mobileNum.startsWith("095") || mobileNum.startsWith("096") || mobileNum.startsWith("094")) {
            // the company is mtn, code=2
            companyCode = 2;
        }

        return companyCode;
    }*/

    private void addAllMTNTransferDetails(int money_amount, String mobileNum, int balanceOrBill, String transfer_code) {
        try {
            final TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());
            String pattern = "dd-MM-yyyy";
            String date = new SimpleDateFormat(pattern).format(new Date());
            TransferModel transferModel = new TransferModel(mobileNum, balanceOrBill, money_amount, 2, date, 2, transfer_code, 1);

            int client_id = sharedpreferences.getInt(USER_ID, 0);
            transferModel.setClient_id(client_id);
            final long success = transferTableOperations.insertTransferData(transferModel);
            if (success != -1) {
                int allAttempts = sharedpreferences.getInt(ALL_ATTEMPTS_NUM, 0);
                List<TransferModel> transfersList;
                transfersList = transferTableOperations.getOneAndAllTransferData();
                int doneAttemptsCount = transfersList.size();
                int remainedAttemptsCount = allAttempts - doneAttemptsCount;
                editor.putInt(REMAIN_ATTEMPTS_NUM, remainedAttemptsCount);
                final int subType = sharedpreferences.getInt(USER_SUBSCRIPTION_TYPE, 0);
                if (subType == 1) {
                    if (remainedAttemptsCount > 0) {
//                Toast.makeText(requireContext(), R.string.transfer_done, Toast.LENGTH_SHORT).show();
                        TransfersApi transfersApi = new TransfersApi(requireContext());
                        transfersApi.transfer_insert(transferModel, new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                if (result.equals("ok")) {
                                    Toast.makeText(requireContext(), R.string.transfer_saved_on_server, Toast.LENGTH_SHORT).show();

                                    int up = transferTableOperations.updateIsSync(success);
                                    if (up != -1)
                                        Toast.makeText(requireContext(), R.string.syc_upated, Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                            }
                        });
                        editTextBalance.setText("");
                        editTextMobileMtn.setText("");
                        editTextPrice.setText("");
                    } else {
                        Toast.makeText(requireContext(), R.string.must_activate, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    TransfersApi transfersApi = new TransfersApi(requireContext());
                    transfersApi.transfer_insert(transferModel, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            if (result.equals("ok")) {
                                Toast.makeText(requireContext(), R.string.transfer_saved_on_server, Toast.LENGTH_SHORT).show();

                                int up = transferTableOperations.updateIsSync(success);
                                if (up != -1)
                                    Toast.makeText(requireContext(), R.string.syc_upated, Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    });
                    editTextBalance.setText("");
                    editTextMobileMtn.setText("");
                    editTextPrice.setText("");
                }
            } else
                Toast.makeText(requireContext(), getString(R.string.err_somthing_wrong), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(requireContext(), getString(R.string.err_somthing_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void addAllSyriatelTransferDetails(int money_amount, int balanceOrBill, String transfer_code) {
        try {
            final TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());
            String pattern = "dd-MM-yyyy";
            String date = new SimpleDateFormat(pattern).format(new Date());
            TransferModel transferModel = new TransferModel("", balanceOrBill, money_amount, 1, date, 2, transfer_code, 1);
            final long success = transferTableOperations.insertTransferData(transferModel);
            if (success != -1) {
                int allAttempts = sharedpreferences.getInt(ALL_ATTEMPTS_NUM, 0);
                List<TransferModel> transfersList;
                transfersList = transferTableOperations.getOneAndAllTransferData();
                int doneAttemptsCount = transfersList.size();
                int remainedAttemptsCount = allAttempts - doneAttemptsCount;
                editor.putInt(REMAIN_ATTEMPTS_NUM, remainedAttemptsCount);

                final int subType = sharedpreferences.getInt(USER_SUBSCRIPTION_TYPE, 0);
                if (subType == 1) {
                    if (remainedAttemptsCount > 0) {
                        Toast.makeText(requireContext(), R.string.transfer_done, Toast.LENGTH_SHORT).show();
                        TransfersApi transfersApi = new TransfersApi(requireContext());
                        transfersApi.transfer_insert(transferModel, new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                if (result.equals("ok")) {
                                    Toast.makeText(requireContext(), R.string.transfer_saved_on_server, Toast.LENGTH_SHORT).show();

                                    int up = transferTableOperations.updateIsSync(success);
                                    if (up != -1)
                                        Toast.makeText(requireContext(), R.string.syc_upated, Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                            }
                        });
                        editTextBalance.setText("");
                        editTextMobileMtn.setText("");
                        editTextPrice.setText("");
                    } else {
                        Toast.makeText(requireContext(), R.string.must_activate, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.transfer_done, Toast.LENGTH_SHORT).show();
                    TransfersApi transfersApi = new TransfersApi(requireContext());
                    transfersApi.transfer_insert(transferModel, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            if (result.equals("ok")) {
                                Toast.makeText(requireContext(), R.string.transfer_saved_on_server, Toast.LENGTH_SHORT).show();

                                int up = transferTableOperations.updateIsSync(success);
                                if (up != -1)
                                    Toast.makeText(requireContext(), R.string.syc_upated, Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    });
                    editTextBalance.setText("");
                    editTextMobileMtn.setText("");
                    editTextPrice.setText("");
                }
            } else
                Toast.makeText(requireContext(), getString(R.string.err_somthing_wrong), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(requireContext(), getString(R.string.err_somthing_wrong), Toast.LENGTH_SHORT).show();
        }
    }
}