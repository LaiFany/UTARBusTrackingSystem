package com.example.utarbustrackingsystemapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by kundan on 10/22/2015.
 */
public class PushNotificationService extends GcmListenerService{

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        System.out.println("received push notification");

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        generateNotification(this, message);

        //Release the lock
        wl.release();
    }

    private static void generateNotification(Context context, String message) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Bus Update");
        mBuilder.setContentText(message);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        Intent resultIntent = new Intent(context, HomeActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }
}
