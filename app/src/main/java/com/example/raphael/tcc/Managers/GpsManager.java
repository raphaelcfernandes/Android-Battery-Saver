package com.example.raphael.tcc.Managers;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Raphael on 20-Apr-16.
 */
public class GpsManager {
    private LocationManager manager;
    public int getStatusGps(Context context){
        manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return 1;
        else
            return 0;
    }
}
