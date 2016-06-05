package com.example.raphael.tcc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Raphael on 18-Apr-16.
 */
public class ProcessesRunning {
    Process p;
    StringBuilder ps = new StringBuilder();
    String s;
    public String setProcessesRunning() {
        try {
            p = Runtime.getRuntime().exec("/system/bin/ps");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                this.ps.append(s);
                this.ps.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(ps);
        s=ps.toString();
        return this.s;
    }
}
