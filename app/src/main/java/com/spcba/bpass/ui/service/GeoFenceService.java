package com.spcba.bpass.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.spcba.bpass.helper.AppPermissionHelper;
import com.spcba.bpass.helper.LocationHelper;
import com.spcba.bpass.helper.NotificationHelper;

public class GeoFenceService extends Service {
    private GeofencingClient geofencingClient;
    private LocationHelper locationHelper = LocationHelper.getInstance();
    private NotificationHelper notificationHelper = NotificationHelper.getInstance();
    private static final String TAG = "GeoFenceService";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Service");
        notificationHelper.createNotificationManager(getApplicationContext());


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        geofencingClient = LocationServices.getGeofencingClient(getApplicationContext());
        if (AppPermissionHelper.checkLocationPermission(getApplicationContext())) {
            locationHelper.initListeners(getApplication());
            locationHelper.startLocationListener();


            geofencingClient.addGeofences(locationHelper.geofencingRequest(), locationHelper.getGeofencePendingIntent(getApplicationContext()))
                    .addOnSuccessListener(task -> {
                        Log.d(TAG, "GeoFence Added");
                        startForeground(NotificationHelper.NOTIFICATION_ID, notificationHelper.createNotification("BPass Location Service"
                                , "Scanning Location...."));
                    }).addOnFailureListener(task -> {
                if (task.getMessage() == null) return;
                if (task.getMessage().trim().equals(String.valueOf(GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE).trim() + ":"))
                    startForeground(NotificationHelper.NOTIFICATION_ID, notificationHelper.createNotification("Error Occurred"
                            , "Please Turn on GPS"));
                if (task.getMessage().trim().equals(String.valueOf(GeofenceStatusCodes.GEOFENCE_INSUFFICIENT_LOCATION_PERMISSION).trim() + ":"))
                    startForeground(NotificationHelper.NOTIFICATION_ID, notificationHelper.createNotification("Error Occurred"
                            , "Please enable location permission"));

            });
        } else {
            Log.d(TAG, "Permission Not Handle, Stopping Service");
            stopSelf();
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Called");
        notificationHelper.dismissNotification(NotificationHelper.NOTIFICATION_ID);
        locationHelper.deAttachLocationListener();
    }
}
