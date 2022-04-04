package com.tele.ramitelecom.ui.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tele.ramitelecom.R;

import java.util.List;

public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder> {

    Context mCtx;
    List<UserModel> usersList;
    UserItemClickListener userItemClickListener;

    UserDeleteClickListener userDeleteClickListener;

    public UserItemAdapter(Context mCtx, List<UserModel> usersList, UserItemClickListener userItemClickListener, UserDeleteClickListener userDeleteClickListener) {
        this.mCtx = mCtx;
        this.usersList = usersList;
        this.userItemClickListener = userItemClickListener;
        this.userDeleteClickListener = userDeleteClickListener;
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.user_item,
                parent, false);
        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserItemViewHolder holder, final int position) {
        final UserModel user = usersList.get(position);

        holder.txtName.setText(user.name);

       /* holder.txtEnableType.setText(user.subscribeType + "");
        if (user.subscribeType < 1)
            holder.txtEnableType.setVisibility(View.GONE);*/

        holder.txtMobileNum.setText(user.mobileNum);

       /* holder.txtEmail.setText(user.email);
        if (user.email == null || user.email.equals("")) {
            holder.txtEmail.setVisibility(View.GONE);
            holder.txtLeftEdge.setVisibility(View.GONE);
            holder.txtRightEdge.setVisibility(View.GONE);
        }

        holder.txtCode.setText(user.enableCode);
        if (user.enableCode == null || user.enableCode.equals("0") || user.enableCode.equals(""))
            holder.txtCode.setVisibility(View.GONE);

        holder.txtAttemptNum.setText(user.attemptsNum + "");
        if (user.attemptsNum < 1) {
            holder.txtAttemptNum.setVisibility(View.GONE);
            holder.txtLeft.setVisibility(View.GONE);
            holder.txtRight.setVisibility(View.GONE);
        }*/

        holder.txtMoneyAmount.setText(user.money_amount + "");
        if (user.money_amount < 1) {
            holder.txtMoneyAmount.setVisibility(View.GONE);
            holder.lblMoneyAmount.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userItemClickListener.onItemClicked(user);
            }
        });
        holder.btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDeleteClickListener.onDeleteClicked(user,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    static class UserItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtMobileNum, txtMoneyAmount, lblMoneyAmount, txtEnableType, txtEmail, txtCode, txtAttemptNum,
                txtLeft, txtLeftEdge, txtRight, txtRightEdge;

        Button btnDeleteUser;

        public UserItemViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
//            txtEnableType = itemView.findViewById(R.id.txtEnableType);
            txtMobileNum = itemView.findViewById(R.id.txtMobileNum);
//            txtEmail = itemView.findViewById(R.id.txtEmail);
//            txtCode = itemView.findViewById(R.id.txtCode);
//            txtAttemptNum = itemView.findViewById(R.id.txtAttemptNum);
            lblMoneyAmount = itemView.findViewById(R.id.lblMoneyAmount);
            txtMoneyAmount = itemView.findViewById(R.id.txtMoneyAmount);

            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
//            txtLeft = itemView.findViewById(R.id.txtLeft);
//            txtLeftEdge = itemView.findViewById(R.id.txtLeftEdge);
//            txtRight = itemView.findViewById(R.id.txtRight);
//            txtRightEdge = itemView.findViewById(R.id.txtRightEdge);
        }
    }
}
