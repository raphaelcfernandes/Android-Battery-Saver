package com.example.raphael.tcc;

import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class CpuManager {
    private int numberOfCores;
    private String pathCPU = "cat /sys/devices/system/cpu/cpu";

    CpuManager(){
        if(Build.VERSION.SDK_INT>=17){
            this.numberOfCores = Runtime.getRuntime().availableProcessors();
        }
        else {
            int i = new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File params){
                    return Pattern.matches("cpu[0-9]", params.getName());
                }
            }).length;
            this.numberOfCores = i;
        }
    }

    public void setProcessesRunning() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("/system/bin/ps");
            returnStringFromProcess(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCoreUtilization(int coreNumber){
        StringBuilder path = new StringBuilder();
        String p= null;
        path.append("cat /sys/devices/system/cpu/cpu" + coreNumber + "/cpufreq/cpu_utilization");
        try {
             p = returnStringFromProcess(Runtime.getRuntime().exec(path.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }//ok

    public String getGovernorOfCore(int coreNumber){//ok
        String p=null;
        StringBuilder path = new StringBuilder();
        path.append(pathCPU + coreNumber + "/cpufreq/scaling_governor");
        try {
            p = returnStringFromProcess(Runtime.getRuntime().exec(path.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }//ok

    public String getSpeedOfCore(int coreNumber) {//ok
        StringBuilder path = new StringBuilder();
        path.append("cat /sys/devices/system/cpu/cpu" + coreNumber + "/cpufreq/scaling_cur_freq");
        String p = null;
        try {
            p = returnStringFromProcess(Runtime.getRuntime().exec(path.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }//ok

    public int getNumberOfCores() {
        return this.numberOfCores;
    }//ok

    public boolean isCoreOnline(int coreNumber){
        StringBuilder path = new StringBuilder();
        path.append(pathCPU + coreNumber + "/online");
        String p = null;
        int result=0;
        try {
            p = returnStringFromProcess(Runtime.getRuntime().exec(path.toString()));
            result = Character.getNumericValue(p.charAt(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(result==1)
            return true;
        else
            return false;
    }

    private String returnStringFromProcess(Process proc) throws IOException {
        StringBuilder ps = new StringBuilder();
        String s;
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        while ((s = stdInput.readLine()) != null) {
            ps.append(s);
            ps.append('\n');
        }
        return ps.toString();
    }//ok
}

