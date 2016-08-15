package com.example.raphael.tcc;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MainActivity extends Activity{
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    File file;
    String teste="myfile";
    StringBuilder teste3 = new StringBuilder();
    FileOutputStream outputStream;
    BatteryManager b1 = new BatteryManager();
    CpuManager pR = new CpuManager();
    GpsManager gpsManager = new GpsManager();
    BluetoothManager blueT = new BluetoothManager();
    NetworkManager networkManager = new NetworkManager();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onResume(){
        super.onResume();
        periodicallyCheck();
    }

    public void periodicallyCheck() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                methodCalls();
            }
        },1,5,SECONDS);
    }


    public void onDestroy(){
        super.onDestroy();
        android.os.Debug.stopMethodTracing();
    }

    private void methodCalls(){
        System.out.println("GPS: "+gpsManager.getStatusGps(this.getApplicationContext()));
        System.out.println("Nivel de bateria: "+b1.getBatteryStatus(this.getApplicationContext()));
        System.out.println("Bluetooth: "+blueT.getBluetoothStatus());
        System.out.println("NetWork: "+networkManager.get_network(this.getApplicationContext()));
        for(int i=0;i<4;++i) {
            if (pR.isCoreOnline(i)) {
                System.out.println("Core "+i+": "+"online");
                System.out.println("Governor: "+pR.getGovernorOfCore(i));
                System.out.println("Speed: "+pR.getSpeedOfCore(i));
                System.out.println("Utilization: "+pR.getCoreUtilization(i));
            }
            else
                System.out.println("Core "+i+": "+"offline");
        }
       //teste3.delete(0, teste3.length());
        //teste3 = new StringBuilder(pR.getCores());
        /*teste3.append("\nGPS STATUS: " + manager.getStatusGps(this.getApplicationContext())
                + "\nPorcentagem BATERIA: " + b1.getBatteryStatus() + "%\nBluetooth: " + blueT.getBluetoothStatus()
                + "\nNetwork utilizada: " + get_network() + "\nMemoria livre: " + memoryAvailable() + " Mbs");*/
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
