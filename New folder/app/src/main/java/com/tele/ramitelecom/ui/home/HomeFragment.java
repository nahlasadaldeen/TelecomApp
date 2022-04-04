package com.tele.ramitelecom.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.tele.ramitelecom.R;
import com.tele.ramitelecom.api_connection.TransfersApi;
import com.tele.ramitelecom.api_connection.UnitsCallback;
import com.tele.ramitelecom.api_connection.UserApi;
import com.tele.ramitelecom.api_connection.UserCallback;
import com.tele.ramitelecom.api_connection.VolleyCallback;
import com.tele.ramitelecom.db.TransferTableOperations;
import com.tele.ramitelecom.db.UnitPriceTableOperations;
import com.tele.ramitelecom.db.UserTableOperations;
import com.tele.ramitelecom.ui.helper.TransferCode;
import com.tele.ramitelecom.ui.users.TransferModel;
import com.tele.ramitelecom.ui.users.UnitPriceModel;
import com.tele.ramitelecom.ui.users.UserDelegate;
import com.tele.ramitelecom.ui.users.UserModel;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.tele.ramitelecom.ui.helper.Constants.ALL_ATTEMPTS_NUM;
import static com.tele.ramitelecom.ui.helper.Constants.CENTER_ID;
import static com.tele.ramitelecom.ui.helper.Constants.MyPREFERENCES;
import static com.tele.ramitelecom.ui.helper.Constants.REMAIN_ATTEMPTS_NUM;
import static com.tele.ramitelecom.ui.helper.Constants.USER_ID;
import static com.tele.ramitelecom.ui.helper.Constants.USER_SUBSCRIPTION_TYPE;
import static com.tele.ramitelecom.ui.helper.Constants.syriatel_distributor_code;
import static com.tele.ramitelecom.ui.helper.Constants.syriatel_sim_code;

