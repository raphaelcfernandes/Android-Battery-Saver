package com.example.raphael.tcc;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity{
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private int minInterval = 5000;
    private Handler mHandler;
    BatteryManager b1;
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
        b1 = new BatteryManager();
        pR = new CpuManager();
        gpsManager = new GpsManager();
        blueT = new BluetoothManager();
        networkManager = new NetworkManager();
        mHandler = new Handler();
    }
    public void onResume(){
        super.onResume();
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                methodCalls();
            }
        },0,3000);
    }
    public void sendMessage(View view){
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        //startActivity(intent);
    }

    public void onDestroy(){
        super.onDestroy();
        android.os.Debug.stopMethodTracing();
    }

    private void methodCalls(){
        //teste3.delete(0, teste3.length());
        //teste3 = new StringBuilder(pR.getCores());
        /*teste3.append("\nGPS STATUS: " + manager.getStatusGps(this.getApplicationContext())
                + "\nPorcentagem BATERIA: " + b1.getBatteryStatus() + "%\nBluetooth: " + blueT.getBluetoothStatus()
                + "\nNetwork utilizada: " + get_network() + "\nMemoria livre: " + memoryAvailable() + " Mbs");*/
        System.out.println(pR.getCoreUtilization(0));
        /*file = new File(this.getApplicationContext().getFilesDir(), teste);
        try {
            outputStream = openFileOutput(teste, Context.MODE_PRIVATE);
            outputStream.write(teste3.toString().getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private long memoryAvailable(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        return availableMegs;
    }
}
