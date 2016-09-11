package com.example.raphael.tcc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by rapha on 11-Sep-16.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getAction().toString());
        if (intent.getAction().equals(BackgroundService.CUSTOM_INTENT)) {
            System.out.println("recebi");
        }
    }
}
