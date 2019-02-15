package com.example.raphael.tcc.AppUI;

import android.support.v4.app.NotificationCompat;
import android.content.Intent;

import com.example.raphael.tcc.R;

/*
    Android Battery Saver
    This class will handle the notification pop up to increase speed.

 */
public class SpeedUpNotification
{
    Intent speedUpIntent = new Intent(this, AlertDetails.class);
            speedUpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder()
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("CPU Speed Manager")
            .setContentText("Tap the notification to increase speed.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent()
            .setOngoing(true);
}
