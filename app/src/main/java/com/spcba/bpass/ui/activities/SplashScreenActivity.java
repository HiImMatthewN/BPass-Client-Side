package com.spcba.bpass.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.spcba.bpass.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding binder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());

        new Handler().postDelayed(()->{
            if (FirebaseAuth.getInstance().getCurrentUser()== null)
                goToLoginActivity();
            else
                goToLobbyActivity();


            finish();
        },1500);
    }
    private void goToLoginActivity(){
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
    }
    private void goToLobbyActivity(){
        Intent lobbyActivity = new Intent(this, LobbyActivity.class);
        startActivity(lobbyActivity);
    }
}