public class HomeFragment extends Fragment {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    TextView lblStillAttemptsCount, txtStillAttemptsCount, lblAllAttemptsCount, txtAllAttemptsCount, lblMustEnable;
    //    TextView txtBalance;
    //    ProgressDialog nDialog;
    ProgressBar mProgressBar;
    Button btnFixedEnable, btnTempEnable, btnGetBalanceForClient, btnShowCenterDetails;
    private HomeViewModel homeViewModel;
    AlertDialog dialog;
    MaterialCheckBox btnByLate;
    EditText editTextPhone, editTextPrice, editTextBalance;
    boolean validMobileNum = true, validBalance = true;
    UserModel userSelected = null;
    MaterialButton btnTransferBalance, btnTransferBills, btnReCall;
    ImageView imgCompany;
    String priceStr = "";
    String mobileNum = "";
    String balance = "";
    HashMap<Integer, Integer> allPrices = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle b = getArguments();
        sharedpreferences = requireContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        final TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());

        if (b != null && !b.isEmpty()) {
            userSelected = HomeFragmentArgs.fromBundle(getArguments()).getUser();
        }
        mProgressBar = root.findViewById(R.id.progress_bar);

        BottomNavigationView bottomNavigationView = root.findViewById(R.id.bottomNavigationView);

        final BottomNavigationView bottomNavigationViewValues = root.findViewById(R.id.bottomNavigationViewValues);

        final int[] selected_btn = {0};
        /*adjustGravity(bottomNavigationView);
        adjustWidth(bottomNavigationView);*/
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                sharedpreferences = requireContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String syriatel_sim_code_value = sharedpreferences.getString(syriatel_sim_code, "");
                String syriatel_distributor_code_value = sharedpreferences.getString(syriatel_distributor_code, "");

                String select_code = "";
                switch (item.getItemId()) {
                    case R.id.mtn_ones:
                        select_code = "*155*1#";
                        selected_btn[0] = 1;
                        break;
                    case R.id.mtn_bills:
                        select_code = "*155*1#";
                        selected_btn[0] = 2;
                        break;
                    case R.id.syriatel_ones:
                        select_code = "*150*2*m*c*1#";// "*150*1*m*c*1#";
                        selected_btn[0] = 3;
                        break;
                    case R.id.syriatel_bills:
                        select_code = "*150*1*m*c*2#";
                        selected_btn[0] = 4;
                        break;
                }
                Intent phone_intent = new Intent(Intent.ACTION_CALL);

                select_code = select_code.replace("m", syriatel_distributor_code_value);
                select_code = select_code.replace("c", syriatel_sim_code_value);
                phone_intent.setData(Uri.parse("tel:" + Uri.encode(select_code)));
                // start Intent
                startActivity(phone_intent);
                String myBalance = sharedpreferences.getString("my_balance", "");

                if (selected_btn[0] == 1) {
                    bottomNavigationViewValues.getMenu().getItem(0).setTitle(myBalance);
                }
                if (selected_btn[0] == 2) {
                    bottomNavigationViewValues.getMenu().getItem(1).setTitle(myBalance);
                }
                if (selected_btn[0] == 3) {
                    bottomNavigationViewValues.getMenu().getItem(2).setTitle(myBalance);
                }
                if (selected_btn[0] == 4) {
                    bottomNavigationViewValues.getMenu().getItem(3).setTitle(myBalance);

                }
                return true;
            }
        });

        imgCompany = root.findViewById(R.id.imgCompany);

        editTextPhone = root.findViewById(R.id.editTextPhone);
        editTextBalance = root.findViewById(R.id.editTextBalance);
        editTextPrice = root.findViewById(R.id.editTextPrice);

        ImageView btnUsers = root.findViewById(R.id.btnUsers);
        if (userSelected != null)
            editTextPhone.setText(userSelected.mobileNum);
        btnUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(requireParentFragment()).navigate(
                        HomeFragmentDirections.actionNavUsersSettings(true, new UserDelegate() {
                            @Override
                            public void getUser(UserModel user) {
                                /*  editTextPhone.setText(user.mobileNum);*/
                            }
                        }, "1"));
            }
        });

        btnGetBalanceForClient = root.findViewById(R.id.btnSelectBalance);
        btnGetBalanceForClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(requireParentFragment()).navigate(
                        HomeFragmentDirections.actionNavHomeToNavSelectForClient());
            }
        });

       /* Intent phone_intent = new Intent(Intent.ACTION_CALL);
        phone_intent.setData(Uri.parse("tel:" + Uri.encode("*100#")));
        // start Intent
        startActivity(phone_intent);*/

       /* String myBalance = sharedpreferences.getString("my_balance", "");

        txtBalance = root.findViewById(R.id.txtBalance);
        txtBalance.setText(myBalance);*/


        int is_center = sharedpreferences.getInt(CENTER_ID, 0);
        btnShowCenterDetails = root.findViewById(R.id.btnShowCenterDetails);

        if (is_center > 0) {
            btnShowCenterDetails.setVisibility(View.VISIBLE);
            btnShowCenterDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavHostFragment.findNavController(requireParentFragment()).navigate(
                            HomeFragmentDirections.actionNavHomeToNavCenter());
                }
            });
        }

        Button btnLog = root.findViewById(R.id.btnLog);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(requireParentFragment()).navigate(
                        HomeFragmentDirections.actionNavHomeToNavTransfersLog(null));
            }
        });
        btnByLate = root.findViewById(R.id.btnByLate);

        btnReCall = root.findViewById(R.id.btnReCall);
        btnReCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextPrice.setText(priceStr);
                editTextPhone.setText(mobileNum);
                editTextBalance.setText(balance);
            }
        });

        btnTransferBalance = root.findViewById(R.id.btnTransferBalance);
        btnTransferBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // insert transfer details in transfer table
                priceStr = editTextPrice.getText().toString();
                mobileNum = editTextPhone.getText().toString();
                balance = editTextBalance.getText().toString();

                if (priceStr.trim().equals("")) {
                    editTextPrice.setError(getString(R.string.err_price_required));
                    return;
                }
                if (mobileNum.trim().equals("")) {
                    editTextPhone.setError(getString(R.string.mobile_num_required));
                    return;
                }
                if (balance.trim().equals("")) {
                    editTextBalance.setError(getString(R.string.balance_required));
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

                        mProgressBar.setVisibility(View.VISIBLE);
                        int price = Integer.parseInt(priceStr);
                        int balanceVal = Integer.parseInt(balance);

                        // transfer balance code here
                        // Getting instance of Intent
                        // with action as ACTION_CALL-
                        Intent phone_intent = new Intent(Intent.ACTION_CALL);

                        int comp = convertCompanyNameToCode(mobileNum);
                        if (comp == 1) {//syriatel, code=1
                            String oneBalanceSyriatelCode = new TransferCode(requireContext()).getOneBalanceSyriatelTransferCode();
                            Log.e("oneBalanceSyriatelCode", oneBalanceSyriatelCode);
                            String syriatelCodeForTransfer = oneBalanceSyriatelCode.replace("m", balanceVal + "");
                            String syriatelCodeForTransferFinal = syriatelCodeForTransfer.replaceAll("p", mobileNum);
                            Log.e("syriatelCodeForTransfer", syriatelCodeForTransferFinal);

                            phone_intent.setData(Uri.parse("tel:" + Uri.encode(syriatelCodeForTransferFinal)));
                            // start Intent
                            startActivity(phone_intent);
                        }
                        if (comp == 2) {//mtn, code=2
                            String oneBalanceMTnCode = new TransferCode(requireContext()).getOneBalanceMtnTransferCode();
                            Log.e("oneBalanceMTnCode", oneBalanceMTnCode);
                            String mtnCodeForTransfer = oneBalanceMTnCode.replace("p", mobileNum);
                            String mtnCodeForTransferFinal = mtnCodeForTransfer.replaceAll("m", balance + "");
                            Log.e("mtnCodeForTransfer", mtnCodeForTransferFinal);

                            phone_intent.setData(Uri.parse("tel:" + Uri.encode(mtnCodeForTransferFinal)));
                            // start Intent
                            startActivity(phone_intent);

                        }


                        boolean res = addTransferDetails(price, mobileNum, comp, 1);


                        if (res) {
                            if (btnByLate.isChecked()) {
                                // save paying by late
                    /*String priceStr = editTextPrice.getText().toString();
                    String mobileNum = editTextPhone.getText().toString();
                    int price = Integer.parseInt(priceStr);*/
                                // update user record in db to set loan money amount
                                addLoanMoneyAmount(price, mobileNum);
                            } else {
                            }
                            Toast.makeText(requireContext(), R.string.transfer_done, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.err_somthing_wrong), Toast.LENGTH_SHORT).show();
                        }
                        mProgressBar.setVisibility(View.GONE);

