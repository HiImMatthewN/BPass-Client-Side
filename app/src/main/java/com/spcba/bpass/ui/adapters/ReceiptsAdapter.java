package com.spcba.bpass.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.spcba.bpass.R;
import com.spcba.bpass.data.datamodels.TopUp;
import com.spcba.bpass.data.datautils.Event;
import com.spcba.bpass.databinding.ItemReceiptsBinding;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ReceiptsViewHolder> {
    private List<TopUp> topUpList = new ArrayList<>();
    private MutableLiveData<Event<TopUp>> selectedTopUpToView = new MutableLiveData<>();
    public ReceiptsAdapter() {
    }

    public void insertData(List<TopUp> data) {
        this.topUpList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReceiptsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receipts,parent,false);
        ItemReceiptsBinding binder = ItemReceiptsBinding.bind(view);
        return new ReceiptsViewHolder(binder);


    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsViewHolder holder, int position) {
            TopUp topUp = topUpList.get(position);
            holder.paymentMethodTv.setText(topUp.getPaymentMethod());
            holder.amountTv.setText("₱" + topUp.getAmount());
            holder.dateTv.setText(String.valueOf(topUp.getDateCreated()));

            if (topUp.isPaid()){
                holder.receiptStatusTv.setText("Paid");
                holder.receiptStatusTv.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.bg_receipt_paid));
            }else{
                holder.receiptStatusTv.setText("Pending");
                holder.receiptStatusTv.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.bg_receipt_pending));

            }
            holder.bind(topUp);
    }

    public LiveData<Event<TopUp>> getSelectedTopUpToView() {
        return selectedTopUpToView;
    }

    @Override
    public int getItemCount() {
        return topUpList.size();
    }

    class ReceiptsViewHolder extends RecyclerView.ViewHolder {
        private TextView paymentMethodTv;
        private TextView dateTv;
        private TextView amountTv;
        private TextView receiptStatusTv;

        public ReceiptsViewHolder(@NonNull ItemReceiptsBinding binder) {
            super(binder.getRoot());
            paymentMethodTv = binder.paymentMethodTv;
            dateTv = binder.dateTv;
            amountTv = binder.amountTv;
            receiptStatusTv = binder.receiptStatus;
        }
        public void bind(TopUp topUp){
            itemView.setOnClickListener(view->{
                selectedTopUpToView.setValue(new Event<>(topUp));
            });


        }
    }


}
