package com.example.raphael.tcc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.raphael.tcc.Managers.AppManager;
import com.example.raphael.tcc.Managers.BrightnessManager;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BackgroundService extends Service {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public static final String CUSTOM_INTENT="com.example.raphael.tcc";
    private BrightnessManager brightnessManager = new BrightnessManager();
    private AppManager appManager = new AppManager();
    private FeedbackButton feedbackButton = new FeedbackButton();
    String s;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service Started", Toast.LENGTH_LONG).show();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                s=appManager.getAppRunningForeground();
                System.out.println(s);
                if(s.equals("com.android.vending")){
                    Intent intent = new Intent(CUSTOM_INTENT);
                    sendBroadcast(intent);
                    brightnessManager.setBrightnessLevel(50);
                }
            }
        },1,2,SECONDS);
        return START_STICKY;
    }


    @Override
    public void onCreate(){
        super.onCreate();
        feedbackButton.createFeedBackButton(getApplicationContext());
   }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service stoped", Toast.LENGTH_LONG).show();
        stopSelf();
        feedbackButton.removeView();
    }
}