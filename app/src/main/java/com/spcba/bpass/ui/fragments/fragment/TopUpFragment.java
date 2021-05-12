package com.spcba.bpass.ui.fragments.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.spcba.bpass.databinding.FragmentTopupBinding;
import com.spcba.bpass.ui.custom.UneditableTextWatcher;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;

public class TopUpFragment extends Fragment {
    private FragmentTopupBinding binder;
    private TextView currentBalanceTv;
    private TextView nameTv;
    private TextView mobileNumberTv;
    private TextView backTv;
    private EditText amountEt;
    private LobbyActivityViewModel viewModel;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentTopupBinding.inflate(inflater,container,false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LobbyActivityViewModel.class);
        navController = Navigation.findNavController(view);
        currentBalanceTv = binder.balanceTv;
        nameTv = binder.nameTv;
        mobileNumberTv = binder.mobileNumberTv;
        backTv = binder.backTv;
        amountEt = binder.amountEt;
        viewModel.getUser().observe(getViewLifecycleOwner(),user -> {
            currentBalanceTv.setText(String.format("₱%.2f", user.getBalance()));

            nameTv.setText(user.getName());
            mobileNumberTv.setText(user.getMobileNumber());

        });
        backTv.setOnClickListener(btn->{
            navController.popBackStack();

        });
        amountEt.setText("₱");
        amountEt.addTextChangedListener(new UneditableTextWatcher(amountEt,"₱"));
    }
}
