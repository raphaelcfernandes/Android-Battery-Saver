package com.example.raphael.tcc.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rapha on 30-Sep-16.
 */

public class AppDbHelper extends SQLiteOpenHelper{

    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "APP_DATABASE.db";
    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.APP_DATABASE.CREATE_TABLE);
    }

    // Method is called during an upgrade of the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.APP_DATABASE.DELETE_TABLE);
        onCreate(db);
    }
    public void insertAppConfiguration(String APP_NAME, int brightnessLevel, int ... arguments){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.APP_DATABASE.APP_NAME, APP_NAME);
        contentValues.put(DBContract.APP_DATABASE.COLUMN_BRIGHTNESS, brightnessLevel);
        for(int x=0;x<arguments.length;x++)
            contentValues.put(DBContract.APP_DATABASE.COLUMN_CORE+x, arguments[x]);
        db.insert(DBContract.APP_DATABASE.TABLE_NAME, null, contentValues);
    }

}
