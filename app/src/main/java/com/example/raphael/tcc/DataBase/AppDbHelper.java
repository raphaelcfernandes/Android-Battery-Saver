package com.example.raphael.tcc.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.raphael.tcc.AppUI.ViewPagerFragments.AppStatsView;
import com.example.raphael.tcc.Logv;
import com.example.raphael.tcc.Managers.CpuManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rapha on 26-Sep-16.
 */

public class AppDbHelper extends SQLiteOpenHelper {

    private final String classTag = getClass().getSimpleName();
    private static AppDbHelper instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "APP_DATABASE.db";

    public static synchronized AppDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AppDbHelper(context);
        }
        return instance;
    }

    public AppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void log(String msg) {
        Logv.log(classTag + " - " + msg);
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

    public void insertAppConfiguration(String APP_NAME, int brightnessLevel, ArrayList<Integer> cpuSpeed, List<Integer> thresholds) {
        log("insertAppConfiguration() - App_NAME:" + APP_NAME);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.APP_DATABASE.APP_NAME, APP_NAME);
        contentValues.put(DBContract.APP_DATABASE.COLUMN_BRIGHTNESS, brightnessLevel);
        for (int x = 0; x < cpuSpeed.size(); x++) {
            contentValues.put(DBContract.APP_DATABASE.COLUMN_CORE + x, cpuSpeed.get(x));
        }
        for (int x = 0; x < thresholds.size(); x++) {
            contentValues.put(DBContract.APP_DATABASE.THRESHOLD + x, thresholds.get(x));
        }
        System.out.println(contentValues.size());
        db.insert(DBContract.APP_DATABASE.TABLE_NAME, null, contentValues);
    }

    private boolean CheckIsDataAlreadyInDBorNot(String AppName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBContract.APP_DATABASE.TABLE_NAME + " WHERE "
                + DBContract.APP_DATABASE.APP_NAME + " LIKE ?", new String[]{AppName});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public AppData getAppData(int numberOfCores, String AppName) {
        log("getAppData() - AppName:" + AppName);
        //Array list size should be total of the following items
        //1- app name
        //2- brightness value
        //3- N of cores
        //4- N of thresholds

        //so if we have CPU with 4 cores then the size will be 10
        //[0] appName
        //[1] brightnessValue
        //[2] core0Speed
        //[3] core1Speed
        //[4] core2Speed
        //[5] core3Speed
        //[6] core0Threshold
        //[7] core1Threshold
        //[8] core2Threshold
        //[9] core0Threshold

        //
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + DBContract.APP_DATABASE.TABLE_NAME + " WHERE "
                + DBContract.APP_DATABASE.APP_NAME + " LIKE ?", new String[]{AppName});

        //
        AppData appData = new AppData();

        if (res.moveToFirst()) {
            appData.name = res.getString(res.getColumnIndex(DBContract.APP_DATABASE.APP_NAME));
            appData.brightness = res.getInt(res.getColumnIndex(DBContract.APP_DATABASE.COLUMN_BRIGHTNESS));

            for (int x = 0; x < numberOfCores; x++) {
                appData.coresSpeeds.add(res.getInt(res.getColumnIndex(DBContract.APP_DATABASE.COLUMN_CORE + x)));
            }
            for (int x = 0; x < numberOfCores; x++) {
                appData.coresThresholds.add(res.getInt(res.getColumnIndex(DBContract.APP_DATABASE.THRESHOLD + x)));
            }

        }
        res.close();
        return appData;
    }

    //baw76 method
    //New method will simply get every row in the table and make a new AppStatsView
    public ArrayList<AppStatsView> getAllAppData() {
        ArrayList<AppStatsView> appData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int numCores = CpuManager.getNumberOfCores();

        //Helpful to shorten query
        String tbName = DBContract.APP_DATABASE.TABLE_NAME;
        String appNameParam = DBContract.APP_DATABASE.APP_NAME;
        String brightParam = DBContract.APP_DATABASE.COLUMN_BRIGHTNESS;
        String coreParams[] = new String[4];
        coreParams[0] = DBContract.APP_DATABASE.COLUMN_CORE + 0;
        coreParams[1] = DBContract.APP_DATABASE.COLUMN_CORE + 1;
        coreParams[2] = DBContract.APP_DATABASE.COLUMN_CORE + 2;
        coreParams[3] = DBContract.APP_DATABASE.COLUMN_CORE + 3;

        //Query the database and save into a Cursor object
        ArrayList<String> columns = new ArrayList<>();
        columns.add(appNameParam);
        columns.add(brightParam);
        if (numCores == 1)
        {
            columns.add(coreParams[0]);
        }
        if (numCores == 2)
        {
            columns.add(coreParams[0]);
            columns.add(coreParams[1]);
        }
        if(numCores == 3)
        {
            columns.add(coreParams[0]);
            columns.add(coreParams[1]);
            columns.add(coreParams[2]);
        }
        if(numCores == 4)
        {
            columns.add(coreParams[0]);
            columns.add(coreParams[1]);
            columns.add(coreParams[2]);
            columns.add(coreParams[3]);
        }

        String[] columnsArray = columns.toArray(new String[columns.size()]);
        Cursor cursor = db.query(tbName,columnsArray, null,null, null, null, null);

        //Set indices to make parsing the row easier
        int appNameIndex = cursor.getColumnIndex(appNameParam);
        int brightIndex = cursor.getColumnIndex(brightParam);
        ArrayList<Integer> coreIndexes = new ArrayList<>();
        for(int i = 0; i < numCores; i++){
            log("getAllAppData - i:" + i);
            coreIndexes.add(cursor.getColumnIndex(coreParams[i]));
        }

        //Checks that cursor is not empty
        if(!cursor.moveToFirst())
            return new ArrayList<AppStatsView>();

        do
        {
            //Get the corresponding value from each column in the row
            String appName = cursor.getString(appNameIndex);
            int brtness = cursor.getInt(brightIndex);
            ArrayList<Integer> cores = new ArrayList<>();
            for(int i = 0; i < numCores; i++)
                cores.add(cursor.getInt(coreIndexes.get(i)));

            //Add the new AppStatsView to our list
            if(numCores == 1)
                appData.add(new AppStatsView(appName, brtness, cores.get(0), 0, 0, 0));
            if(numCores == 2)
                appData.add(new AppStatsView(appName, brtness, cores.get(0), cores.get(1), 0, 0));
            if(numCores == 3)
                appData.add(new AppStatsView(appName, brtness, cores.get(0), cores.get(1), cores.get(2), 0));
            if(numCores == 4)
                appData.add(new AppStatsView(appName,brtness,cores.get(0),cores.get(1),cores.get(2),cores.get(3)));
        }
        while (cursor.moveToNext()); //Will iterate the cursor Index

        //Close the cursor and database connection; return the list we created
        cursor.close();
        db.close();
        return appData;
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
  
    public void updateAppConfiguration(String APP_NAME, int brightnessLevel, ArrayList<Integer> cpuSpeed, List<Integer> thresholds) {
        log("updateAppConfiguration() - App_NAME:" + APP_NAME + " - brightnessLevel: " + brightnessLevel);

        if (!CheckIsDataAlreadyInDBorNot(APP_NAME))
            insertAppConfiguration(APP_NAME, brightnessLevel, cpuSpeed, thresholds);
        else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBContract.APP_DATABASE.COLUMN_BRIGHTNESS, brightnessLevel);
            for (int x = 0; x < cpuSpeed.size(); x++) {
                contentValues.put(DBContract.APP_DATABASE.COLUMN_CORE + x, cpuSpeed.get(x));

            }
            for (int x = 0; x < thresholds.size(); x++) {
                contentValues.put(DBContract.APP_DATABASE.THRESHOLD + x, thresholds.get(x));
            }
            db.update(DBContract.APP_DATABASE.TABLE_NAME, contentValues, "APP_NAME = ? ", new String[]{APP_NAME});
        }
    }
}
