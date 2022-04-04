package com.tele.ramitelecom.ui.users_loan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tele.ramitelecom.R;
import com.tele.ramitelecom.ui.users.UserModel;

import java.util.List;

public class UserLoanItemAdapter extends RecyclerView.Adapter<UserLoanItemAdapter.UserItemViewHolder> {

    Context mCtx;
    List<UserModel> usersList;
    UserLoanClickListener userItemClickListener;

    public UserLoanItemAdapter(Context mCtx, List<UserModel> usersList, UserLoanClickListener userItemClickListener) {
        this.mCtx = mCtx;
        this.usersList = usersList;
        this.userItemClickListener = userItemClickListener;
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.user_loan_item,
                parent, false);
        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserItemViewHolder holder, final int position) {
        final UserModel user = usersList.get(position);

        holder.txtName.setText(user.name);

        holder.txtMoneyAmount.setText(user.money_amount + "");

        holder.btnPayMoneyPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userItemClickListener.onItemClicked(user, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    static class UserItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtMoneyAmount;
        Button btnPayMoneyPart;

        public UserItemViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtMoneyAmount = itemView.findViewById(R.id.txtMoneyAmount);
            btnPayMoneyPart = itemView.findViewById(R.id.btnPayMoneyPart);
        }
    }
}
