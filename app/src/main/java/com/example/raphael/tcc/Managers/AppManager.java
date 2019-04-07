package com.example.raphael.tcc.Managers;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by raphael on 28-Sep-16.
 */

public class AppManager {

    public String getAppRunningOnForeground(Context context) {
        return getProcessName(getUsageStatsList(context));
    }

    // To check the USAGE_STATS_SERVICE permission
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public List<UsageStats> getUsageStatsList(Context context) {
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.MINUTE, -1);
        long startTime = calendar.getTimeInMillis();
        return usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressWarnings("ResourceType")
    private UsageStatsManager getUsageStatsManager(Context context) {
        return (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }
    @TargetApi(Build.VERSION_CODES.M)
    private String getProcessName(List<UsageStats> stats) {
        String foregroundProcess = "";
            if(stats != null) {
                SortedMap<Long,UsageStats> mySortedMap = new TreeMap<>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
                }
                if(!mySortedMap.isEmpty()) {
                    foregroundProcess = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        return foregroundProcess;
    }

}