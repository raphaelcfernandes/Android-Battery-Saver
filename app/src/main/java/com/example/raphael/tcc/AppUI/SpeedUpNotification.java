package com.example.raphael.tcc.AppUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.content.Intent;

import com.example.raphael.tcc.R;

/*
    Android Battery Saver
    This class will handle the notification pop up to increase speed.
    baw76
 */
public class SpeedUpNotification
{
    private Intent speedUpIntent = new Intent("com.example.raphael.tcc.REQUESTED_MORE_CPU");
    private static final int SPEEDUPNOTIFICATION_ID = 1;

    public void createSpeedUpNotification(Context context)
    {
        PendingIntent pendingSpeedUpIntent = PendingIntent.getActivity(context, 0, speedUpIntent, 0);

        NotificationCompat.Builder speedUpBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_speedup_image)
                .setContentTitle("CPU Speed Manager")
                .setContentText("Tap this notification to increase speed.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingSpeedUpIntent)
                .setOngoing(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(SPEEDUPNOTIFICATION_ID, speedUpBuilder.build());
    }

    public void removeNotification(Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(SPEEDUPNOTIFICATION_ID);
    }
}
