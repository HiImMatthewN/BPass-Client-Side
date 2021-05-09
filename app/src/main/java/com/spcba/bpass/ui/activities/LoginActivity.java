package com.spcba.bpass.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.spcba.bpass.ui.viewmodels.LoginActivityViewModel;
import com.spcba.bpass.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binder;
    private LoginActivityViewModel loginActivityViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivityViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        binder = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());

    }
}
