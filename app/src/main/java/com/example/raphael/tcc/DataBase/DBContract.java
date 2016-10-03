package com.example.raphael.tcc.DataBase;

import android.provider.BaseColumns;

/**
 * Created by rapha on 30-Sep-16.
 */

public class DBContract {
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DBContract() {}

    public static class APP_DATABASE implements BaseColumns {
        public static final String TABLE_NAME       = "APP_CONFIGURATION";
        public static final String APP_NAME = "APP_NAME";
        public static final String COLUMN_BRIGHTNESS_COL2 = "APP_BRIGHTNESS_LEVEL";
        public static final String COLUMN_NAME_COL3 = "column3";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                APP_NAME + " TEXT PRIMARY KEY," +
                COLUMN_BRIGHTNESS_COL2 + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_COL3 + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
