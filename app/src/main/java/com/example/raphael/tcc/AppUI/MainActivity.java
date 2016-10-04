package com.example.raphael.tcc.AppUI;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.raphael.tcc.BackgroundServices.BackgroundService;
import com.example.raphael.tcc.DataBase.AppDbHelper;
import com.example.raphael.tcc.DataBase.DBContract;
import com.example.raphael.tcc.R;

public class MainActivity extends Activity {
    AppDbHelper appDbHelper = new AppDbHelper(MainActivity.this);
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*if (usageStatus.getUsageStatsList(this).isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        startService(new Intent(getBaseContext(),BackgroundService.class));*/
       appDbHelper.insertAppConfiguration("TESTE",150,153,1500,132,15232);
    }
    public void onResume(){
        super.onResume();
    }

    public void startService(View view){
        startService(new Intent(this,BackgroundService.class));
    }
    public void stopService(View view){
        stopService(new Intent(this,BackgroundService.class));
    }

    public void onDestroy(){
        super.onDestroy();
        android.os.Debug.stopMethodTracing();
    }


}
