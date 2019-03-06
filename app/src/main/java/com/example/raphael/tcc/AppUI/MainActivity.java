package com.example.raphael.tcc.AppUI;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import com.example.raphael.tcc.BackgroundServices.BackgroundService;
import com.example.raphael.tcc.Managers.AppManager;
import com.example.raphael.tcc.R;
import com.example.raphael.tcc.ReadWriteFile;
import java.io.IOException;

import android.support.v4.view.ViewPager;

public class MainActivity extends Activity {
    private AppManager appManager = new AppManager();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        ViewPager viewPager = findViewById(R.id.viewpager);
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
