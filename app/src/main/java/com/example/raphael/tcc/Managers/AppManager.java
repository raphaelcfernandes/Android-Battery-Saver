package com.example.raphael.tcc.Managers;

/**
 * Created by rapha on 28-Sep-16.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by rapha on 28-Sep-16.
 */

public class AppManager {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");

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
        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        return stats;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressWarnings("ResourceType")
    private UsageStatsManager getUsageStatsManager(Context context) {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        return usm;
    }
    private String getProcessName(List<UsageStats> stats) {
        String foregroundProcess = "";
            if(stats != null) {
                SortedMap<Long,UsageStats> mySortedMap = new TreeMap<Long,UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
                }
                if(mySortedMap != null && !mySortedMap.isEmpty()) {
                    String topPackageName =  mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    foregroundProcess = topPackageName;
                }
            }
        return foregroundProcess;
    }
}