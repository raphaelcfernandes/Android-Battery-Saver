package com.example.raphael.tcc.AppUI;

import android.support.v4.app.NotificationCompat;

import com.example.raphael.tcc.R;

public class SpeedUpNotification
{
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("CPU Speed Manager")
            .setContentText("Tap the notification to increase speed.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);
}
