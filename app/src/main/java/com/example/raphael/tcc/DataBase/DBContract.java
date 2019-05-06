package com.example.raphael.tcc.DataBase;

import android.app.Activity;
import android.provider.BaseColumns;

import com.example.raphael.tcc.Managers.CpuManager;

/**
 * Created by rapha on 30-Sep-16.
 */

public class DBContract extends Activity {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DBContract() {
    }

    public static class APP_DATABASE implements BaseColumns {
        private static CpuManager cpuManager = new CpuManager();
        public static final String TABLE_NAME = "APP_CONFIGURATION";
        public static final String APP_NAME = "APP_NAME";
        public static final String COLUMN_BRIGHTNESS = "APP_BRIGHTNESS_LEVEL";
        public static final String COLUMN_CORE = "CPU";
        public static final String THRESHOLD = "THRES";
        public static final String CREATE_TABLE = CREATE_TABLE();
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        //Create Table
        private static String CREATE_TABLE() {
            int i;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CREATE TABLE " + TABLE_NAME + " (" + APP_NAME + " TEXT PRIMARY KEY," + COLUMN_BRIGHTNESS + INT_TYPE + COMMA_SEP);
            for (i = 0; i < CpuManager.getNumberOfCores(); i++) {
                stringBuilder.append(COLUMN_CORE).append(i).append(INT_TYPE).append(COMMA_SEP);

            }
            //Second For loop to avoid conflict with old methods.
            for (i = 0; i < CpuManager.getNumberOfCores(); i++) {
                stringBuilder.append(THRESHOLD).append(i).append(INT_TYPE).append(COMMA_SEP);

            }
            stringBuilder.setCharAt(stringBuilder.length() - 1, ')');
            return stringBuilder.toString();
        }

    }
}