//                nDialog.dismiss();
                    } else {
                        Toast.makeText(requireContext(), R.string.must_activate, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    int price = Integer.parseInt(priceStr);
                    int balanceVal = Integer.parseInt(balance);

                    // transfer balance code here
                    // Getting instance of Intent
                    // with action as ACTION_CALL-
                    Intent phone_intent = new Intent(Intent.ACTION_CALL);

                    int comp = convertCompanyNameToCode(mobileNum);
                    if (comp == 1) {//syriatel, code=1
                        String oneBalanceSyriatelCode = new TransferCode(requireContext()).getOneBalanceSyriatelTransferCode();
                        Log.e("oneBalanceSyriatelCode", oneBalanceSyriatelCode);
                        String syriatelCodeForTransfer = oneBalanceSyriatelCode.replace("m", balanceVal + "");
                        String syriatelCodeForTransferFinal = syriatelCodeForTransfer.replaceAll("p", mobileNum);
                        Log.e("syriatelCodeForTransfer", syriatelCodeForTransferFinal);

                        phone_intent.setData(Uri.parse("tel:" + Uri.encode(syriatelCodeForTransferFinal)));
                        // start Intent
                        startActivity(phone_intent);
                    }
                    if (comp == 2) {//mtn, code=2
                        String oneBalanceMTnCode = new TransferCode(requireContext()).getOneBalanceMtnTransferCode();
                        Log.e("oneBalanceMTnCode", oneBalanceMTnCode);
                        String mtnCodeForTransfer = oneBalanceMTnCode.replace("p", mobileNum);
                        String mtnCodeForTransferFinal = mtnCodeForTransfer.replaceAll("m", balance + "");
                        Log.e("mtnCodeForTransfer", mtnCodeForTransferFinal);

                        phone_intent.setData(Uri.parse("tel:" + Uri.encode(mtnCodeForTransferFinal)));
                        // start Intent
                        startActivity(phone_intent);

                    }


                    boolean res = addTransferDetails(price, mobileNum, comp, 1);


                    if (res) {
                        if (btnByLate.isChecked()) {
                            // save paying by late
                    /*String priceStr = editTextPrice.getText().toString();
                    String mobileNum = editTextPhone.getText().toString();
                    int price = Integer.parseInt(priceStr);*/
                            // update user record in db to set loan money amount
                            addLoanMoneyAmount(price, mobileNum);
                        } else {
                        }
                        Toast.makeText(requireContext(), R.string.transfer_done, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.err_somthing_wrong), Toast.LENGTH_SHORT).show();
                    }
                    mProgressBar.setVisibility(View.GONE);
                }

            }
        });
        btnTransferBills = root.findViewById(R.id.btnTransferBills);
        btnTransferBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // insert transfer details in transfer table
                priceStr = editTextPrice.getText().toString();
                mobileNum = editTextPhone.getText().toString();
                balance = editTextBalance.getText().toString();

                if (priceStr.trim().equals("")) {
                    editTextPrice.setError(getString(R.string.err_price_required));
                    return;
                }
                if (mobileNum.trim().equals("")) {
                    editTextPhone.setError(getString(R.string.mobile_num_required));
                    return;
                }
                if (balance.trim().equals("")) {
                    editTextBalance.setError(getString(R.string.balance_required));
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
                        mProgressBar.setVisibility(View.VISIBLE);

                        Intent phone_intent = new Intent(Intent.ACTION_CALL);
                        //ToDO transfer bills code here

                        int price = Integer.parseInt(priceStr);
                        int comp = convertCompanyNameToCode(mobileNum);
                        if (comp == 1) {//syriatel, code=1
                            String oneBillSyriatelCode = new TransferCode(requireContext()).getOneBillSyriatelTransferCode();
                            Log.e("oneBillSyriatelCode", oneBillSyriatelCode);
                            String syriatelCodeForTransfer = oneBillSyriatelCode.replace("m", balance + "");
                            String syriatelCodeForTransferFinal = syriatelCodeForTransfer.replaceAll("p", mobileNum);
                            Log.e("syriatelCodeForBill", syriatelCodeForTransferFinal);

                            phone_intent.setData(Uri.parse("tel:" + Uri.encode(syriatelCodeForTransferFinal)));
                            // start Intent
                            startActivity(phone_intent);
                        }
                        if (comp == 2) {//mtn, code=2
                            String oneBillMTnCode = new TransferCode(requireContext()).getOneBillMtnTransferCode();
                            Log.e("oneBillMTnCode", oneBillMTnCode);
                            String mtnCodeForTransfer = oneBillMTnCode.replace("p", mobileNum);
                            String mtnCodeForTransferFinal = mtnCodeForTransfer.replaceAll("m", balance + "");
                            Log.e("mtnCodeForBill", mtnCodeForTransferFinal);

                            phone_intent.setData(Uri.parse("tel:" + Uri.encode(mtnCodeForTransferFinal)));
                            // start Intent
                            startActivity(phone_intent);

                        }

                        boolean res = addTransferDetails(price, mobileNum, comp, 2);
                        if (res) {
                            if (btnByLate.isChecked()) {
                                // save paying by late
                    /*String priceStr = editTextPrice.getText().toString();
                    String mobileNum = editTextPhone.getText().toString();
                    int price = Integer.parseInt(priceStr);*/
                                // update user record in db to set loan money amount
                                addLoanMoneyAmount(price, mobileNum);
                            } else {
                            }
                            Toast.makeText(requireContext(), R.string.transfer_done, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.err_somthing_wrong), Toast.LENGTH_SHORT).show();
                        }
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(requireContext(), R.string.must_activate, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);

                    Intent phone_intent = new Intent(Intent.ACTION_CALL);
                    //ToDO transfer bills code here

                    int price = Integer.parseInt(priceStr);
                    int comp = convertCompanyNameToCode(mobileNum);
                    if (comp == 1) {//syriatel, code=1
                        String oneBillSyriatelCode = new TransferCode(requireContext()).getOneBillSyriatelTransferCode();
                        Log.e("oneBillSyriatelCode", oneBillSyriatelCode);
                        String syriatelCodeForTransfer = oneBillSyriatelCode.replace("m", balance + "");
                        String syriatelCodeForTransferFinal = syriatelCodeForTransfer.replaceAll("p", mobileNum);
                        Log.e("syriatelCodeForBill", syriatelCodeForTransferFinal);

                        phone_intent.setData(Uri.parse("tel:" + Uri.encode(syriatelCodeForTransferFinal)));
                        // start Intent
                        startActivity(phone_intent);
                    }
                    if (comp == 2) {//mtn, code=2
                        String oneBillMTnCode = new TransferCode(requireContext()).getOneBillMtnTransferCode();
                        Log.e("oneBillMTnCode", oneBillMTnCode);
                        String mtnCodeForTransfer = oneBillMTnCode.replace("p", mobileNum);
                        String mtnCodeForTransferFinal = mtnCodeForTransfer.replaceAll("m", balance + "");
                        Log.e("mtnCodeForBill", mtnCodeForTransferFinal);

                        phone_intent.setData(Uri.parse("tel:" + Uri.encode(mtnCodeForTransferFinal)));
                        // start Intent
                        startActivity(phone_intent);

                    }

                    boolean res = addTransferDetails(price, mobileNum, comp, 2);
                    if (res) {
                        if (btnByLate.isChecked()) {
                            // save paying by late
                    /*String priceStr = editTextPrice.getText().toString();
                    String mobileNum = editTextPhone.getText().toString();
                    int price = Integer.parseInt(priceStr);*/
                            // update user record in db to set loan money amount
                            addLoanMoneyAmount(price, mobileNum);
                        } else {
                        }
                        Toast.makeText(requireContext(), R.string.transfer_done, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.err_somthing_wrong), Toast.LENGTH_SHORT).show();
                    }
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        final String[] lastChar = {" "};

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = editTextPhone.getText().toString().length();
                if (digits > 1)
                    lastChar[0] = editTextPhone.getText().toString().substring(digits - 1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = editTextPhone.getText().toString().length();
                Log.e("LENGTH", "" + digits);
                if (digits == 1) {
                    imgCompany.setVisibility(View.INVISIBLE);

                    String num = s.toString();
                    int numm = Integer.parseInt(num);
                    if (numm != 0) {
                        validMobileNum = false;
                        editTextPhone.setError(getString(R.string.err_not_phone));
                    }
                }
                if (digits == 2) {
                    imgCompany.setVisibility(View.INVISIBLE);
                    String num = s.toString();
                    int numm = Integer.parseInt(num);
                    if (numm != 9) {
                        validMobileNum = false;
                        editTextPhone.setError(getString(R.string.err_not_phone));
                    }
                }

                if (digits == 3) {
                    String num = s.toString();
                    int numm = Integer.parseInt(num);

                    if (numm == 93 || numm == 98 || numm == 99) {
                        // the number is syriatel
                        imgCompany.setVisibility(View.VISIBLE);
                        imgCompany.setImageResource(R.drawable.syriatel);
                    }
                    if (numm == 96 || numm == 95 || numm == 94) {
                        // the number is mtn
                        imgCompany.setVisibility(View.VISIBLE);
                        imgCompany.setImageResource(R.drawable.mtn);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = editTextBalance.getText().toString().length();
                if (digits > 1)
                    lastChar[0] = editTextBalance.getText().toString().substring(digits - 1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UnitPriceTableOperations unitPriceTableOperations = new UnitPriceTableOperations(requireContext());
                ArrayList<UnitPriceModel> res = unitPriceTableOperations.getAllUnitsData();
                if (res.size() < 1) {
                    Toast.makeText(requireContext(), getString(R.string.you_must_sync), Toast.LENGTH_LONG).show();
                    return;
                }
                int digits = editTextBalance.getText().toString().length();
                Log.d("LENGTH", "" + digits);
                if (digits == 1) {
                    String num = s.toString();
                    int numm = Integer.parseInt(num);
                    if (numm < 1) {
                        validBalance = false;
                        editTextBalance.setError(getString(R.string.err_balance));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = s.toString();
                if (!num.equals("")) {
                    int numm = Integer.parseInt(num);
                    if (numm < 1) {
                        validBalance = false;
                        editTextBalance.setError(getString(R.string.err_balance));
                    }
                    calculatePrice(numm);
                }
            }
        });

        Button btnSaveNum = root.findViewById(R.id.btnSaveNum);
        btnSaveNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validMobileNum = true;
                validBalance = true;
                mobileNum = editTextPhone.getText().toString();
                balance = editTextBalance.getText().toString();
                if (mobileNum.equals("")) {
                    editTextPhone.setError(getString(R.string.mobile_num_required));
                    validMobileNum = false;
                    editTextPhone.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                }
                if (mobileNum.length() < 10) {
                    editTextPhone.setError(getString(R.string.err_not_phone));
                    validMobileNum = false;
                    editTextPhone.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                }
                if (balance.equals("")) {
                    editTextBalance.setError(getString(R.string.balance_required));
                    validBalance = false;
                }
                if (!validBalance || !validMobileNum) return;
                showAlertDialog();
            }
        });

        txtAllAttemptsCount = root.findViewById(R.id.txtAllAttemptsCount);

        btnTempEnable = root.findViewById(R.id.btnRequestAttempts);
        btnTempEnable.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // request enable code for attempts num
                        showTempEnableDialog();
                    }
                }
        );
        int allAttempts = sharedpreferences.getInt(ALL_ATTEMPTS_NUM, 0);
        txtAllAttemptsCount.setText(allAttempts + "");

        txtStillAttemptsCount = root.findViewById(R.id.txtStillAttemptsCount);
        lblAllAttemptsCount = root.findViewById(R.id.lblAllAttemptsCount);
        lblStillAttemptsCount = root.findViewById(R.id.lblStillAttemptsCount);

        lblMustEnable = root.findViewById(R.id.lblMustEnable);

        List<TransferModel> transfersList;
        transfersList = transferTableOperations.getOneAndAllTransferData();

        int doneAttemptsCount = transfersList.size();

        int remainedAttemptsCount = allAttempts - doneAttemptsCount;

        txtStillAttemptsCount.setText(remainedAttemptsCount + "");
        //remainedAttemptsCount = 0;
        if (remainedAttemptsCount < 1) {
            txtStillAttemptsCount.setText("0 ");
            lblMustEnable.setVisibility(View.VISIBLE);
//            txtStillAttemptsCount.setVisibility(View.GONE);
            btnTempEnable.setVisibility(View.VISIBLE);
        }

        btnFixedEnable = root.findViewById(R.id.btnFixedEnable);

        btnFixedEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send request to fixed enable to server
                showFixEnableDialog();

                /*// test login
                UserApi userApi = new UserApi(requireContext());
                userApi.user_login("sami", "", new UserCallback() {
                    @Override
                    public void onSuccess(UserModel user) {
                        Log.e("oooooooooooooo", "user ...." + user.name);
                    }
                });*/
            }
        });

        final int subType = sharedpreferences.getInt(USER_SUBSCRIPTION_TYPE, 0);
        if (subType == 2) {
            btnFixedEnable.setVisibility(View.GONE);
            btnTempEnable.setVisibility(View.GONE);
            lblAllAttemptsCount.setVisibility(View.GONE);
            lblStillAttemptsCount.setVisibility(View.GONE);
            lblMustEnable.setVisibility(View.GONE);
            txtAllAttemptsCount.setVisibility(View.GONE);
            txtStillAttemptsCount.setVisibility(View.GONE);
        }

        Button btnSync = root.findViewById(R.id.btnSync);
        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(requireContext(), getString(R.string.err_no_internet), Toast.LENGTH_SHORT).show();
                    return;
                }
                TransfersApi transfersApi = new TransfersApi(requireContext());
                transfersApi.getUnitsPrice(new UnitsCallback() {
                    @Override
                    public void onSuccess(ArrayList<UnitPriceModel> allPrices) {
                        UnitPriceTableOperations unitPriceTableOperations = new UnitPriceTableOperations(requireContext());
                        boolean res = unitPriceTableOperations.deleteAllUnitData();
                        for (int i = 0; i < allPrices.size(); i++) {
                            unitPriceTableOperations.insertCenterData(allPrices.get(i));
                        }
                    }

                    @Override
                    public void onFail(int errorCode) {

                    }
                });

                List<TransferModel> transfersList;
                transfersList = transferTableOperations.getOneAndAllTransferData();
                int doneAttemptsCount = transfersList.size();
                int allAttempts = sharedpreferences.getInt(ALL_ATTEMPTS_NUM, 0);

                int remainedAttemptsCount = allAttempts - doneAttemptsCount;
                txtStillAttemptsCount.setText(remainedAttemptsCount + "");
                int user_id = sharedpreferences.getInt(USER_ID, 0);

                UserApi userApi = new UserApi(requireContext());
                if (subType == 1) {
                    if (remainedAttemptsCount < 1) {
                        txtStillAttemptsCount.setText("0 ");
                        lblMustEnable.setVisibility(View.VISIBLE);

                        btnTempEnable.setVisibility(View.VISIBLE);
                        userApi.update_user_attempts_num(user_id, remainedAttemptsCount, new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                if (result.equals("ok")) {
                                    Toast.makeText(requireContext(), "attempts updated", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(requireContext(), R.string.err_somthing_wrong, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

                // get non syned records from transfer table

                final List<TransferModel> unSyncTransfers = transferTableOperations.getAllUnSyncTransferData();

//                Toast.makeText(requireContext(), unSyncTransfers.size() + "", Toast.LENGTH_SHORT).show();

                if (!unSyncTransfers.isEmpty()) {

                    // send all to server
                    for (int i = 0; i < unSyncTransfers.size(); i++) {
                        final int finalI = i;
                        transfersApi.transfer_insert(unSyncTransfers.get(i), new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                if (result.equals("ok")) {
                                    Toast.makeText(requireContext(), R.string.transfer_saved_on_server, Toast.LENGTH_SHORT).show();

                                    int up = transferTableOperations.updateIsSync(unSyncTransfers.get(finalI).id);
                                    if (up != -1)
                                        Toast.makeText(requireContext(), R.string.syc_upated, Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(requireContext(), R.string.err_somthing_wrong, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        return root;
    }

    private void calculatePrice(int numm) {

        UnitPriceTableOperations unitPriceTableOperations = new UnitPriceTableOperations(requireContext());
        ArrayList<UnitPriceModel> res = unitPriceTableOperations.getAllUnitsData();
        for (int i = 0; i < res.size(); i++) {
            UnitPriceModel unitPriceModel = res.get(i);
            allPrices.put(unitPriceModel.unit_class, unitPriceModel.unit_price);
        }
        Integer priceOfUnits = allPrices.get(numm);
        if (priceOfUnits != null)
            editTextPrice.setText(priceOfUnits + "");
        else
            editTextPrice.setText("");
       /* switch (num) {
            case 50: {
                editTextPrice.setText("75");
                break;
            }
            case 75: {
                editTextPrice.setText("100");
                break;
            }
            case 90:
                editTextPrice.setText("100");
                break;
            case 100:
            case 105:
                editTextPrice.setText("125");
                break;
            case 150:
                editTextPrice.setText("175");
                break;
            case 200:
                editTextPrice.setText("225");
                break;
            case 225:
                editTextPrice.setText("250");
                break;
            case 250:
                editTextPrice.setText("300");
                break;
            case 300:
                editTextPrice.setText("350");
                break;
            case 450:
                editTextPrice.setText("500");
                break;
            case 500:
                editTextPrice.setText("550");
                break;
            case 650:
                editTextPrice.setText("750");
                break;
            case 750:
                editTextPrice.setText("850");
                break;
            case 900:
                editTextPrice.setText("1000");
                break;
            case 1000:
                editTextPrice.setText("1100");
                break;
            case 1200:
                editTextPrice.setText("1350");
                break;
            case 1500:
                editTextPrice.setText("1650");
                break;
            case 2000:
                editTextPrice.setText("2200");
                break;
            case 4000:
                editTextPrice.setText("4400");
                break;
            case 5000:
                editTextPrice.setText("5500");
                break;
            case 10000:
                editTextPrice.setText("11000");
                break;
            case 14381:
                editTextPrice.setText("15500");
                break;
            case 15000:
                editTextPrice.setText("16500");
                break;
            case 20000:
                editTextPrice.setText("22000");
                break;
            default:
                editTextPrice.setText("");
                break;
        }*/
    }

    public void showFixEnableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.fixed_enable);
        final View customLayout = getLayoutInflater().inflate(R.layout.fix_enable_layout, null);
        builder.setView(customLayout);

        // add a button
        final Button btnSaveEnable = customLayout.findViewById(R.id.btnSaveEnable);
        final EditText editTextFixEnable = customLayout.findViewById(R.id.editTextFixEnable);
        btnSaveEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fixEnable = editTextFixEnable.getText().toString();
                if (fixEnable.equals("")) {
                    editTextFixEnable.setError(getString(R.string.required));
                    return;
                }


                int user_id = sharedpreferences.getInt(USER_ID, 0);
                UserApi userApi = new UserApi(requireContext());
                userApi.user_fixed_enable(user_id, fixEnable, new VolleyCallback() {

                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("ok")) {
                            Toast.makeText(requireContext(), getString(R.string.enabled_ok), Toast.LENGTH_SHORT).show();
                            btnFixedEnable.setVisibility(View.GONE);
                            btnTempEnable.setVisibility(View.GONE);
                            lblAllAttemptsCount.setVisibility(View.GONE);
                            lblStillAttemptsCount.setVisibility(View.GONE);
                            lblMustEnable.setVisibility(View.GONE);
                            txtAllAttemptsCount.setVisibility(View.GONE);
                            txtStillAttemptsCount.setVisibility(View.GONE);
                        } else
                            Toast.makeText(requireContext(), getString(R.string.err_enable), Toast.LENGTH_SHORT).show();

                    }
                });


                dialog.dismiss();

            }
        });
        // create and show the alert dialog
        dialog = builder.create();
        dialog.show();
    }

    public void showTempEnableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.temp_enable);
        final View customLayout = getLayoutInflater().inflate(R.layout.temp_enable_layout, null);
        builder.setView(customLayout);

        // add a button
        final Button btnSaveEnable = customLayout.findViewById(R.id.btnSaveEnable);
        final EditText editTextTempEnable = customLayout.findViewById(R.id.editTextTempEnable);
        btnSaveEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempEnable = editTextTempEnable.getText().toString();
                if (tempEnable.equals("")) {
                    editTextTempEnable.setError(getString(R.string.required));
                    return;
                }

                UserApi userApi = new UserApi(requireContext());
                int user_id = sharedpreferences.getInt(USER_ID, 0);
                userApi.user_temp_enable(user_id, tempEnable, new UserCallback() {
                            @Override
                            public void onSuccess(UserModel user) {
                                saveUserData(user);
                                Toast.makeText(requireContext(), getString(R.string.enabled_ok), Toast.LENGTH_SHORT).show();
                                btnTempEnable.setVisibility(View.GONE);
                                lblMustEnable.setVisibility(View.GONE);
                                txtAllAttemptsCount.setText(user.attemptsNum + "");
                                txtStillAttemptsCount.setText(user.attemptsNum + "");
                            }

                            @Override
                            public void onFail(int errorCode) {
                                Toast.makeText(requireContext(), getString(R.string.err_enable), Toast.LENGTH_SHORT).show();

                            }
                        }
                        /* new VolleyCallback() {

                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("ok")) {
                            Toast.makeText(requireContext(), getString(R.string.enabled_ok), Toast.LENGTH_SHORT).show();
                            btnTempEnable.setVisibility(View.GONE);
                        } else
                            Toast.makeText(requireContext(), getString(R.string.err_enable), Toast.LENGTH_SHORT).show();

                    }
                }*/);


                dialog.dismiss();

            }
        });
        // create and show the alert dialog
        dialog = builder.create();
        dialog.show();
    }

    public void showAlertDialog() {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.save_number);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.save_number_layout, null);
        builder.setView(customLayout);

        // add a button
        Button btnSaveClient = customLayout.findViewById(R.id.btnSaveClient);
        final EditText editTextName = customLayout.findViewById(R.id.editTextName);
        final EditText editTextPhone = customLayout.findViewById(R.id.editTextPhone);
        editTextPhone.setText(mobileNum);
        btnSaveClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clientName = editTextName.getText().toString();
                if (clientName.equals("")) {
                    editTextName.setError(getString(R.string.err_client_name));
                    return;
                }
                UserModel newUser;

                if (btnByLate.isChecked()) {
                    String priceStr = editTextPrice.getText().toString();
                    int price = Integer.parseInt(priceStr);
                    newUser = new UserModel(editTextName.getText().toString(), editTextPhone.getText().toString(), price, false);
                } else {
                    newUser = new UserModel(editTextName.getText().toString(), editTextPhone.getText().toString(), false);
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

                if (success) {
                    Toast.makeText(requireContext(), R.string.user_saved, Toast.LENGTH_SHORT).show();

                   /* UserApi userApi = new UserApi(requireContext());
                    userApi.user_insert(user, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            if (result.equals("ok"))
                                Toast.makeText(requireContext(), R.string.user_saved_on_server, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    });*/
                } else
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(requireContext(), R.string.mobile_already_registered, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private int convertCompanyNameToCode(String mobileNum) {
        int companyCode = 0;
        if (mobileNum.startsWith("093") || mobileNum.startsWith("098") || mobileNum.startsWith("099")) {
            // the company is syriatel, code=1
            companyCode = 1;
        }
        if (mobileNum.startsWith("096") || mobileNum.startsWith("095") || mobileNum.startsWith("094")) {
            // the company is mtn, code=2
            companyCode = 2;
        }

        return companyCode;
    }

    private boolean addTransferDetails(int money_amount, String mobileNum, int company, int balanceOrBill) {
        try {
            final TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());
            String pattern = "dd-MM-yyyy";
            String date = new SimpleDateFormat(pattern).format(new Date());
            final TransferModel transferModel = new TransferModel(mobileNum, balanceOrBill, money_amount, company, date, 1, "", 1);
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
                txtStillAttemptsCount.setText(remainedAttemptsCount + "");
                final int subType = sharedpreferences.getInt(USER_SUBSCRIPTION_TYPE, 0);
                if (subType == 1) {
                    if (remainedAttemptsCount < 1) {
                        txtStillAttemptsCount.setText("0 ");
                        btnTempEnable.setVisibility(View.VISIBLE);
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
                        editTextPhone.setText("");
                        editTextPrice.setText("");
                        return true;
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
                    editTextPhone.setText("");
                    editTextPrice.setText("");
                    return true;
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean saveUserData(UserModel user) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(ALL_ATTEMPTS_NUM, user.attemptsNum);
        editor.putInt(REMAIN_ATTEMPTS_NUM, user.attemptsNum);
        editor.apply();
        return true;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static void adjustWidth(BottomNavigationView nav) {
        try {
            Field menuViewField = nav.getClass().getDeclaredField("mMenuView");
            menuViewField.setAccessible(true);
            Object menuView = menuViewField.get(nav);

            Field itemWidth = menuView.getClass().getDeclaredField("mActiveItemMaxWidth");
            itemWidth.setAccessible(true);
            itemWidth.setInt(menuView, Integer.MAX_VALUE);
        } catch (NoSuchFieldException e) {
            // TODO
        } catch (IllegalAccessException e) {
            // TODO
        }
    }

    private static void adjustGravity(View v) {
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;

            for (int i = 0; i < vg.getChildCount(); i++) {
                adjustGravity(vg.getChildAt(i));
            }
        }
    }
}