package com.spcba.bpass.ui.fragments.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.spcba.bpass.R;
import com.spcba.bpass.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binder;
    private Button loginBtn;
    private Button registerBtn;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = FragmentLoginBinding.inflate(inflater,container,false);
        return binder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        loginBtn = binder.loginBtn;
        registerBtn = binder.registerBtn;


        loginBtn.setOnClickListener(btn ->{
            navController.navigate(R.id.action_loginFragment_to_enterNumberFragment);
        });
        registerBtn.setOnClickListener(btn ->{
            navController.navigate(R.id.action_loginFragment_to_basicInfoFragment);

        });



    }
}
