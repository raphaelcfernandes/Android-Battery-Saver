package com.example.raphael.tcc.Managers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Raphael on 08-Apr-16.
 */
public class BatteryManager {
    private float atributo;
    public float getBatteryStatus(Context context){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.getApplicationContext().registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(android.os.BatteryManager.EXTRA_SCALE,-1);
        this.atributo=(status/(float)scale)*100;
        return this.atributo;
    }
}
//Está funcionando 100%