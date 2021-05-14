package com.spcba.bpass.ui.fragments.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.spcba.bpass.R;
import com.spcba.bpass.databinding.FragmentTopupBinding;
import com.spcba.bpass.ui.custom.UneditableTextWatcher;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;
import com.spcba.bpass.ui.viewmodels.TopUpFragmentViewModel;

public class TopUpFragment extends Fragment {
    private FragmentTopupBinding binder;
    private TextView currentBalanceTv;
    private TextView nameTv;
    private TextView mobileNumberTv;
    private TextView backTv;
    private TextView historyTv;

    private EditText amountEt;
    private NavController navController;


    private Button amountTenBtn;
    private Button amountTwentyBtn;
    private Button amountFiftyBtn;
    private Button amountHundredBtn;
    private Button amountTwoHundredBtn;
    private Button amountFiveHundredBtn;
    private Button amountThousandBtn;


    private RadioButton gCashOption;
    private RadioButton cashOption;

    private Button confirmPayment;
    private ProgressBar progressBar;
    private TopUpFragmentViewModel topUpFragmentViewModel;
    private LobbyActivityViewModel viewModel;

    private static final String TAG = "TopUpFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentTopupBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LobbyActivityViewModel.class);
        topUpFragmentViewModel = new ViewModelProvider(this).get(TopUpFragmentViewModel.class);
        navController = Navigation.findNavController(view);
        currentBalanceTv = binder.balanceTv;
        nameTv = binder.nameTv;
        mobileNumberTv = binder.mobileNumberTv;
        backTv = binder.backTv;
        amountEt = binder.amountEt;
        historyTv = binder.historyTv;

        amountTenBtn = binder.amountTenBtn;
        amountTwentyBtn = binder.amountTwentyBtn;
        amountFiftyBtn = binder.amountFiftyBtn;
        amountHundredBtn = binder.amountHundredBtn;
        amountTwoHundredBtn = binder.amountTwoHundredBtn;
        amountFiveHundredBtn = binder.amountFiveHundredBtn;
        amountThousandBtn = binder.amountThousandBtn;

        gCashOption = binder.gcashRb;
        cashOption = binder.cashRb;

        confirmPayment = binder.confirmPaymentBtn;
        progressBar = binder.progressBar;


        backTv.setOnClickListener(btn -> {
            navController.popBackStack();

        });
        historyTv.setOnClickListener(btn->{
            navController.navigate(R.id.action_topUpFragment_to_transactionHistoryFragment2);

        });
        amountEt.setText("₱");
        amountEt.addTextChangedListener(new UneditableTextWatcher(amountEt, "₱"));
        amountEt.setOnTouchListener((v, event) -> true);


        amountTenBtn.setOnClickListener(btn -> {
            amountEt.setText("₱10");
            amountEt.setSelection(amountEt.getText().length());
            topUpFragmentViewModel.setAmount(10);
        });
        amountTwentyBtn.setOnClickListener(btn -> {
            amountEt.setText("₱20");
            amountEt.setSelection(amountEt.getText().length());
            topUpFragmentViewModel.setAmount(20);

        });
        amountFiftyBtn.setOnClickListener(btn -> {
            amountEt.setText("₱50");
            amountEt.setSelection(amountEt.getText().length());
            topUpFragmentViewModel.setAmount(50);

        });
        amountHundredBtn.setOnClickListener(btn -> {
            amountEt.setText("₱100");
            amountEt.setSelection(amountEt.getText().length());
            topUpFragmentViewModel.setAmount(100);

        });
        amountTwoHundredBtn.setOnClickListener(btn -> {
            amountEt.setText("₱200");
            amountEt.setSelection(amountEt.getText().length());
            topUpFragmentViewModel.setAmount(200);

        });
        amountFiveHundredBtn.setOnClickListener(btn -> {
            amountEt.setText("₱500");
            amountEt.setSelection(amountEt.getText().length());
            topUpFragmentViewModel.setAmount(500);

        });
        amountThousandBtn.setOnClickListener(btn -> {
            amountEt.setText("₱1000");
            amountEt.setSelection(amountEt.getText().length());
            topUpFragmentViewModel.setAmount(1000);
        });

        gCashOption.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                topUpFragmentViewModel.setPaymentOption("GCash");

        });
        cashOption.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked)
                topUpFragmentViewModel.setPaymentOption("Cash");

        }));

        confirmPayment.setOnClickListener(btn -> {
            isVerifying(true);
            topUpFragmentViewModel.confirmPayment();
        });

        topUpFragmentViewModel.getAddedTopUpStatus().observe(getViewLifecycleOwner(), addedEvent -> {
            if (addedEvent.isHandled()) return;
            if (addedEvent.getContentIfNotHandled())
                navController.navigate(R.id.action_topUpFragment_to_topUpOverviewFragment);

            isVerifying(false);

        });
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            currentBalanceTv.setText(String.format("₱%.2f", user.getBalance()));

            nameTv.setText(user.getName());
            mobileNumberTv.setText(user.getMobileNumber());
            topUpFragmentViewModel.setUser(user);
        });

    }
    private void isVerifying(boolean value) {
        if (value) {
            progressBar.setVisibility(View.VISIBLE);
            confirmPayment.setText("");
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            confirmPayment.setText("Confirm Payment");
        }
        confirmPayment.setEnabled(!value);
    }

}
