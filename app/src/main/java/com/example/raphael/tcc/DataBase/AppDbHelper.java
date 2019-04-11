package com.example.raphael.tcc.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.raphael.tcc.AppUI.ViewPagerFragments.AppStatsView;

import java.util.ArrayList;

/**
 * Created by rapha on 26-Sep-16.
 */

public class AppDbHelper extends SQLiteOpenHelper{

    private static final  int    DATABASE_VERSION   = 1;
    private static final  String DATABASE_NAME      = "APP_DATABASE.db";
    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.APP_DATABASE.DELETE_TABLE);
        db.execSQL(DBContract.APP_DATABASE.CREATE_TABLE);
    }

    // Method is called during an upgrade of the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.APP_DATABASE.DELETE_TABLE);
        onCreate(db);
    }
    public void insertAppConfiguration(String APP_NAME, int brightnessLevel, ArrayList<Integer> cpuSpeed){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.APP_DATABASE.APP_NAME, APP_NAME);
        contentValues.put(DBContract.APP_DATABASE.COLUMN_BRIGHTNESS, brightnessLevel);
        for(int x=0;x<cpuSpeed.size();x++)
            contentValues.put(DBContract.APP_DATABASE.COLUMN_CORE+x, cpuSpeed.get(x));
        db.insert(DBContract.APP_DATABASE.TABLE_NAME, null, contentValues);
    }

    private boolean CheckIsDataAlreadyInDBorNot(String AppName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM "+ DBContract.APP_DATABASE.TABLE_NAME +" WHERE "
                + DBContract.APP_DATABASE.APP_NAME+" LIKE ?", new String[]{AppName});
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public ArrayList<String> getAppData(int numberOfCores, String AppName){
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM "+ DBContract.APP_DATABASE.TABLE_NAME +" WHERE "
                + DBContract.APP_DATABASE.APP_NAME+" LIKE ?", new String[]{AppName});
        if(res.moveToFirst()){
            arrayList.add(res.getString(res.getColumnIndex(DBContract.APP_DATABASE.APP_NAME)));
            arrayList.add(res.getString(res.getColumnIndex(DBContract.APP_DATABASE.COLUMN_BRIGHTNESS)));
            for(int x=0;x<numberOfCores;x++)
                arrayList.add(res.getString(res.getColumnIndex(DBContract.APP_DATABASE.COLUMN_CORE+x)));
        }
        res.close();
        return arrayList;
    }

    //baw76 method
    //New method will simply get every row in the table and make a new AppStatsView
    public ArrayList<AppStatsView> getAllAppData()
    {
        ArrayList<AppStatsView> appData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //Helpful to shorten query
        String tbName = DBContract.APP_DATABASE.TABLE_NAME;
        String appNameParam = DBContract.APP_DATABASE.APP_NAME;
        String brightParam = DBContract.APP_DATABASE.COLUMN_BRIGHTNESS;
        String coreParam1 = DBContract.APP_DATABASE.COLUMN_CORE + 0;
        String coreParam2 = DBContract.APP_DATABASE.COLUMN_CORE + 1;
        String coreParam3 = DBContract.APP_DATABASE.COLUMN_CORE + 2;
        String coreParam4 = DBContract.APP_DATABASE.COLUMN_CORE + 3;

        //Query the database and save into a Cursor object
        String[] columns = {appNameParam,brightParam,coreParam1,coreParam2,coreParam3,coreParam4};
        Cursor cursor = db.query(tbName,columns, null,null, null, null, null);

        //Set indices to make parsing the row easier
        int appNameIndex = cursor.getColumnIndex(appNameParam);
        int brightIndex = cursor.getColumnIndex(brightParam);
        int core1Index = cursor.getColumnIndex(coreParam1);
        int core2Index = cursor.getColumnIndex(coreParam2);
        int core3Index = cursor.getColumnIndex(coreParam3);
        int core4Index = cursor.getColumnIndex(coreParam4);

        //Checks that cursor is not empty
        if(!cursor.moveToFirst())
            return new ArrayList<AppStatsView>();

        do
        {
            //Get the corresponding value from each column in the row
            String appName = cursor.getString(appNameIndex);
            int brtness = cursor.getInt(brightIndex);
            int core1 = cursor.getInt(core1Index);
            int core2 = cursor.getInt(core2Index);
            int core3 = cursor.getInt(core3Index);
            int core4 = cursor.getInt(core4Index);

            //Add the new AppStatsView to our list
            appData.add(new AppStatsView(appName, brtness, core1, core2, core3, core4));
        }
        while (cursor.moveToNext()); //Will iterate the cursor Index

        //Close the cursor and database connection; return the list we created
        cursor.close();
        db.close();
        return appData;
    }

    public void updateAppConfiguration (String APP_NAME, int brightnessLevel, ArrayList<Integer> cpuSpeed) {
        if(!CheckIsDataAlreadyInDBorNot(APP_NAME))
            insertAppConfiguration(APP_NAME, brightnessLevel, cpuSpeed);
        else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBContract.APP_DATABASE.COLUMN_BRIGHTNESS, brightnessLevel);
            for (int x = 0; x < cpuSpeed.size(); x++)
                contentValues.put(DBContract.APP_DATABASE.COLUMN_CORE + x, cpuSpeed.get(x));
            db.update(DBContract.APP_DATABASE.TABLE_NAME, contentValues, "APP_NAME = ? ", new String[]{APP_NAME});
        }
    }
}
