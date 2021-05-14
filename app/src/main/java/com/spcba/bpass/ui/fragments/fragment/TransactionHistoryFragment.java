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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.spcba.bpass.R;
import com.spcba.bpass.databinding.FragmentReceiptsBinding;
import com.spcba.bpass.ui.adapters.ReceiptsAdapter;
import com.spcba.bpass.ui.viewmodels.TopUpDetailsViewModel;
import com.spcba.bpass.ui.viewmodels.TopUpHistoryViewModel;

public class TransactionHistoryFragment extends Fragment {
    private TopUpDetailsViewModel topUpDetailsViewModel;
    private TopUpHistoryViewModel topUpHistoryViewModel;


    private FragmentReceiptsBinding binder;
    private RecyclerView receiptsRv;
    private TextView noReceiptsTv;
    private TextView backTv;
    private ReceiptsAdapter receiptsAdapter;


    private NavController navController;

    private static final String TAG = "TransactionHistoryFragm";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentReceiptsBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        topUpHistoryViewModel = new ViewModelProvider(this).get(TopUpHistoryViewModel.class);
        topUpDetailsViewModel = new ViewModelProvider(requireActivity()).get(TopUpDetailsViewModel.class);
        receiptsRv = binder.transactionsRv;
        backTv = binder.backBTv;
        noReceiptsTv = binder.noReceipts;
        receiptsAdapter = new ReceiptsAdapter();
        receiptsRv.setAdapter(receiptsAdapter);

        topUpHistoryViewModel.requestTopUpHistory();
        noReceiptsTv.setVisibility(View.VISIBLE);


        backTv = binder.backBTv;

        backTv.setOnClickListener(btn->{
            navController.popBackStack();

        });

        topUpHistoryViewModel.getTopUpHistory().observe(getViewLifecycleOwner(), topUps -> {
            if (topUps.size() > 0) {
                receiptsAdapter.insertData(topUps);
                noReceiptsTv.setVisibility(View.GONE);

            }
        });
        receiptsAdapter.getSelectedTopUpToView().observe(getViewLifecycleOwner(), selectedTopUpEvent -> {
            if (selectedTopUpEvent.isHandled()) return;



            topUpDetailsViewModel.setTopUpToView(selectedTopUpEvent.getContentIfNotHandled());
            navController.navigate(R.id.action_transactionHistoryFragment2_to_topUpDetailsFragment3);
        });


    }
}
