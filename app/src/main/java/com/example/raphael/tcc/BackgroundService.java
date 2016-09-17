package com.example.raphael.tcc;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BackgroundService extends Service {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public static final String CUSTOM_INTENT="com.example.raphael.tcc";
    private TextView teste;
    BrightnessManager brightnessManager = new BrightnessManager();
    private ImageView chatHead;
    private WindowManager windowManager;
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
                //System.out.println(s);
                if(s.equals("com.android.vending")){
                    Intent intent = new Intent(CUSTOM_INTENT);
                    sendBroadcast(intent);
                    System.out.println(brightnessManager.getScreenBrightnessLevel());
                    brightnessManager.setBrightnessLevel(1);
                    System.out.println(brightnessManager.getScreenBrightnessLevel());
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
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.mipmap.ic_cross_symbol);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 100;
        params.y = 100;
        windowManager.addView(chatHead, params);
        chatHead.setOnTouchListener(new View.OnTouchListener(){
            private final static int MAX_CLICK_DURATION = 200;
            private long startClickTime;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(Calendar.getInstance().getTimeInMillis() - startClickTime < MAX_CLICK_DURATION) {
                            System.out.println("apertei kct");
                            teste.setVisibility(View.VISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHead, params);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    private static String getAppRunningForeground(){
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