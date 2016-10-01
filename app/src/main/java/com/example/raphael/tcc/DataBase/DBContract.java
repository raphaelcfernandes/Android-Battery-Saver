package com.example.raphael.tcc.DataBase;

import android.provider.BaseColumns;

/**
 * Created by rapha on 30-Sep-16.
 */

public class DBContract {
    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "APP_DATABASE.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DBContract() {}

    public static abstract class APP_DATABASE implements BaseColumns {
        public static final String TABLE_NAME       = "nameOfTable";
        public static final String COLUMN_NAME_COL1 = "column1";
        public static final String COLUMN_NAME_COL2 = "column2";
        public static final String COLUMN_NAME_COL3 = "column3";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_COL1 + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_COL2 + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_COL3 + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
