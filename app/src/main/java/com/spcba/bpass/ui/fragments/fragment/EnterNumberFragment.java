package com.spcba.bpass.ui.fragments.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.spcba.bpass.R;
import com.spcba.bpass.UneditableTextWatcher;
import com.spcba.bpass.databinding.FragmentEnterNumberBinding;
import com.spcba.bpass.ui.viewmodels.LoginActivityViewModel;

public class EnterNumberFragment extends Fragment {
    private FragmentEnterNumberBinding binder;
    private EditText mobileNumberEt;
    private Button verifyBtn;
    private ProgressBar progressBar;

    private NavController navController;

    private LoginActivityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentEnterNumberBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginActivityViewModel.class);
        navController = Navigation.findNavController(view);
        mobileNumberEt = binder.enterMobileNumEt;
        verifyBtn = binder.verifyBtn;
        progressBar = binder.progressBar;

        verifyBtn.setOnClickListener(btn -> {
            String mobileNumber = mobileNumberEt.getText().toString();
            viewModel.checkIfNumberExists(mobileNumber);
            isVerifying(true);

        });
        viewModel.getInvalidInput().observe(getViewLifecycleOwner(), inputInvalidEvent -> {
            if (inputInvalidEvent.isHandled()) return;
            if (inputInvalidEvent.getContentIfNotHandled()){
                isVerifying(false);
                mobileNumberEt.setError("Invalid Input");
            }
        });
        viewModel.getOnCodeReceived().observe(getViewLifecycleOwner(), onCodeReceived ->
        {
            if (onCodeReceived.isHandled()) return;
            if (onCodeReceived.getContentIfNotHandled()){
                navController.navigate(R.id.action_enterNumberFragment_to_enterCodeFragment);
                isVerifying(false);

            }
        });

        viewModel.getFirebaseAuthException().observe(getViewLifecycleOwner(),exceptionEvent ->{
                    if (exceptionEvent.isHandled()) return;
                    if (exceptionEvent.getContentIfNotHandled() != null)
                        isVerifying(false);
        });
        viewModel.getIfNumberAlreadyExists().observe(getViewLifecycleOwner(),numberAlreadyExists->{
                    if (numberAlreadyExists.isHandled()) return;
                    boolean isNumberTaken = numberAlreadyExists.getContentIfNotHandled();
                    if (!isNumberTaken){
                        mobileNumberEt.setError("Number not registered");
                        isVerifying(false);
                    }else{
                        String mobileNumber = mobileNumberEt.getText().toString();
                        viewModel.verifyPhoneNumber(mobileNumber,requireActivity());

                    }



        });
        mobileNumberEt.addTextChangedListener(new UneditableTextWatcher(mobileNumberEt, "+639"));

    }

    /**
     * @param value
     *  When value is true, show progress bar
     */
    private void isVerifying(boolean value) {
        if (value) {
            progressBar.setVisibility(View.VISIBLE);
            verifyBtn.setText("");
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            verifyBtn.setText("Next");
        }
        verifyBtn.setEnabled(!value);
    }

}
