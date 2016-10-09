package com.example.raphael.tcc.Managers;

import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class CpuManager {
    private int numberOfCores;
    private String pathCPU = "/sys/devices/system/cpu/cpu";
    private int[][] clockLevels;
    private static boolean isClockLevelsFilled=false;

    public CpuManager(){
        if(Build.VERSION.SDK_INT>=17){
            numberOfCores = Runtime.getRuntime().availableProcessors();
        }
        else {
            numberOfCores = new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File params){
                    return Pattern.matches("cpu[0-9]", params.getName());
                }
            }).length;
        }
        if(isClockLevelsFilled==false) {
            isClockLevelsFilled=true;
            clockLevels = new int[this.numberOfCores][];
            prepareCores();
        }
    }

    public int getNumberOfCores(){
        return this.numberOfCores;
    }

    private void prepareCores(){
        //Stop the decision of the kernel to control the cpu
        String stopMpDecision = "stop mpdecision";
        try{
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", stopMpDecision});
            proc.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Set userspace on the cores so one can write the configuration into cpu files
        for(int i=0;i<this.numberOfCores;++i)
            echoUserSpace(i);
        //Turn on cores to read their clock levels
        for(int i=0;i<this.numberOfCores;++i)
            turnCoreOnOff(i,true);
        //Fill the matrix of clockLevels
        fillClockLevelMatrix();
        for(int i=1;i<this.numberOfCores;++i)
            turnCoreOnOff(i,false);
    }

    private void echoUserSpace(int core){
        StringBuilder path = new StringBuilder();
        try {
            path.setLength(0);
            path.append(String.format("echo userspace > /sys/devices/system/cpu/cpu%d/cpufreq/scaling_governor", core));
            Process proc = Runtime.getRuntime().exec(new String[]{"su","-c",path.toString()});
            proc.waitFor();
            path.setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void turnCoreOnOff(int core,boolean state){
        //True to turn on Core
        StringBuilder path = new StringBuilder();
        if(state==true) {
            try {
                path.setLength(0);
                path.append(String.format("echo 1 > " + pathCPU + "%d/online", core));
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
                proc.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                path.setLength(0);
                path.append(String.format("echo 0 > " + pathCPU + "%d/online", core));
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
                proc.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillClockLevelMatrix(){
        String line;
        String[] levels;
        int x;
        for(int i=0;i<this.numberOfCores;++i){
            try {
                line = returnStringFromProcess(Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_available_frequencies"));
                System.out.println(line);
                levels = line.split("[ \t]");
                this.clockLevels[i]=new int[levels.length+1];
                for(x = 0; x < levels.length; x++) {
                    clockLevels[i][x] = Integer.valueOf(levels[x]);
                }
                if(i>0){
                    clockLevels[i][x]=0;//Only main core online
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        }
        return ps.toString();
    }//ok
}

