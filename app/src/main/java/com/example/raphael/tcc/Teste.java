package com.example.raphael.tcc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by rapha on 18-Sep-16.
 */
public class Teste extends Activity {
    private WindowManager windowManager;
    @Override
    public void onCreate(Bundle savedInstancedBundle){
        super.onCreate(savedInstancedBundle);
        setContentView(R.layout.custom_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -20;
        params.height = 400;
        params.width = 550;
        params.y = -10;
        this.getWindow().setAttributes(params);
    }
}

