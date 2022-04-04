package com.tele.ramitelecom.ui.transfers.all;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tele.ramitelecom.R;
import com.tele.ramitelecom.db.TransferTableOperations;
import com.tele.ramitelecom.ui.users.TransferModel;
import com.tele.ramitelecom.ui.users.UserModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllTransferLogFragment extends Fragment {

    RecyclerView recyclerView;
    AllTransferLogItemAdapter adapter;
    List<TransferModel> transfersList;
    private AllTransferLogViewModel allTransferLogViewModel;
    UserModel userSelected = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        allTransferLogViewModel =
                ViewModelProviders.of(this).get(AllTransferLogViewModel.class);
        View root = inflater.inflate(R.layout.fragment_one_transfer_log, container, false);
        Bundle b = getArguments();


        recyclerView = root.findViewById(R.id.lst_log);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        transfersList = new ArrayList<>();

        if (userSelected == null) {

            TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());
            transfersList = transferTableOperations.getAllTransferData();
            if (transfersList.isEmpty())
                Toast.makeText(requireContext(), R.string.no_transfers_yet, Toast.LENGTH_SHORT).show();
            adapter = new AllTransferLogItemAdapter(requireContext(), transfersList);
            recyclerView.setAdapter(adapter);
        }
        if (userSelected != null) {
            transfersList = getTransferLogForUser(userSelected);
            if (transfersList.isEmpty())
                Toast.makeText(requireContext(), R.string.no_transfers_yet, Toast.LENGTH_SHORT).show();
            adapter = new AllTransferLogItemAdapter(requireContext(), transfersList);
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

        return root;
    }

    private List<TransferModel> getTransferLogForUser(UserModel user) {
        TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());
        transfersList = transferTableOperations.getTransferDataByUser(user.mobileNum);

        if (transfersList.isEmpty()) {
            Toast.makeText(requireContext(), R.string.no_transfers_yet, Toast.LENGTH_SHORT).show();
            return null;
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
            transfersList = transferTableOperations.getAllTransferDataByDate(selectedDate);

            if (transfersList.isEmpty())
                Toast.makeText(requireContext(), R.string.no_transfers_yet, Toast.LENGTH_SHORT).show();

            adapter = new AllTransferLogItemAdapter(requireContext(), transfersList);
            recyclerView.setAdapter(adapter);
            Log.e("hhhh", selectedDate);
        }
    };
}