package com.spcba.bpass.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.spcba.bpass.R;

public class NotificationHelper {
    private static NotificationHelper instance;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    public static final int NOTIFICATION_ID = 23;
    public static final int ALARM_NOTIFICATION_ID = 24;
    private String CHANNEL_ID = "BPassChannelId";

    public static NotificationHelper getInstance() {
        if (instance == null)
            instance = new NotificationHelper();
        return instance;
    }

    public void createNotificationManager(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "BPass Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
            notificationManager.createNotificationChannel(channel);
            notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        }
    }

    public Notification createNotification( String title, String content) {
        return notificationBuilder
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_destination)
                .setContentText(content).build();

    }

    public void updateNotification(Notification notification,int id) {
        notificationManager.notify(id, notification);

    }
    public void dismissNotification(int notifId){
        notificationManager.cancel(notifId);

    }

}
