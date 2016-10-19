package com.example.raphael.tcc.AppUI;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.raphael.tcc.BackgroundServices.BackgroundService;
import com.example.raphael.tcc.DataBase.AppDbHelper;
import com.example.raphael.tcc.Managers.AppManager;
import com.example.raphael.tcc.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    AppManager appManager = new AppManager();
    ArrayList<String> arrayList = new ArrayList<>();
    Button b1,b2;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button)findViewById(R.id.ativar);
        b2 = (Button)findViewById(R.id.desativar);

        if (appManager.getUsageStatsList(this).isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        b2.setEnabled(false);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                b1.setEnabled(false);
                b2.setEnabled(true);
                startService(new Intent(getBaseContext(),BackgroundService.class));
            }
        });
        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                b1.setEnabled(true);
                b2.setEnabled(false);
                stopService(new Intent(getBaseContext(),BackgroundService.class));
            }
        });
    }
    public void onResume(){
        super.onResume();
        /*final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
        UsageStatsManager sm = (UsageStatsManager) getUsageStatsManager(MainActivity.this);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.MINUTE, -1);
        long startTime = calendar.getTimeInMillis();
        System.out.println(dateFormat.format(startTime) );
        System.out.println(dateFormat.format(endTime));

        List<UsageStats> lUsageStatsList =
                sm.queryUsageStats(
                        UsageStatsManager.INTERVAL_DAILY,
                        System.currentTimeMillis()- TimeUnit.DAYS.toMillis(1),
                        System.currentTimeMillis()+ TimeUnit.DAYS.toMillis(1));*/
        //System.out.println(getProcessName());

    }
    private String getProcessName() {
        String foregroundProcess = "";
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        // Process running
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*10, time);
            // Sort the stats by the last time used
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
        }
        return foregroundProcess;
    }
    private static UsageStatsManager getUsageStatsManager(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        return usm;
    }
    public void onDestroy(){
        super.onDestroy();
        android.os.Debug.stopMethodTracing();
    }


}
