package com.example.raphael.tcc;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.raphael.tcc.Managers.BrightnessManager;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;


/**
 * Created by rapha on 18-Sep-16.
 */
public class FeedBackPopUpWindow extends Activity {
    private WindowManager windowManager;
    DiscreteSeekBar seekBar;
    BrightnessManager brightnessManager = new BrightnessManager();
    @Override
    public void onCreate(Bundle savedInstancedBundle){
        super.onCreate(savedInstancedBundle);
        setContentView(R.layout.custom_dialog);
        seekBar = (DiscreteSeekBar) findViewById(R.id.seekBarBrightness);
        seekBar.setMin(0);
        seekBar.setProgress((brightnessManager.getScreenBrightnessLevel()*100)/255);
        seekBar.setMax(100);//Percentage

        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            int onProgressChanged=0;
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                onProgressChanged = value;
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -20;
        params.height = 500;
        params.width = 550;
        params.y = -10;
        this.getWindow().setAttributes(params);
    }
    public void onResume(Bundle savedInstanceBundle){
        super.onResume();
    }
    public void onDestroy(){
        super.onDestroy();
        brightnessManager.setBrightnessLevel((seekBar.getProgress()*255)/100);
    }
}

