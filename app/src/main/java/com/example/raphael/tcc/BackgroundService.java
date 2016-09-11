package com.example.raphael.tcc;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BackgroundService extends Service {
    private Context context;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public static final String CUSTOM_INTENT="com.example.raphael.tcc";
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
                s=getAppRunningForeground();
                System.out.println(s);
                if(s.equals("com.android.vending")){
                    Intent intent = new Intent(CUSTOM_INTENT);
                    sendBroadcast(intent);
                }
            }
        },1,2,SECONDS);
        return START_STICKY;
    }
    private static String read(String path) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        output.append(reader.readLine());
        for (String line = reader.readLine(); line != null; line = reader.readLine())
            output.append('\n').append(line);
        reader.close();
        return output.toString();
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    public static String getAppRunningForeground(){
        int pid;
        File[] files = new File("/proc").listFiles();
        int lowestOomScore = Integer.MAX_VALUE;
        String foregroundProcess = null;
        for (File file : files) {
            if (!file.isDirectory() || (!file.getName().matches(("\\d+"))))
                continue;
            pid = Integer.parseInt(file.getName());
            try {
                String cgroup = read(String.format("/proc/%d/cgroup", pid));
                String[] lines = cgroup.split("\n");
                if (lines.length != 2)
                    continue;
                String cpuSubsystem = lines[0];
                String cpuaccctSubsystem = lines[1];
                if (!cpuaccctSubsystem.endsWith(Integer.toString(pid)) || cpuSubsystem.endsWith("bg_non_interactive"))
                    continue;
                String cmdline = read(String.format("/proc/%d/cmdline", pid));
                if (cmdline.contains("com.android.systemui")) {
                    continue;
                }
                int uid = Integer.parseInt(cpuaccctSubsystem.split(":")[2].split("/")[1].replace("uid_", ""));
                if (uid >= 1000 && uid <= 1038)//System process
                    continue;
                File oomScoreAdj = new File(String.format("/proc/%d/oom_score_adj", pid));
                if (oomScoreAdj.canRead()) {
                    int oomAdj = Integer.parseInt(read(oomScoreAdj.getAbsolutePath()));
                    if (oomAdj != 0) {
                        continue;
                    }
                }
                int oomscore = Integer.parseInt(read(String.format("/proc/%d/oom_score", pid)));
                if (oomscore < lowestOomScore) {
                    lowestOomScore = oomscore;
                    foregroundProcess = cmdline.replaceAll("\\p{Cntrl}", "");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return foregroundProcess;
    }

}