package com.example.raphael.tcc;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {
    private Context context;
    private boolean isRunning;
    /** first app user */
    public static final int AID_APP = 10000;

    /** offset for uid ranges for each user */
    public static final int AID_USER = 100000;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service Started", Toast.LENGTH_LONG).show();

        Timer timer  =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                /*try {
                    Process mLogcatProc = null;
                    BufferedReader reader = null;
                    mLogcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
                    String line;
                    reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));

                    final StringBuilder log = new StringBuilder();
                    String separator = System.getProperty("line.separator");
                    while ((line = reader.readLine()) != null)
                    {
                        log.append(line);
                        log.append(separator);
                    }
                    String w = log.toString();
                    Toast.makeText(getApplicationContext(),w, Toast.LENGTH_LONG).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }*/
                    File[] files = new File("/proc").listFiles();
                    int lowestOomScore = Integer.MAX_VALUE;
                    String foregroundProcess = null;

                    for (File file : files) {
                        if (!file.isDirectory()) {
                            continue;
                        }

                        int pid;
                        try {
                            pid = Integer.parseInt(file.getName());
                        } catch (NumberFormatException e) {
                            continue;
                        }

                        try {
                            String cgroup = read(String.format("/proc/%d/cgroup", pid));

                            String[] lines = cgroup.split("\n");

                            if (lines.length != 2) {
                                continue;
                            }

                            String cpuSubsystem = lines[0];
                            String cpuaccctSubsystem = lines[1];

                            if (!cpuaccctSubsystem.endsWith(Integer.toString(pid))) {
                                // not an application process
                                continue;
                            }

                            if (cpuSubsystem.endsWith("bg_non_interactive")) {
                                // background policy
                                continue;
                            }

                            String cmdline = read(String.format("/proc/%d/cmdline", pid));

                            if (cmdline.contains("com.android.systemui")) {
                                continue;
                            }

                            int uid = Integer.parseInt(
                                    cpuaccctSubsystem.split(":")[2].split("/")[1].replace("uid_", ""));
                            if (uid >= 1000 && uid <= 1038) {
                                // system process
                                continue;
                            }

                            int appId = uid - AID_APP;
                            int userId = 0;
                            // loop until we get the correct user id.
                            // 100000 is the offset for each user.
                            while (appId > AID_USER) {
                                appId -= AID_USER;
                                userId++;
                            }

                            if (appId < 0) {
                                continue;
                            }

                            // u{user_id}_a{app_id} is used on API 17+ for multiple user account support.
                            // String uidName = String.format("u%d_a%d", userId, appId);

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
                                foregroundProcess = cmdline;
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println(foregroundProcess);
            }
        },2000,3000);
        return START_STICKY;
    }
    private static String read(String path) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        output.append(reader.readLine());
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            output.append('\n').append(line);
        }
        reader.close();
        return output.toString();
    }
    @Override
    public void onCreate(){
        this.context=context;
        this.isRunning=false;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}