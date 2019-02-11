package com.example.raphael.tcc.Managers;

import com.example.raphael.tcc.ReadWriteFile;

import java.io.IOException;

/**
 * Created by raphael on 11-Sep-16.
 */
public class BrightnessManager {
    //Files can be found at /sys/class/leds/lcd-backlight/
    public static int minLevel = 5;
    public int getScreenBrightnessLevel(){
        String s = null;
        try {
            s = ReadWriteFile.returnStringFromProcess(Runtime.getRuntime().exec(new String[] {"su", "-c", "cat /sys/class/leds/lcd-backlight/brightness"}));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(s);
    }
    public void setBrightnessLevel(int level){
        try {
            Process proc = Runtime.getRuntime().exec(new String[] {"su", "-c", "echo " + level + " > " + "/sys/class/leds/lcd-backlight/brightness"});
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
