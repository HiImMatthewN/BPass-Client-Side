package com.spcba.bpass.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.spcba.bpass.R;
import com.spcba.bpass.databinding.ActivityMainBinding;
import com.spcba.bpass.ui.viewmodels.LobbyActivityViewModel;

public class LobbyActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ActivityMainBinding binder;
    private LobbyActivityViewModel lobbyActivityViewModel;
    private static final String TAG = "LobbyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: UID  " + FirebaseAuth.getInstance().getCurrentUser().getUid());
        lobbyActivityViewModel = new ViewModelProvider(this).get(LobbyActivityViewModel.class);
        binder = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());
        bottomNavigationView = binder.bottomNav;

        NavigationUI.setupWithNavController(bottomNavigationView, Navigation.findNavController(this, R.id.nav_host_fragment));


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.nav_host_fragment));
            return true;
        });
    }
}