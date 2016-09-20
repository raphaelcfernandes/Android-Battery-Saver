package com.example.raphael.tcc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends Activity {
    File file;
   // String feedBackPopUpWindow="myfile";
    StringBuilder teste3 = new StringBuilder();
    FileOutputStream outputStream;
    private BroadcastReceiver receiver;
    private FeedBackPopUpWindow feedBackPopUpWindow = new FeedBackPopUpWindow();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter("com.example.raphael.tcc");
        startService(new Intent(getBaseContext(),BackgroundService.class));
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
              // System.out.println("Recebi o comando, to na main");
            }
        };
        registerReceiver(receiver,filter);
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
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
        android.os.Debug.stopMethodTracing();
    }

    private void methodCalls(){
       //teste3.delete(0, teste3.length());
        //teste3 = new StringBuilder(pR.getCores());
        /*teste3.append("\nGPS STATUS: " + manager.getStatusGps(this.getApplicationContext())
                + "\nPorcentagem BATERIA: " + b1.getBatteryStatus() + "%\nBluetooth: " + blueT.getBluetoothStatus()
                + "\nNetwork utilizada: " + get_network() + "\nMemoria livre: " + memoryAvailable() + " Mbs");*/
        /*file = new File(this.getApplicationContext().getFilesDir(), feedBackPopUpWindow);
        try {
            outputStream = openFileOutput(feedBackPopUpWindow, Context.MODE_PRIVATE);
            outputStream.write(teste3.toString().getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}
