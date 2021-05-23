package com.spcba.bpass.ui.recievers;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.spcba.bpass.helper.NotificationHelper;

public class AlarmReceiver extends BroadcastReceiver {
    NotificationHelper notificationHelper = NotificationHelper.getInstance();
    private static final String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Alarm Triggered");
        notificationHelper.createNotificationManager(context);
        
       Notification notification=  notificationHelper.createNotification("BPass Reminder",intent.getStringExtra("AlarmMessage"));
        notificationHelper.updateNotification(notification,NotificationHelper.ALARM_NOTIFICATION_ID);


    }
}
