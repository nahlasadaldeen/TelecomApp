package com.tele.ramitelecom.ui.transfers.one;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tele.ramitelecom.R;
import com.tele.ramitelecom.api_connection.TransfersApi;
import com.tele.ramitelecom.db.TransferTableOperations;
import com.tele.ramitelecom.ui.users.TransferModel;
import com.tele.ramitelecom.ui.users.UserDelegate;
import com.tele.ramitelecom.ui.users.UserModel;

import java.util.ArrayList;
import java.util.Calendar;

public class OneTransferLogFragment extends Fragment {

    RecyclerView recyclerView;
    OneTransferLogItemAdapter adapter;
    ArrayList<TransferModel> transfersList;
    private OneTransferLogViewModel oneTransferLogViewModel;
    UserModel userSelected = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        oneTransferLogViewModel =
                ViewModelProviders.of(this).get(OneTransferLogViewModel.class);
        View root = inflater.inflate(R.layout.fragment_one_transfer_log, container, false);
        Bundle b = getArguments();
        if (b != null && !b.isEmpty()) {
            userSelected = OneTransferLogFragmentArgs.fromBundle(getArguments()).getUser();
        }

        recyclerView = root.findViewById(R.id.lst_log);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        transfersList = new ArrayList<>();

        if (userSelected == null) {

            TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());
            transfersList = transferTableOperations.getOneTransferData();
            if (transfersList.isEmpty())
                Toast.makeText(requireContext(), R.string.no_transfers_yet, Toast.LENGTH_SHORT).show();
            adapter = new OneTransferLogItemAdapter(requireContext(), transfersList);
            recyclerView.setAdapter(adapter);
        }
        if (userSelected != null) {
            transfersList = getTransferLogForUser(userSelected);
            if (transfersList.isEmpty())
                Toast.makeText(requireContext(), R.string.no_transfers_yet, Toast.LENGTH_SHORT).show();
            adapter = new OneTransferLogItemAdapter(requireContext(), transfersList);
//            adapter.setList(transfersList);
            recyclerView.setAdapter(adapter);
//        recyclerView.invalidate();
//            adapter.notifyDataSetChanged();
        }
        Button btnSelectLogByDate = root.findViewById(R.id.btnSelectLogByDate);
        btnSelectLogByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.MySpinnerDatePickerStyle, myDateSetListener, c
                        .get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        Button btnSelectLogByUser = root.findViewById(R.id.btnSelectLogByUser);
        btnSelectLogByUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(requireParentFragment()).navigate(
                        OneTransferLogFragmentDirections.actionNavUsersSettings1(true, new UserDelegate() {
                            @Override
                            public void getUser(UserModel user) {
                                userSelected = user;
                                transfersList = getTransferLogForUser(user);
                            }
                        }, "2"));
            }
        });
        return root;
    }

    private ArrayList<TransferModel> getTransferLogForUser(UserModel user) {
        TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());
//        transfersList = transferTableOperations.getTransferDataByUser(user.mobileNum);
        TransfersApi transferApi = new TransfersApi(requireContext());
        transfersList = transferApi.getAllTransfersForUser(2);

        Log.e("dddddd", transfersList.size() + "");
        if (transfersList.isEmpty()) {
            Toast.makeText(requireContext(), R.string.no_transfers_yet, Toast.LENGTH_SHORT).show();
            return new ArrayList(0);
        } else
            return transfersList;
        /*adapter = new TransferLogItemAdapter(requireContext(), transfersList);
        adapter.setList(transfersList);
        recyclerView.setAdapter(adapter);
//        recyclerView.invalidate();
        adapter.notifyDataSetChanged();*/
    }

    DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int j, int k) {

            int year = i;
            int month = j;
            int day = k;
            String selectedDate = day + "-" + (month + 1) + "-" + year;

            transfersList = new ArrayList<>();
            TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());
            transfersList = transferTableOperations.getOneTransferDataByDate(selectedDate);

            if (transfersList.isEmpty())
                Toast.makeText(requireContext(), R.string.no_transfers_yet, Toast.LENGTH_SHORT).show();

            adapter = new OneTransferLogItemAdapter(requireContext(), transfersList);
            recyclerView.setAdapter(adapter);
            Log.e("hhhh", selectedDate);
        }
    };
}