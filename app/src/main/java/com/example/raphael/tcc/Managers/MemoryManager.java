package com.example.raphael.tcc.Managers;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by rapha on 17-Sep-16.
 */
public class MemoryManager  {
    private ActivityManager am;
    public long memoryAvailable(Context context){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.getMemoryInfo(mi);
        return mi.availMem / 1048576L;
    }
}
