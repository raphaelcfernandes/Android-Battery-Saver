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

public class MainActivity extends Activity {
    AppManager appManager = new AppManager();
    Button b1, b2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = findViewById(R.id.ativar);
        b2 = findViewById(R.id.desativar);

        if (appManager.getUsageStatsList(this).isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        b2.setEnabled(false);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                b1.setEnabled(false);
                b2.setEnabled(true);
                startService(new Intent(getBaseContext(),BackgroundService.class));
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                b1.setEnabled(true);
                b2.setEnabled(false);
                stopService(new Intent(getBaseContext(), BackgroundService.class));
            }
        });
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Teste";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
