package com.spcba.bpass.ui.fragments.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.spcba.bpass.GenericTextWatcher;
import com.spcba.bpass.R;
import com.spcba.bpass.databinding.FragmentEnterCodeBinding;
import com.spcba.bpass.ui.activities.LobbyActivity;
import com.spcba.bpass.ui.viewmodels.LoginActivityViewModel;

public class EnterCodeFragment extends Fragment {
    private FragmentEnterCodeBinding binder;
    private EditText e1;
    private EditText e2;
    private EditText e3;
    private EditText e4;
    private EditText e5;
    private EditText e6;
    private Button verifyCodeBtn;
    private ProgressBar progressBar;
    private LoginActivityViewModel viewModel;
    private static final String TAG = "EnterCodeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentEnterCodeBinding.inflate(inflater, container, false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginActivityViewModel.class);
        verifyCodeBtn = binder.verifyBtn;
        e1 = binder.e1;
        e2 = binder.e2;
        e3 = binder.e3;
        e4 = binder.e4;
        e5 = binder.e5;
        e6 = binder.e6;
        progressBar =binder.progressBar;
        verifyCodeBtn.setOnClickListener(btn -> {
            String codeNumOne = e1.getText().toString();
            String codeNumTwo = e2.getText().toString();
            String codeNumThree = e3.getText().toString();
            String codeNumFour = e4.getText().toString();
            String codeNumFive = e5.getText().toString();
            String codeNumSix = e6.getText().toString();
            String codeInputted = codeNumOne + codeNumTwo + codeNumThree + codeNumFour + codeNumFive + codeNumSix;

            viewModel.verifyCode(codeInputted, requireActivity());
            isVerifying(true);
        });

        viewModel.getSignInStatusLiveData().observe(getViewLifecycleOwner(), onSignInEvent -> {
            if (onSignInEvent.isHandled()) return;
            Boolean status = onSignInEvent.getContentIfNotHandled();
            if (status) {
                viewModel.uploadProfilePic();
                if (viewModel.getCurrentUser() == null){
                    requireActivity().finish();
                    goToLobby();
                }

            } else
                Log.d(TAG, "Error Signing in");
        });
        viewModel.getDownloadedUri().observe(getViewLifecycleOwner(), event -> {
            if (event.isHandled()) return;
            Uri uri = event.getContentIfNotHandled();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            viewModel.saveUser(uid, uri);

        });
        viewModel.getSaveUserStatus().observe(getViewLifecycleOwner(),userSaveEvent->{

            requireActivity().finish();
            goToLobby();
            isVerifying(false);

        });
        e1.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.border_edittext_bg_dark));
        e1.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(e1, InputMethodManager.SHOW_IMPLICIT);


        e1.addTextChangedListener(new GenericTextWatcher(e2, e1, e1, requireContext()));
        e2.addTextChangedListener(new GenericTextWatcher(e3, e2, e1, requireContext()));
        e3.addTextChangedListener(new GenericTextWatcher(e4, e3, e2, requireContext()));
        e4.addTextChangedListener(new GenericTextWatcher(e5, e4, e3, requireContext()));
        e5.addTextChangedListener(new GenericTextWatcher(e6, e5, e4, requireContext()));
        e6.addTextChangedListener(new GenericTextWatcher(e6, e6, e5, requireContext()));

    }
    private void goToLobby() {
        Intent lobbyIntent = new Intent(requireContext(), LobbyActivity.class);
        requireActivity().startActivity(lobbyIntent);
    }
    /**
     * @param value
     *  When value is true, show progress bar
     */
    private void isVerifying(boolean value) {
        if (value) {
            progressBar.setVisibility(View.VISIBLE);
            verifyCodeBtn.setText("");
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            verifyCodeBtn.setText("Next");
        }
        verifyCodeBtn.setEnabled(!value);
    }

}
