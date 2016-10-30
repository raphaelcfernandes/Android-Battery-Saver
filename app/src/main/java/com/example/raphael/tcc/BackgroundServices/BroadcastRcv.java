package com.example.raphael.tcc.BackgroundServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.raphael.tcc.Managers.CpuManager;
import com.example.raphael.tcc.SingletonClasses;

/**
 * Created by rapha on 27-Oct-16.
 */

public class BroadcastRcv extends BroadcastReceiver {
    CpuManager object = SingletonClasses.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int value = 0;
        if(action.equals("com.example.raphael.tcc.REQUESTED_MORE_CPU")){
            value = intent.getIntExtra("valorCpuUsuario",0);
            object.setCpuSpeedFromUserInput(value);
        }
    }
}
