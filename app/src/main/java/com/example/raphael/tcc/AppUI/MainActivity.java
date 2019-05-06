package com.example.raphael.tcc.AppUI;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.raphael.tcc.R;

import android.provider.Settings;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/*
    Android Battery Saver
    baw76 Capstone Research Spring 2019
    -This class is the main activity but functions more as a container that sets up the swipe tabs.
*/

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter fpa;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager); //Gets the layout from R class
        //Check for usage stats permission
        PackageManager packageManager = this.getPackageManager();
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) this
                .getSystemService(Context.APP_OPS_SERVICE);

        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), this.getPackageName());
        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (this.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (!granted) {
            builder.setMessage("We need one more permission.\n You will be redirect to the settings to grant the user stats permission.").setTitle("Permission Required.");
            builder.setPositiveButton("OK", (dialog, id) -> {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            });
            builder.setNegativeButton("Refuse", (dialog, id) -> System.exit(0));
            builder.show();
        }
        ViewPager viewPager = findViewById(R.id.viewPager); //Set new view pager
        fpa = new HomeScreen(getSupportFragmentManager());
        viewPager.setAdapter(fpa);
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
