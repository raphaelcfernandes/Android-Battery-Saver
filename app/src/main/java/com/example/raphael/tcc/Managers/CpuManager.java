package com.example.raphael.tcc.Managers;

import android.os.Build;

import com.example.raphael.tcc.ReadWriteFile;
import com.example.raphael.tcc.SearchAlgorithms;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class CpuManager {
    private static int numberOfCores;
    private String pathCPU = "/sys/devices/system/cpu/cpu";
    private static int[][] clockLevels;
    private static int[][] currentClockLevel;
    private static int amountOfValuesPerCore=0;
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
        for (int i = 0; i < numberOfCores; ++i) {
            if(i!=0)
                turnCoreOnOff(i, true);
            //Set userspace on the cores so one can write the configuration into cpu files
            echoUserSpace(i);
            //Fill the matrix of clockLevels
            fillClockLevelMatrix(i);
            //Set Min and Max Freq based on ClockLevelMatrix
            if(i!=0)
                turnCoreOnOff(i, false);
        }
    }

    private void adjustMinAndMaxSpeed(int core) {
        StringBuilder path = new StringBuilder();
        try {
            path.setLength(0);
            path.append("echo "+clockLevels[core][clockLevels[core].length-1]+ " > "+pathCPU + core +"/cpufreq/scaling_max_freq");
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c",path.toString()});
            proc.waitFor();
            path.setLength(0);
            path.append("echo "+clockLevels[core][0]+ " > "+pathCPU +  core+ "/cpufreq/scaling_min_freq");
            proc = Runtime.getRuntime().exec(new String[]{"su", "-c",path.toString()});
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void giveAndroidFullControl() {

        for (int i = 0; i < numberOfCores; ++i) {
            turnCoreOnOff(i, true);
            //Set userspace on the cores so one can write the configuration into cpu files
            StringBuilder path = new StringBuilder();
            try {
                path.setLength(0);
                path.append("echo ondemand > "+pathCPU+i+"/cpufreq/scaling_governor");
                Process proc = Runtime.getRuntime().exec(new String[]{"su","-c",path.toString()});
                proc.waitFor();
                path.setLength(0);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            if (i != 0)
                turnCoreOnOff(i, false);
        }
        String stopMpDecision = "start mpdecision";
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", stopMpDecision});
            proc.waitFor();
        } catch (InterruptedException | IOException e) {
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
                currentClockLevel[core][2]=1;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
            try {//Turn Off
                path.setLength(0);
                path.append("echo 0 > " + pathCPU + core+"/online");
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
                proc.waitFor();
                currentClockLevel[core][2]=0;
                currentClockLevel[core][0]=0;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillClockLevelMatrix(int core){
        String line;
        String[] levels;
        int x;
        try {
            line = ReadWriteFile.returnStringFromProcess(Runtime.getRuntime().exec(new String[] {"su", "-c", "cat /sys/devices/system/cpu/cpu" + core + "/cpufreq/scaling_available_frequencies"}));
            levels = line.split("[ \t]");
            clockLevels[core]=new int[levels.length];
            currentClockLevel[core][1]=levels.length;
            amountOfValuesPerCore=levels.length;//Problema se o ultimo processador tiver menos frequencia q os demais
            if(core==0) {
                currentClockLevel[core][2] = 1;
                currentClockLevel[core][0]=Integer.valueOf(levels[levels.length/2]);
            }
            for(x = 0; x < levels.length; x++)
                clockLevels[core][x] = Integer.valueOf(levels[x]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeSpeedOnCore(int core,int speed){
        StringBuilder path = new StringBuilder();
        //Ligar o core caso esteja offline
        if(speed!=0) {
            if(currentClockLevel[core][2]==0) {
                turnCoreOnOff(core, true);
            }
            adjustMinAndMaxSpeed(core);
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
        else
            turnCoreOnOff(core,false);
    }

    private int getSpeedOfCore(int coreNumber) {//ok
        return currentClockLevel[coreNumber][0];
    }

    private int isCoreOnline(int coreNumber){
        return currentClockLevel[coreNumber][2];
    }

    public int getSumNumberCore(){
        return (calculation()*100)/ (amountOfValuesPerCore*numberOfCores);
    }

    private int calculation(){
        int sum=0;
        for(int i = 0; i< numberOfCores && currentClockLevel[i][2]==1; ++i)
            sum += SearchAlgorithms.binarySearch(currentClockLevel[i][0],i, clockLevels);
        System.out.println(sum);
        return sum;
    }

    public void adjustConfiguration(ArrayList<String> arrayConfiguration){
        int x,i;
        if(arrayConfiguration.size()==0){
            for(i=1;i<numberOfCores;i++)
                turnCoreOnOff(i,false);
            writeSpeedOnCore(0,clockLevels[0][((currentClockLevel[0][1])/2)+1]);
        }
        else
            for(i=2,x=0;i<arrayConfiguration.size();i++,x++)
                writeSpeedOnCore(x, Integer.parseInt(arrayConfiguration.get(i)));
    }

    public ArrayList<Integer> getArrayListCoresSpeed(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i=0;i<numberOfCores;i++){
            arrayList.add(i,getSpeedOfCore(i));
        }
        return arrayList;
    }

    public void setCpuSpeedFromUserInput(int value){
        //value is percentage
        int converter = (value*(numberOfCores* amountOfValuesPerCore))/100;
        setArrayListOfSpeedFromUserInput(converter);
    }

    private void setArrayListOfSpeedFromUserInput(int converter) {
        int i=0;
        boolean flag=true;
        ArrayList<Integer> arrayList = new ArrayList<>();
        while(i<numberOfCores){
            if(converter>=amountOfValuesPerCore) {
                arrayList.add(i,amountOfValuesPerCore);
                flag=false;
            }
            else if(converter>0&&converter<amountOfValuesPerCore) {
                arrayList.add(i, converter);
                break;
            }
            else if(converter==0 && flag) {
                arrayList.add(i, 0);
                break;
            }
            else
                break;
            i++;
            converter-=amountOfValuesPerCore;
        }
        for(i=0;i<arrayList.size();i++) {
            if(arrayList.get(i)==amountOfValuesPerCore)
                writeSpeedOnCore(i, clockLevels[i][arrayList.get(i)-1]);
            else
                writeSpeedOnCore(i, clockLevels[i][arrayList.get(i)]);
        }
        if(i<numberOfCores){
            for(;i<numberOfCores;i++)
                turnCoreOnOff(i,false);
        }
    }
}

