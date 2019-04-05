package com.example.raphael.tcc.BackgroundServices;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.raphael.tcc.AppUI.BubbleButton;
import com.example.raphael.tcc.AppUI.SpeedUpNotification;
import com.example.raphael.tcc.DataBase.AppDbHelper;
import com.example.raphael.tcc.Managers.AppManager;
import com.example.raphael.tcc.Managers.BrightnessManager;
import com.example.raphael.tcc.Managers.CpuManager;
import com.example.raphael.tcc.SingletonClasses;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BackgroundService extends Service {
    /**
     * Objects
     */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final BubbleButton bubbleButton = new BubbleButton();
    private final SpeedUpNotification speedUpNotification = new SpeedUpNotification(); //Create notification handler
    private AppManager appManager = new AppManager();
    private BrightnessManager brightnessManager = new BrightnessManager();
    private CpuManager cpuManager = SingletonClasses.getInstance();
    private AppDbHelper appDbHelper = new AppDbHelper(BackgroundService.this);
    private int timer = 0;
    /**
     * Variables
     */
    private ArrayList<String> arrayList = new ArrayList<>();
    private static boolean loaded = true, changeDetector = false, firstTimeOnSystem = false, screenOnOff = true, loadLastAppOnScreenOnOff = false;
    private static String actualApp, lastApp = "";
    private static int brightnessValue;
    private boolean notifEnabled;
    private boolean buttonEnabled;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private ArrayList<Integer> currentspeeds;

    @Override
    //TODO I think the algorithm is not saving correctly the changes of the user when the screen turns off
    //Todo the cause may be that flag screenOnOff doesnt have a if case when it is False
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.i(this.getClass().getName(), "Running the thread");
                if (screenOnOff) {
                    loadLastAppOnScreenOnOff = true;//Reload last app
                    actualApp = appManager.getAppRunningOnForeground(BackgroundService.this);
                    if (actualApp.equals("com.android.launcher") || actualApp.equals("com.google.android.googlequicksearchbox"))
                        timer++;
                    if (timer >= 5) {
                        arrayList.clear();
                        setAppConfiguration(arrayList);
                        timer = 0;
                        lastApp = "";
                    }
                    if (!actualApp.equals("com.android.launcher") && !actualApp.equals("com.google.android.googlequicksearchbox") && !actualApp.equals(lastApp) && !actualApp.equals("com.example.raphael.tcc")
                            && !actualApp.equals("com.android.systemui") && !actualApp.equals("android") && !actualApp.isEmpty())
                        loaded = false;
                    if (!loaded) {//Retrieve app info from DB
                        //reload actualApp
                        arrayList = appDbHelper.getAppData(CpuManager.getNumberOfCores(), actualApp);
                        //If got the app from the database
                        if (!arrayList.isEmpty()) {
                            brightnessValue = Integer.parseInt(arrayList.get(1));
                            cpuManager.adjustConfiguration(arrayList);
                            //otherwise:
                        } else {
                            Log.i(this.getClass().getName(), "Did not get the app from db. ");
                            if (currentspeeds.size() > 0) {
                                for (int i = currentspeeds.size() - 1; i >= 0; i--) {
                                    if (currentspeeds.get(i) > 0) {
                                        currentspeeds.set(i, (int) Math.pow(currentspeeds.get(i), 0.95));
                                        // Set the core with highest number to 0.95 of its value
                                        Log.e(this.getClass().getName(), "current speed: " + i + ", " + currentspeeds.get(i));
                                        break;
                                    }
                                }
                                currentspeeds = cpuManager.setSpeedByArrayListDESC(currentspeeds);
                            }
                            //Set to highest speed
                            else {
                                cpuManager.adjustConfiguration(new ArrayList<String>());
                                currentspeeds = cpuManager.getArrayListCoresSpeed();
                            }
                        }
                        if (!actualApp.equals(lastApp) && !lastApp.equals("")) {
                            if (changeDetector) {
                                appDbHelper.updateAppConfiguration(lastApp, brightnessManager.getScreenBrightnessLevel(), cpuManager.getArrayListCoresSpeed());
                                currentspeeds = new ArrayList<>();
                            } else if (firstTimeOnSystem) {
                                appDbHelper.insertAppConfiguration(lastApp, brightnessManager.getScreenBrightnessLevel(), cpuManager.getArrayListCoresSpeed());
                                currentspeeds = new ArrayList<>();
                            }
                        }
                        //setAppConfiguration(arrayList);
                        loaded = false;
                        lastApp = actualApp;
                        changeDetector = false;
                    } else {
                        if (currentspeeds.size() > 0) {
                            for (int i = currentspeeds.size() - 1; i >= 0; i--) {
                                if (currentspeeds.get(i) > 0) {
                                    currentspeeds.set(i, (int) Math.pow(currentspeeds.get(i), 0.95));
                                    Log.e(this.getClass().getName(), "current speed: " + i + ", " + currentspeeds.get(i));
                                    break;
                                }
                            }
                            cpuManager.setSpeedByArrayListDESC(currentspeeds);
                        }
                    }
                } else if (loadLastAppOnScreenOnOff) {//When the screen turn off, put all config to min.
                    loadLastAppOnScreenOnOff = false;
                    if (arrayList.isEmpty())
                        appDbHelper.updateAppConfiguration(actualApp, BrightnessManager.minLevel, cpuManager.getArrayListCoresSpeed());
                    else if (changeDetector || firstTimeOnSystem)
                        appDbHelper.updateAppConfiguration(actualApp, brightnessValue, cpuManager.getArrayListCoresSpeed());
                    loaded = false;//reload config
                    arrayList.clear();
                    cpuManager.adjustConfiguration(arrayList);
                }
            }
        }, 1, 1, SECONDS);
        return START_NOT_STICKY;
    }

    //Once receive the request
    public void levelup() {
        currentspeeds.set(currentspeeds.size() - 1, (int) (currentspeeds.get(currentspeeds.size() - 1) * 1.10));
        cpuManager.setSpeedByArrayListDESC(currentspeeds);
        appDbHelper.updateAppConfiguration(actualApp, brightnessValue, cpuManager.getArrayListCoresSpeed());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //removed bubbleButton
        //bubbleButton.createFeedBackButton(getApplicationContext());
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction("com.example.raphael.tcc.REQUESTED_MORE_CPU");
        registerReceiver(broadcastRcv, filter);

        //Create the notification
        speedUpNotification.createSpeedUpNotification(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
        unregisterReceiver(broadcastRcv);
        scheduler.shutdown();
        cpuManager.giveAndroidFullControl();

        speedUpNotification.removeNotification(this);
        bubbleButton.removeView();
        stopService(new Intent(this, BackgroundService.class));
    }

    private void setAppConfiguration(ArrayList<String> appConfiguration) {
        //Empty ArrayList? No records found -> set to minimum
        cpuManager.adjustConfiguration(appConfiguration);
        if (appConfiguration.size() != 0) {
            brightnessManager.setBrightnessLevel(Integer.parseInt(appConfiguration.get(1)));
            firstTimeOnSystem = false;
        } else {
            brightnessManager.setBrightnessLevel(BrightnessManager.minLevel);
            firstTimeOnSystem = true;
        }
    }

    private final BroadcastReceiver broadcastRcv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int value;
            if (action.equals("com.example.raphael.tcc.REQUESTED_MORE_CPU")) {
                value = intent.getIntExtra("valorCpuUsuario", 0);
                levelup();
                changeDetector = true;
            }
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                //changeDetector = false;
                screenOnOff = false;
            }
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
                screenOnOff = true;

            if (intent.getAction().equals("com.example.raphael.tcc.ENABLE_BUTTON"))
                bubbleButton.createFeedBackButton(getApplicationContext());

            if (intent.getAction().equals("com.example.raphael.tcc.DISABLE_BUTTON"))
                bubbleButton.removeView();

            if (intent.getAction().equals("com.example.raphael.tcc.ENABLE_NOTIFICATION"))
                speedUpNotification.createSpeedUpNotification(getApplicationContext());

            if (intent.getAction().equals("com.example.raphael.tcc.DISABLE_NOTIFICATION"))
                speedUpNotification.removeNotification(getApplicationContext());
        }
    };
}

