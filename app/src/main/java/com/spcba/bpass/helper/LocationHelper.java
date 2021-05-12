package com.spcba.bpass.helper;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.spcba.bpass.ui.recievers.GeoFenceListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationHelper {
    private static LocationHelper instance;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;

    private MutableLiveData<String> locationLiveData = new MutableLiveData<>();
    private static final String TAG = "LocationHelper";

    private static final String HOME_FENCE_ID = "Home";
    public static final String FENCE_ONE_ID = "Gate";
    public static final String FENCE_TWO_ID = "MiniMart";
    public static final String FENCE_THREE_ID = "LolaMilas";

    public static LocationHelper getInstance() {

        if (instance == null)
            instance = new LocationHelper();
        return instance;
    }

    public void initListeners(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        geocoder = new Geocoder(context, Locale.getDefault());
        geofencingClient = LocationServices.getGeofencingClient(context);
    }

    @SuppressLint("MissingPermission")
    public void startLocationListener() {
        Log.d(TAG, "startLocationListener: Started Listening");
        fusedLocationClient.requestLocationUpdates(getLocationRequest(), locationCallback, Looper.getMainLooper());

    }
    public GeofencingRequest geofencingRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(createGeoFenceList());
        return builder.build();
    }

    public PendingIntent getGeofencePendingIntent(Context context) {

        Intent intent = new Intent(context, GeoFenceListener.class);
        geofencePendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }
    private  List<Geofence> createGeoFenceList() {
        //Veraville Alegria Gate lat: 14.436253 long: 121.000506
        //KPARAISO Mini Grocery lat 14.436697 long:121.001492
        //Lola milas lat 14.437328long:121.001908


        List<Geofence> geofenceList = new ArrayList<>();

        Geofence homeFence =  new Geofence.Builder().setRequestId(HOME_FENCE_ID)
                .setCircularRegion(14.435837, 121.000033, 50)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
        Geofence firstFence = new Geofence.Builder().setRequestId(FENCE_ONE_ID)
                .setCircularRegion(14.436253, 121.000506, 50)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
        Geofence secondFence = new Geofence.Builder().setRequestId(FENCE_TWO_ID)
                .setCircularRegion(14.436697, 121.001492, 50)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
        Geofence thirdFence = new Geofence.Builder().setRequestId(FENCE_THREE_ID)
                .setCircularRegion(14.437328, 121.001908, 50)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

        geofenceList.add(homeFence);
        geofenceList.add(firstFence);
        geofenceList.add(secondFence);
        geofenceList.add(thirdFence);
        return geofenceList;
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            try {
                Log.d(TAG, "onLocationResult: ");
                List<Address> currentAddress;
                currentAddress = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude()
                        , locationResult.getLastLocation().getLongitude(), 1);

                if (currentAddress.size() > 0) {
                    String featureName = currentAddress.get(0).getFeatureName();
                    String subLocality = currentAddress.get(0).getSubLocality();
                    String locality = currentAddress.get(0).getLocality();
                    if (featureName == null)
                        featureName = "";
                    if (subLocality == null)
                        subLocality = "";
                    if (locality == null)
                        locality = "";
                    locationLiveData.setValue(featureName + " " + subLocality + ", " + locality);

                }


            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onLocationResult: Error " + e.getMessage());
            }


        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
        }
    };

    public void deAttachLocationListener() {
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public LiveData<String> getLocationLiveData() {
        return locationLiveData;
    }


    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        return locationRequest;

    }

}
