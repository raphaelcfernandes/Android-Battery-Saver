package com.example.raphael.tcc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.raphael.tcc.Managers.BrightnessManager;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;


/**
 * Created by rapha on 18-Sep-16.
 */
public class FeedBackPopUpWindow extends Activity {
    private boolean isClicked=false;
    private boolean isProgressBarMoved=false;
    DiscreteSeekBar seekBar;
    Button button ;
    BrightnessManager brightnessManager = new BrightnessManager();
    @Override
    public void onCreate(Bundle savedInstancedBundle){
        super.onCreate(savedInstancedBundle);
        setContentView(R.layout.custom_dialog);
        seekBar = (DiscreteSeekBar) findViewById(R.id.seekBarBrightness);
        button = (Button)findViewById(R.id.upButton);
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
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isClicked=true;
            }
        });
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -20;
        params.height = 500;
        params.width = 550;
        params.y = -10;
        this.getWindow().setAttributes(params);
    }
    public void onResume(){
        super.onResume();
    }
    public void onDestroy(){
        super.onDestroy();
        if(isProgressBarMoved==true)
            brightnessManager.setBrightnessLevel((seekBar.getProgress()*255)/100);
        //Create Intent and send to BackgroundService -> ButtonClicked
        System.out.println(isClicked);
        if(isClicked==true) {
            Intent i = new Intent("com.example.raphael.tcc.REQUESTED_MORE_CPU");
            sendBroadcast(i);
        }
    }
}

