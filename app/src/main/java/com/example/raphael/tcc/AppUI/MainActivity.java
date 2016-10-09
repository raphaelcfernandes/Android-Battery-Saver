package com.example.raphael.tcc.AppUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.raphael.tcc.BackgroundServices.BackgroundService;
import com.example.raphael.tcc.DataBase.AppDbHelper;
import com.example.raphael.tcc.Managers.AppManager;
import com.example.raphael.tcc.Managers.CpuManager;
import com.example.raphael.tcc.R;

import java.util.ArrayList;

public class MainActivity extends Activity {
    AppDbHelper appDbHelper = new AppDbHelper(MainActivity.this);
    AppManager appManager = new AppManager();
    ArrayList<String> arrayList = new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if (appManager.getUsageStatsList(this).isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        startService(new Intent(getBaseContext(),BackgroundService.class));
        appDbHelper.insertAppConfiguration("TESTE",150,153,1500,132,15232);
        arrayList = appDbHelper.getAppData(cpuManager.getNumberOfCores(),"TESTE");
        for(String a : arrayList)
            System.out.println(a);
        appDbHelper.updateAppConfiguration("TESTE",1323,1,1,1,1);
        arrayList = appDbHelper.getAppData(cpuManager.getNumberOfCores(),"TESTE");
        for(String a : arrayList)
            System.out.println(a);*/

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
