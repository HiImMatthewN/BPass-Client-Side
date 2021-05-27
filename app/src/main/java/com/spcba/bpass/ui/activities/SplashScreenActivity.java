package com.spcba.bpass.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.spcba.bpass.ExceptionHandler;
import com.spcba.bpass.databinding.ActivitySplashScreenBinding;
import com.spcba.bpass.helper.SharedPrefHelper;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding binder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        SharedPrefHelper.init(this);

        binder = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());


    }

}
