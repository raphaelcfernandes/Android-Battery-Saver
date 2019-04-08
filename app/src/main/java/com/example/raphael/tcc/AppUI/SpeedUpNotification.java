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

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

/*
    Android Battery Saver
    This class will handle the notification pop up to increase speed.
    baw76
 */
public class SpeedUpNotification
{
    private Intent speedUpIntent = new Intent("com.example.raphael.tcc.REQUESTED_MORE_CPU");
    private static final int SPEEDUPNOTIFICATION_ID = 1; //Identifier for this specific notification

    //This method creates the notification when the app is started
    public void createSpeedUpNotification(Context context)
    {
        //Notification sends this on press
        PendingIntent pendingSpeedUpIntent = PendingIntent.getBroadcast(context, 0, speedUpIntent, 0);

        NotificationCompat.Builder speedUpBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_speedup_image) //Notification image
                .setContentTitle("CPU Speed Manager") //Title of notification
                .setContentText("Tap this notification to increase speed.") //Small text under title
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) //Default priority setting(subject to change)
                .setContentIntent(pendingSpeedUpIntent) //Will pass this when pressed
                .addAction(R.drawable.ic_speedup_image,"speedup",pendingSpeedUpIntent)
                .setOngoing(true); // Makes the notification stay after it's pressed

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(SPEEDUPNOTIFICATION_ID, speedUpBuilder.build());
    }

    //This method will remove the notification from the bar when the app is killed
    public void removeNotification(Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        try
        {
            notificationManager.cancel(SPEEDUPNOTIFICATION_ID); //Cancel is how NM removes the notification
        }
        catch(NullPointerException e)
        {

        }
    }
}
