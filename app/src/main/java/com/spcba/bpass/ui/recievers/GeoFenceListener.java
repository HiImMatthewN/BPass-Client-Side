package com.spcba.bpass.ui.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.spcba.bpass.helper.NotificationHelper;

import java.util.List;

public class GeoFenceListener extends BroadcastReceiver {
    private static final String TAG = "GeoFenceListener";
   private NotificationHelper notificationHelper = NotificationHelper.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }
        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

        // Test that the reported transition was of interest.
        switch (geofenceTransition) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                for (Geofence geofence : triggeringGeofences) {
                    notificationHelper.updateNotification(notificationHelper.createNotification("You have entered a destination",
                            "Current Destination " + geofence.getRequestId()),NotificationHelper.NOTIFICATION_ID);
                }

                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                for (Geofence geofence : triggeringGeofences) {
                    notificationHelper.updateNotification(notificationHelper.createNotification("You have exited a destination",
                            "Current Destination " + geofence.getRequestId()),NotificationHelper.NOTIFICATION_ID);
                }
                break;


        }



    }

}
