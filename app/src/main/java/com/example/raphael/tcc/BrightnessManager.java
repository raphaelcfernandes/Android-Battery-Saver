package com.example.raphael.tcc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by rapha on 11-Sep-16.
 */
public class BrightnessManager {
    //Files can be found at /sys/class/leds/lcd-backlight/
    public String getScreenBrightnessLevel(){
        String s = null;
        try {
            s = read("/sys/class/leds/lcd-backlight/brightness");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
    public boolean setBrightnessLevel(int level){
        try {
            Runtime.getRuntime().exec(new String[]{"su", "-c"});
            Runtime.getRuntime().exec(new String[] {"su", "-c", "echo " + level + " > " + "/sys/class/leds/lcd-backlight/brightness"});
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private static String read(String path) throws IOException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        output.append(reader.readLine());
        for (String line = reader.readLine(); line != null; line = reader.readLine())
            output.append('\n').append(line);
        reader.close();
        return output.toString();
    }
}
