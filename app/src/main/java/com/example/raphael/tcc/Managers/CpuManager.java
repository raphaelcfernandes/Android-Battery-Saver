package com.example.raphael.tcc.Managers;

import android.os.Build;

import com.example.raphael.tcc.SearchAlgorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class CpuManager {
    private static int numberOfCores;
    private String pathCPU = "/sys/devices/system/cpu/cpu";
    private static int[][] clockLevels;
    private static int[][] currentClockLevel;
    private static int amountOfValues=0;
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
        if(!isClockLevelsFilled) {
            isClockLevelsFilled=true;
            clockLevels = new int[numberOfCores][];
            currentClockLevel = new int[numberOfCores][3];
            prepareCores();
        }
    }

    public static int getNumberOfCores(){
        return numberOfCores;
    }

    private void prepareCores() {
        //Stop the decision of the kernel to control the cpu
        stopMpDecision();
        //Turn on cores to read their clock levels
        for (int i = 0; i < numberOfCores; ++i)
            turnCoreOnOff(i, true);
        //Set userspace on the cores so one can write the configuration into cpu files
        for (int i = 0; i < numberOfCores; ++i)
            echoUserSpace(i);
        //Fill the matrix of clockLevels
        fillClockLevelMatrix();
        //Set Min and Max Freq based on ClockLevelMatrix
        adjustMinAndMaxSpeed();
        for (int i = 1; i < numberOfCores; ++i)
            turnCoreOnOff(i, false);
        //Fill the vector of current cores. ALL but core0 is offline.
        for (int i = 1; i < numberOfCores; i++)
            currentClockLevel[i][0] = 0;
        //initialConfiguration();
    }

    private void adjustMinAndMaxSpeed() {
        StringBuilder path = new StringBuilder();
        try {
            for (int i = 0; i < numberOfCores; i++){
                path.setLength(0);
                path.append("echo "+clockLevels[i][clockLevels[i].length-1]+ " > "+pathCPU + i +"/cpufreq/scaling_max_freq");
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c",path.toString()});
                proc.waitFor();
                path.setLength(0);
                path.append("echo "+clockLevels[i][0]+ " > "+pathCPU +  i+ "/cpufreq/scaling_min_freq");
                proc = Runtime.getRuntime().exec(new String[]{"su", "-c",path.toString()});
                proc.waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopMpDecision(){
        String stopMpDecision = "stop mpdecision";
        try{
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", stopMpDecision});
            proc.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void echoUserSpace(int core){
        StringBuilder path = new StringBuilder();
        try {
            path.setLength(0);
            path.append("echo userspace > "+pathCPU+core+"/cpufreq/scaling_governor");
            Process proc = Runtime.getRuntime().exec(new String[]{"su","-c",path.toString()});
            proc.waitFor();
            path.setLength(0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void turnCoreOnOff(int core,boolean state){
        //True to turn on Core
        StringBuilder path = new StringBuilder();
        if(state==true) {//Turn on
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
            try {//Turn Off
                path.setLength(0);
                path.append("echo 0 > " + pathCPU + core+"/online");
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
        for(int i = 0; i< numberOfCores; ++i){
            try {
                line = returnStringFromProcess(Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_available_frequencies"));
                levels = line.split("[ \t]");
                clockLevels[i]=new int[levels.length];
                currentClockLevel[i][1]=levels.length;
                amountOfValues+=levels.length;
                if(i==0)
                    currentClockLevel[i][2]=1;
                for(x = 0; x < levels.length; x++)
                    clockLevels[i][x] = Integer.valueOf(levels[x]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialConfiguration(){
        StringBuilder path = new StringBuilder();
        currentClockLevel[0][0]= clockLevels[0][0];
        try {
            path.setLength(0);
            path.append("echo " + clockLevels[0][0] + " > " + pathCPU + "0/cpufreq/scaling_setspeed");
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void writeSpeedOnCore(int core,int speed){
        StringBuilder path = new StringBuilder();
        //Ligar o core caso esteja offline
        if(speed!=0) {
            if(currentClockLevel[core][0]==0)
                turnCoreOnOff(core, true);
            try {
                path.setLength(0);
                path.append(String.format("echo %d" + " > " + pathCPU + core + "/cpufreq/scaling_setspeed", speed));
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
                proc.waitFor();
                currentClockLevel[core][0] = speed;
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getSpeedOfCore(int coreNumber) {//ok
        return currentClockLevel[coreNumber][0];
    }//ok

    private int isCoreOnline(int coreNumber){
        return currentClockLevel[coreNumber][0];
    }

    public int getSumNumberCore(){
        return (calculation()*100)/amountOfValues;
    }

    private int calculation(){
        int sum=0;
        for(int i = 0; i< numberOfCores && currentClockLevel[i][2]==1; ++i)
            sum += SearchAlgorithms.binarySearch(currentClockLevel[i][0],i, clockLevels);
        return sum;
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

    public void setConfigurationToMinimum(){
        for(int i=1;i<numberOfCores;i++)
            turnCoreOnOff(i,false);
        writeSpeedOnCore(0,clockLevels[0][0]);
    }

    public void adjustConfiguration(ArrayList<String> arrayConfiguration){
        for(int i=2,x=0;i<arrayConfiguration.size();i++,x++){
            writeSpeedOnCore(x,Integer.parseInt(arrayConfiguration.get(i)));
        }
    }

    public ArrayList<Integer> getCoresSpeed(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i=0;i<numberOfCores;i++){
            arrayList.add(i,getSpeedOfCore(i));
        }
        return arrayList;
    }
}

