package com.example.raphael.tcc.BackgroundServices;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import com.example.raphael.tcc.AppUI.BubbleButton;
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
    private BubbleButton bubbleButton = new BubbleButton();
    private AppManager appManager = new AppManager();
    private BrightnessManager brightnessManager = new BrightnessManager();
    private CpuManager object = SingletonClasses.getInstance();
    private AppDbHelper appDbHelper = new AppDbHelper(BackgroundService.this);
    private int teste=0;
    /**
     * Variables
     */
    ArrayList<String> arrayList = new ArrayList<>();
    private static boolean loaded=true,changeDetector=false,firstTimeOnSystem=false,screenOnOff=true, loadLastAppOnScreenOnOff = false;
    private static String actualApp,lastApp="";
    private static int brightnessValue;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(screenOnOff) {
                    loadLastAppOnScreenOnOff=true;//Recarregar last app
                    actualApp = appManager.getAppRunningOnForeground(BackgroundService.this);
                    System.out.println("to aqui");
                    if(actualApp.equals("com.android.launcher") || actualApp.equals("com.google.android.googlequicksearchbox"))
                        teste++;
                    if(teste>=5){
                        arrayList.clear();
                        setAppConfiguration(arrayList);
                        teste=0;
                        lastApp="";
                    }
                    if (!actualApp.equals("com.android.launcher") && !actualApp.equals("com.google.android.googlequicksearchbox") && !actualApp.equals(lastApp) && !actualApp.equals("com.example.raphael.tcc")
                            && !actualApp.equals("com.android.systemui") && !actualApp.equals("android") && !actualApp.isEmpty())
                        loaded = false;
                    if (!loaded) {//Retrieve app info from DB
                        //carregar actualApp
                        arrayList = appDbHelper.getAppData(CpuManager.getNumberOfCores(), actualApp);
                        if(!arrayList.isEmpty())
                            brightnessValue = Integer.parseInt(arrayList.get(1));
                        if (!actualApp.equals(lastApp) && !lastApp.equals("")) {
                            if (changeDetector)
                                appDbHelper.updateAppConfiguration(lastApp, brightnessManager.getScreenBrightnessLevel(), object.getArrayListCoresSpeed());
                            else if (firstTimeOnSystem)
                                appDbHelper.insertAppConfiguration(lastApp, brightnessManager.getScreenBrightnessLevel(), object.getArrayListCoresSpeed());
                        }
                        setAppConfiguration(arrayList);
                        loaded = true;
                        lastApp = actualApp;
                        changeDetector = false;
                    }
                }
                else if(loadLastAppOnScreenOnOff){//Quando a tela desligar, coloque no minimo. Mas adjustConfig estará setando para length/2
                    loadLastAppOnScreenOnOff=false;
                    if(arrayList.isEmpty())
                        appDbHelper.updateAppConfiguration(actualApp, BrightnessManager.minLevel, object.getArrayListCoresSpeed());
                    else if(changeDetector || firstTimeOnSystem)
                        appDbHelper.updateAppConfiguration(actualApp, brightnessValue, object.getArrayListCoresSpeed());
                    loaded=false;//recarregar config
                    arrayList.clear();
                    object.adjustConfiguration(arrayList);
                }
            }
        },1,1,SECONDS);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        bubbleButton.createFeedBackButton(getApplicationContext());
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction("com.example.raphael.tcc.REQUESTED_MORE_CPU");
        registerReceiver(broadcastRcv, filter);

   }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service stoped", Toast.LENGTH_LONG).show();
        unregisterReceiver(broadcastRcv);
        scheduler.shutdown();
        object.giveAndroidFullControl();
        stopService(new Intent(this,BackgroundService.class));
        bubbleButton.removeView();
    }

    private void setAppConfiguration(ArrayList<String> appConfiguration){
        //Empty ArrayList? No records found -> set to minimum
        object.adjustConfiguration(appConfiguration);
        if(appConfiguration.size()!=0) {
            brightnessManager.setBrightnessLevel(Integer.parseInt(appConfiguration.get(1)));
            firstTimeOnSystem=false;
        }
        else {
            brightnessManager.setBrightnessLevel(BrightnessManager.minLevel);
            firstTimeOnSystem = true;
        }
    }

    private final BroadcastReceiver broadcastRcv = new BroadcastReceiver (){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int value;
            if(action.equals("com.example.raphael.tcc.REQUESTED_MORE_CPU")){
                value = intent.getIntExtra("valorCpuUsuario",0);
                object.setCpuSpeedFromUserInput(value);
                changeDetector=true;
            }
            if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                //changeDetector = false;
                screenOnOff=false;
            }
            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
                screenOnOff = true;
        }
    };
}
