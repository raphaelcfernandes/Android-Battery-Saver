package com.example.raphael.tcc.Managers;

import com.example.raphael.tcc.ReadWriteFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by rapha on 11-Sep-16.
 */
public class BrightnessManager {
    //Files can be found at /sys/class/leds/lcd-backlight/
    public int getScreenBrightnessLevel(){
        String s = null;
        try {
            s = ReadWriteFile.read("/sys/class/leds/lcd-backlight/brightness");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(s);
    }
    public boolean setBrightnessLevel(int level){
        try {
            Runtime.getRuntime().exec(new String[] {"su", "-c", "echo " + level + " > " + "/sys/class/leds/lcd-backlight/brightness"});
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
