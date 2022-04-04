package com.tele.ramitelecom.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tele.ramitelecom.R;
import com.tele.ramitelecom.db.UserTableOperations;

import java.util.ArrayList;
import java.util.List;

public class UserSettingsFragment extends Fragment {

    RecyclerView recyclerView;
    UserItemAdapter adapter;
    List<UserModel> usersList;
    private UserSettingsViewModel userSettingsViewModel;
    UserDelegate userDelegate;

    String isHomeOrLog = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userSettingsViewModel =
                ViewModelProviders.of(this).get(UserSettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user_settings, container, false);
        boolean isUser = false;
        if (getArguments() != null && !getArguments().isEmpty()) {
            isUser = UserSettingsFragmentArgs.fromBundle(getArguments()).getIsUser();
            userDelegate = UserSettingsFragmentArgs.fromBundle(getArguments()).getDelegate();
            isHomeOrLog = UserSettingsFragmentArgs.fromBundle(getArguments()).getFrom();
        }

        recyclerView = root.findViewById(R.id.lst_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        usersList = new ArrayList<>();
        UserTableOperations userTableOperations = new UserTableOperations(requireContext());
        usersList = userTableOperations.getUserData();
        if (usersList.isEmpty())
            Toast.makeText(requireContext(), R.string.no_users_saved, Toast.LENGTH_SHORT).show();
        final boolean finalIsUser = isUser;
        adapter = new UserItemAdapter(requireContext(), usersList, new UserItemClickListener() {
            @Override
            public void onItemClicked(UserModel user) {
                if (finalIsUser) {
                    userDelegate.getUser(user);
//                    NavHostFragment.findNavController(requireParentFragment()).navigateUp();
                    if (isHomeOrLog.equals("1"))
                        NavHostFragment.findNavController(requireParentFragment()).navigate(
                                UserSettingsFragmentDirections.backToA(user));
                    if (isHomeOrLog.equals("2"))
                        NavHostFragment.findNavController(requireParentFragment()).navigate(
                                UserSettingsFragmentDirections.backToB(user));
                }
            }
        }, new UserDeleteClickListener() {
            @Override
            public void onDeleteClicked(UserModel user, int position) {
                deleteUser(user);
                adapter.notifyItemRemoved(position);
                usersList.remove(position);
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

    private void deleteUser(UserModel user) {
        try {
            UserTableOperations userTableOperations = new UserTableOperations(requireContext());
            boolean result = userTableOperations.deleteUserData(user.id);
            if (result)
                Toast.makeText(requireContext(), getString(R.string.user_deleted), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(requireContext(), R.string.err_somthing_wrong, Toast.LENGTH_SHORT).show();
        }
    }
}

interface UserItemClickListener {
    void onItemClicked(UserModel user);
}

interface UserDeleteClickListener {
    void onDeleteClicked(UserModel user, int position);
}