package com.tele.ramitelecom.ui.transfers.all;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tele.ramitelecom.R;
import com.tele.ramitelecom.ui.users.TransferModel;

import java.util.List;

public class AllTransferLogItemAdapter extends RecyclerView.Adapter<AllTransferLogItemAdapter.UserItemViewHolder> {

    Context mCtx;
    List<TransferModel> transfersList;

    public void setList(List<TransferModel> transfersList) {
        this.transfersList = transfersList;
        notifyDataSetChanged();

    }

    public AllTransferLogItemAdapter(Context mCtx, List<TransferModel> transfersList) {
        this.mCtx = mCtx;
        this.transfersList = transfersList;
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.all_transfer_log_item,
                parent, false);
        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserItemViewHolder holder, int position) {
        final TransferModel transfer = transfersList.get(position);

        holder.txtName.setText(transfer.mobileNum);
        holder.txtMoneyAmount.setText(transfer.money_amount + "");
        holder.txtCode.setText(transfer.transfer_code);

        if (transfer.company == 1)
            holder.imgCompany.setImageResource(R.drawable.syriatel);
        else
            holder.imgCompany.setImageResource(R.drawable.mtn);

        holder.txtDate.setText(transfer.transfer_date);
    }

    @Override
    public int getItemCount() {
        return transfersList.size();
    }

    static class UserItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtMoneyAmount, txtCashOrLoan, txtCode, txtDate;
        ImageView imgCompany;

        public UserItemViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtNameOrMobile);
            txtMoneyAmount = itemView.findViewById(R.id.txtMoneyAmount);
            txtCashOrLoan = itemView.findViewById(R.id.txtCashOrLoan);
            txtCode = itemView.findViewById(R.id.txtCode);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgCompany = itemView.findViewById(R.id.imgCompany);

        }
    }
}
