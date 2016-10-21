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
import com.example.raphael.tcc.Managers.BrightnessManager;
import com.example.raphael.tcc.Managers.CpuManager;
import com.example.raphael.tcc.Managers.AppManager;
import com.example.raphael.tcc.ReadWriteFile;
import com.example.raphael.tcc.SingletonClasses;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BackgroundService extends Service {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private BubbleButton bubbleButton = new BubbleButton();
    private AppManager appManager = new AppManager();
    private BrightnessManager brightnessManager = new BrightnessManager();
    CpuManager object = SingletonClasses.getInstance();
    private BroadcastReceiver buttonClicked;
    private boolean loaded=false;
    ArrayList<String> arrayList = new ArrayList<>();
    private AppDbHelper appDbHelper = new AppDbHelper(BackgroundService.this);
    private String actualApp,lastApp;
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
                actualApp=appManager.getAppRunningOnForeground(BackgroundService.this);
                if(!loaded){//Retrieve app info from DB
                    //carregar actualApp
                    arrayList = appDbHelper.getAppData(CpuManager.getNumberOfCores(),actualApp);
                    setAppConfiguration(arrayList);
                    loaded=true;
                    lastApp = actualApp;
                }
                if(!actualApp.equals(lastApp)){//Changed apps
                    if(arrayList.size()==0)//Salvar
                        appDbHelper.insertAppConfiguration(actualApp,brightnessManager.getScreenBrightnessLevel(),object.getCoresSpeed());
                    else//Update
                        appDbHelper.updateAppConfiguration(actualApp,brightnessManager.getScreenBrightnessLevel(),object.getCoresSpeed());
                    loaded=false;
                }
            }
        },1,2,SECONDS);
        return START_STICKY;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        bubbleButton.createFeedBackButton(getApplicationContext());
        buttonClicked = new BroadcastReceiver() {
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
        unregisterReceiver(buttonClicked);
        stopService(new Intent(this,BackgroundService.class));
        bubbleButton.removeView();
    }

    private void setAppConfiguration(ArrayList<String> appConfiguration){
        //Empty ArrayList? No records found -> set to minimum
        if(appConfiguration.size()==0){
            object.setConfigurationToMinimum();
        }
        //ArrayList with elements -> load them
        else{
            object.adjustConfiguration(appConfiguration);
            brightnessManager.setBrightnessLevel(Integer.parseInt(appConfiguration.get(1)));
        }
    }

}
/*appDbHelper.insertAppConfiguration("TESTE",150,153,1500,132,15232);
        arrayList = appDbHelper.getAppData(cpuManager.getNumberOfCores(),"TESTE");
        for(String a : arrayList)
            System.out.println(a);
        appDbHelper.updateAppConfiguration("TESTE",1323,1,1,1,1);
        arrayList = appDbHelper.getAppData(cpuManager.getNumberOfCores(),"TESTE");
        for(String a : arrayList)
            System.out.println(a);*/