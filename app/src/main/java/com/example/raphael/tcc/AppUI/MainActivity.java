package com.example.raphael.tcc.AppUI;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.raphael.tcc.BackgroundServices.BackgroundService;
import com.example.raphael.tcc.DataBase.AppDbHelper;
import com.example.raphael.tcc.DataBase.DBContract;
import com.example.raphael.tcc.Managers.BrightnessManager;
import com.example.raphael.tcc.Managers.CpuManager;
import com.example.raphael.tcc.Managers.UsageStatus;
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
        SQLiteDatabase db = appDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.APP_DATABASE.APP_NAME,"teste");
        values.put(DBContract.APP_DATABASE.COLUMN_BRIGHTNESS_COL2,150);
        long newRow = db.insert(DBContract.APP_DATABASE.TABLE_NAME,null,values);
        db = appDbHelper.getReadableDatabase();
        String[] projection = {
                DBContract.APP_DATABASE.APP_NAME
        };
        String selection = DBContract.APP_DATABASE.APP_NAME + " = ?";
        String[] selectionArgs = { "teste" };
        String sortOrder =
                DBContract.APP_DATABASE.APP_NAME + " DESC";
        Cursor c = db.query(
                DBContract.APP_DATABASE.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        String itemId = c.getString(
                c.getColumnIndexOrThrow(DBContract.APP_DATABASE.APP_NAME)
        );
        System.out.println("AQUI: "+itemId);
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
