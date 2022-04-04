package com.tele.ramitelecom.ui.users_loan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tele.ramitelecom.R;
import com.tele.ramitelecom.db.UserTableOperations;
import com.tele.ramitelecom.ui.users.UserDelegate;
import com.tele.ramitelecom.ui.users.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UsersLoanFragment extends Fragment {

    RecyclerView recyclerView;
    UserLoanItemAdapter adapter;
    List<UserModel> usersList;
    private UsersLoanViewModel usersLoanViewModel;
    UserDelegate userDelegate;
    AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        usersLoanViewModel =
                ViewModelProviders.of(this).get(UsersLoanViewModel.class);
        View root = inflater.inflate(R.layout.fragment_users_loan_settings, container, false);
        /*if (getArguments() != null) {
            userDelegate = UserSettingsFragmentArgs.fromBundle(getArguments()).getDelegate();
        }*/

        recyclerView = root.findViewById(R.id.lst_users_with_loans);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        usersList = new ArrayList<>();
        UserTableOperations userTableOperations = new UserTableOperations(requireContext());
        usersList = userTableOperations.getLoanUserData();

        if (usersList.isEmpty())
            Toast.makeText(requireContext(), R.string.no_loan_users_yet, Toast.LENGTH_SHORT).show();
        adapter = new UserLoanItemAdapter(requireContext(), usersList, new UserLoanClickListener() {
            @Override
            public void onItemClicked(UserModel user, int position) {
                showAlertDialog(user, position);

            }
        });
        recyclerView.setAdapter(adapter);

        /*final TextView textView = root.findViewById(R.id.text_gallery);
        userSettingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    public void showAlertDialog(final UserModel user, final int position) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.pay_part);
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.pay_amount_layout, null);
        builder.setView(customLayout);

        // add a button
        Button btnPay = customLayout.findViewById(R.id.btnPay);
        final EditText editTextPayAmount = customLayout.findViewById(R.id.editTextPayAmount);
        final TextView txtName = customLayout.findViewById(R.id.txtName);
        final TextView txtAllLoanAmount = customLayout.findViewById(R.id.txtAllLoanAmount);
        txtName.setText(user.name);
        txtAllLoanAmount.setText(user.money_amount + "");
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payAmountStr = editTextPayAmount.getText().toString();
                if (payAmountStr.equals("")) {
                    editTextPayAmount.setError(getString(R.string.err_money_payed));
                    return;
                }
                int payAmount = Integer.parseInt(payAmountStr);
                if (payAmount <= 0 || payAmount > user.money_amount) {
                    editTextPayAmount.setError(getString(R.string.err_money_payed_value_unvalid));
                    return;
                }
                updateMoneyAmount(payAmount, user.mobileNum);
//                adapter.notifyDataSetChanged();
                int newMoney = user.money_amount - payAmount;
                UserModel user2 = user;
                user2.money_amount = newMoney;
                usersList.set(position, user2);
                adapter.notifyItemChanged(position);
                dialog.dismiss();
            }
        });
        // create and show the alert dialog
        dialog = builder.create();
        dialog.show();
    }

    private void updateMoneyAmount(int amount, String mobileNum) {
        try {
            UserTableOperations userTableOperations = new UserTableOperations(requireContext());
            int moneyLoanBefore = userTableOperations.getLoanOfUserByMobileNum(mobileNum);
            int newMoney = moneyLoanBefore - amount;
            int result = userTableOperations.updateUserLoanMoney(newMoney, mobileNum);
            Toast.makeText(requireContext(), R.string.saved, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(requireContext(), R.string.err_somthing_wrong, Toast.LENGTH_SHORT).show();
        }
    }
}