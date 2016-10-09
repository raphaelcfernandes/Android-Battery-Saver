package com.example.raphael.tcc.BackgroundServices;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import com.example.raphael.tcc.AppUI.BubbleButton;
import com.example.raphael.tcc.Managers.CpuManager;
import com.example.raphael.tcc.Managers.AppManager;
import com.example.raphael.tcc.ReadWriteFile;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BackgroundService extends Service {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private BubbleButton bubbleButton = new BubbleButton();
    private AppManager appManager = new AppManager();
    private CpuManager cpuManager = new CpuManager();
    String s;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        Toast.makeText(this,"Service Started", Toast.LENGTH_LONG).show();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                s= appManager.getAppRunningOnForeground(BackgroundService.this);
                System.out.println(s);
                if(s.equals("com.android.vending"))
                    bubbleButton.removeView();
            }
        },1,2,SECONDS);
        return START_STICKY;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        bubbleButton.createFeedBackButton(getApplicationContext());
        BroadcastReceiver buttonClicked = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                ReadWriteFile.createFile(arg0);
            }
        };
        registerReceiver(buttonClicked, new IntentFilter("com.example.raphael.tcc.REQUESTED_MORE_CPU"));
   }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service stoped", Toast.LENGTH_LONG).show();
        stopSelf();
        bubbleButton.removeView();
    }
}