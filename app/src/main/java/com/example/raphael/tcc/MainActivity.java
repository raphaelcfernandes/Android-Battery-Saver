package com.example.raphael.tcc;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity{
    BattManager b1;
    ProcessesRunning pR;
    File file;
    String teste="myfile";
    StringBuilder teste3 = new StringBuilder();
    FileOutputStream outputStream;
    GpsManager manager;
    BluetoothManager blueT;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Up = (Button) findViewById(R.id.button);
        b1 = new BattManager();
        pR = new ProcessesRunning();
        manager = new GpsManager();
        blueT = new BluetoothManager();
        b1.setBatteryStatus(this.getApplicationContext());
        Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodCalls();
            }
        });
    }
    private void methodCalls(){
        Button bgElement = (Button) findViewById(R.id.button);
        bgElement.setBackgroundColor(Color.GREEN);
        teste3.delete(0, teste3.length());
        teste3 = new StringBuilder(pR.getCores());
        /*teste3.append("\nGPS STATUS: " + manager.getStatusGps(this.getApplicationContext())
                + "\nPorcentagem BATERIA: " + b1.getBatteryStatus() + "%\nBluetooth: " + blueT.getBluetoothStatus()
                + "\nNetwork utilizada: " + get_network() + "\nMemoria livre: " + memoryAvailable() + " Mbs");*/
        file = new File(this.getApplicationContext().getFilesDir(), teste);
        try {
            outputStream = openFileOutput(teste, Context.MODE_PRIVATE);
            outputStream.write(teste3.toString().getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long memoryAvailable(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        return availableMegs;
    }
}
