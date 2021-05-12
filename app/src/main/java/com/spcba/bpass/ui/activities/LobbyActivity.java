package com.spcba.bpass.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.spcba.bpass.helper.AppPermissionHelper;
import com.spcba.bpass.R;
import com.spcba.bpass.databinding.ActivityMainBinding;
import com.spcba.bpass.ui.service.GeoFenceService;
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

    @Override
    protected void onResume() {
        super.onResume();
        if (AppPermissionHelper.checkLocationPermission(this)){
            startLocationService();
        }else{
            Log.d(TAG, "onResume: Permissiong not granted");
            AppPermissionHelper.requestPermission(this);

        }

    }

    private void startLocationService(){
        Log.d(TAG, "startLocationService: Starting Service");
        Intent locationServiceIntent = new Intent(this, GeoFenceService.class);
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O)
            startForegroundService(locationServiceIntent);
        else
            startService(locationServiceIntent);
    }
    private void stopLocationService(){
        Intent locationServiceIntent = new Intent(this, GeoFenceService.class);
        stopService(locationServiceIntent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppPermissionHelper.PERMISSION_CODE)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startLocationService();
    }


}