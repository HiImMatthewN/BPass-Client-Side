package com.spcba.bpass.ui.fragments.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.spcba.bpass.databinding.FragmentReceiptsBinding;
import com.spcba.bpass.ui.adapters.ReceiptsAdapter;
import com.spcba.bpass.ui.viewmodels.TopUpFragmentViewModel;

public class TransactionHistoryFragment extends Fragment {
    private FragmentReceiptsBinding binder;
    private RecyclerView receiptsRv;
    private TextView noReceiptsTv;
    private TopUpFragmentViewModel topUpFragmentViewModel;
    private ReceiptsAdapter receiptsAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentReceiptsBinding.inflate(inflater,container,false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topUpFragmentViewModel = new ViewModelProvider(this).get(TopUpFragmentViewModel.class);
        receiptsRv = binder.transactionsRv;
        noReceiptsTv = binder.noReceipts;
        receiptsAdapter = new ReceiptsAdapter();
        receiptsRv.setAdapter(receiptsAdapter);

        topUpFragmentViewModel.requestTopUpHistory();
        noReceiptsTv.setVisibility(View.VISIBLE);
        topUpFragmentViewModel.getTopUpHistory().observe(getViewLifecycleOwner(),topUps -> {
            if (topUps.size() > 0){
                receiptsAdapter.insertData(topUps);
                noReceiptsTv.setVisibility(View.GONE);

            }
        });





    }
}
