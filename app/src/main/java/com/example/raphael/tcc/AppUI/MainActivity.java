package com.example.raphael.tcc.AppUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.raphael.tcc.BackgroundServices.BackgroundService;
import com.example.raphael.tcc.Managers.UsageStatus;
import com.example.raphael.tcc.R;

public class MainActivity extends Activity {
    UsageStatus usageStatus = new UsageStatus();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (usageStatus.getUsageStatsList(this).isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

        startService(new Intent(getBaseContext(),BackgroundService.class));
    }

    public void onResume(){
        super.onResume();
    }

    public void startService(View view){
        startService(new Intent(this,BackgroundService.class));
    }
    public void stopService(View view){
        stopService(new Intent(this,BackgroundService.class));
    }

    public void onDestroy(){
        super.onDestroy();
        android.os.Debug.stopMethodTracing();
    }


}
