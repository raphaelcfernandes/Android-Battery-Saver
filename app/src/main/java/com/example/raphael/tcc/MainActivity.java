package com.example.raphael.tcc;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity{
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    BattManager b1;
    CpuManager pR;
    File file;
    String teste="myfile";
    StringBuilder teste3 = new StringBuilder();
    FileOutputStream outputStream;
    GpsManager gpsManager;
    BluetoothManager blueT;
    NetworkManager networkManager;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Button Up = (Button) findViewById(R.id.button);
        b1 = new BattManager();
        pR = new CpuManager();
        gpsManager = new GpsManager();
        blueT = new BluetoothManager();
        networkManager = new NetworkManager();
    }
    public void onResume(){
        super.onResume();
        methodCalls();
    }
    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    public void onDestroy(){
        super.onDestroy();
        android.os.Debug.stopMethodTracing();
    }

    private void methodCalls(){
        Button bgElement = (Button) findViewById(R.id.button);
        bgElement.setBackgroundColor(Color.GREEN);
        teste3.delete(0, teste3.length());
        //teste3 = new StringBuilder(pR.getCores());
        /*teste3.append("\nGPS STATUS: " + manager.getStatusGps(this.getApplicationContext())
                + "\nPorcentagem BATERIA: " + b1.getBatteryStatus() + "%\nBluetooth: " + blueT.getBluetoothStatus()
                + "\nNetwork utilizada: " + get_network() + "\nMemoria livre: " + memoryAvailable() + " Mbs");*/
        System.out.println(pR.getNumberOfCores());
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
