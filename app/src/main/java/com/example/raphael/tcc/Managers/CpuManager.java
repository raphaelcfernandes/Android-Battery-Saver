package com.example.raphael.tcc.Managers;

import android.os.Build;
import android.util.Log;

import com.example.raphael.tcc.ReadWriteFile;
import com.example.raphael.tcc.SearchAlgorithms;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class CpuManager {
    private static int numberOfCores;
    private String pathCPU = "/sys/devices/system/cpu/cpu";


    /**
     * This variable stores each row as core number and each column the possible frequency from lowest to highest
     * Suppose you have two available cores with 10 speed each, then this matrix will be [2][10]:
     * [0][0] = 1Mhz, [0][1] = 2Mhz, [0][2] = 30Mhz ...
     * [1][0] = 1Mhz, [1][1] = 2Mhz, [1][2] = 30Mhz ...
     */
    private static int[][] clockLevels;
    /**
     * This variable stores in the following order for each core
     * currentClockLevel[coreN][0] -> Tells in what frequency coreN is running, if it's 0 then core is offline
     * currentClockLevel[coreN][1] -> Tells how many available frequencies the coreN could run at
     * currentClockLevel[coreN][2] -> Tells whether the core is on (1) or off (0)
     */
    private static int[][] currentClockLevel;
    private static int amountOfValuesPerCore = 0;
    private static boolean isClockLevelsFilled = false;

    public static int[][] getClockLevels() {
        return clockLevels;
    }

    public CpuManager() {
        /**
         *   The constructor is responsible to identify how many cores the Smartphone has.
         *   Then, the matrices of each core should be filled.
         */
        if (Build.VERSION.SDK_INT >= 17) {
            numberOfCores = Runtime.getRuntime().availableProcessors();
        } else {
            numberOfCores = new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File params) {
                    return Pattern.matches("cpu[0-9]", params.getName());
                }
            }).length;
        }
        if (!isClockLevelsFilled) {
            isClockLevelsFilled = true;
            clockLevels = new int[numberOfCores][];
            currentClockLevel = new int[numberOfCores][3];
            prepareCores();
        }
    }

    /**
     * Method responsible for finding the default governor in the smartphone
     *
     * @return interactive governor if the smartphone has it, or return default ondemand
     */
    private String getDefaultGovernor() {
        String line = "";
        try {
            line = ReadWriteFile.returnStringFromProcess(Runtime.getRuntime().exec(new String[]{"su", "-c", "cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors"}));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String a : line.split("[ \t]")) {
            if (a.equals("interactive")) {
                return a;
            }
        }
        return "ondemand";
    }

    public static int getNumberOfCores() {
        return numberOfCores;
    }

    /**
     * This should prepare all cores to be userspace governor and also
     * it's responsible to fill the matrices about clockLevels and currentClockLevel for each core
     */
    private void prepareCores() {
        //Stop the decision of the kernel to control the cpu
        stopMpDecision();
        //Turn on cores to read their clock levels
        for (int i = 0; i < numberOfCores; ++i) {
            if (i != 0)
                turnCoreOnOff(i, true);
            //Set userspace on the cores so one can write the configuration into cpu files
            echoUserSpace(i);
            //Fill the matrix of clockLevels
            fillClockLevelMatrix(i);
            //Set Min and Max Freq based on ClockLevelMatrix
            if (i != 0)
                turnCoreOnOff(i, false);
        }
    }

    private void adjustMinAndMaxSpeed(int core) {
        StringBuilder path = new StringBuilder();
        try {
            path.setLength(0);
            path.append("echo " + clockLevels[core][clockLevels[core].length - 1] + " > " + pathCPU + core + "/cpufreq/scaling_max_freq");
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
            proc.waitFor();
            path.setLength(0);
            path.append("echo " + clockLevels[core][0] + " > " + pathCPU + core + "/cpufreq/scaling_min_freq");
            proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
            proc.waitFor();
            proc.getInputStream().close();
            proc.getOutputStream().close();
            proc.getErrorStream().close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * In case the user stops UImpatience, we should be responsible to retrieve ALL the CPU control
     * back to Android.
     * Also restart Qualcomm mpdecision
     */
    public void giveAndroidFullControl() {

        for (int i = 0; i < numberOfCores; ++i) {
            turnCoreOnOff(i, true);
            StringBuilder path = new StringBuilder();
            try {
                path.setLength(0);
                path.append("echo " + getDefaultGovernor() + " > " + pathCPU + i + "/cpufreq/scaling_governor");
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
                proc.waitFor();
                proc.getInputStream().close();
                proc.getOutputStream().close();
                proc.getErrorStream().close();
                path.setLength(0);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        String stopMpDecision = "start mpdecision";
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", stopMpDecision});
            proc.waitFor();
            proc.getInputStream().close();
            proc.getOutputStream().close();
            proc.getErrorStream().close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * Self-explanotory method. This will turn off the Qualcomm mpdecision
     * Mpdecision: https://elementalx.org/the-truth-about-kernels-and-battery-life/
     */
    private void stopMpDecision() {
        String stopMpDecision = "stop mpdecision";
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", stopMpDecision});
            proc.waitFor();
            proc.getInputStream().close();
            proc.getOutputStream().close();
            proc.getErrorStream().close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Self-explanotory method. It will write in the cpu scaling_governor "userspace"
     * so UImpatience can take control of the specific core.
     *
     * @param core
     */
    private void echoUserSpace(int core) {
        StringBuilder path = new StringBuilder();
        try {
            path.setLength(0);
            path.append("echo userspace > " + pathCPU + core + "/cpufreq/scaling_governor");
            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
            proc.waitFor();
            proc.getInputStream().close();
            proc.getOutputStream().close();
            proc.getErrorStream().close();
            path.setLength(0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param core  in respect to which core will be turned on/off
     * @param state True if you want to turn on, False otherwise
     *              This method will turn on the core and update its currentClockLevel[core][2] to 1 or 0 based on @param state
     */
    private void turnCoreOnOff(int core, boolean state) {
        //True to turn on Core
        StringBuilder path = new StringBuilder();
        if (state) {//Turn on
            try {
                path.setLength(0);
                //Write in the CPU file 1 indicating the core should be turned on
                path.append(String.format("echo 1 > " + pathCPU + "%d/online", core));
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
                proc.waitFor();
                proc.getInputStream().close();
                proc.getOutputStream().close();
                proc.getErrorStream().close();
                currentClockLevel[core][2] = 1;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {//Turn Off
                path.setLength(0);
                //Write in the CPU file 1 indicating the core should be turned off
                path.append("echo 0 > " + pathCPU + core + "/online");
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
                proc.waitFor();
                proc.getInputStream().close();
                proc.getOutputStream().close();
                proc.getErrorStream().close();
                //Update column 2 related to the core state (on/off)
                currentClockLevel[core][2] = 0;
                //Update column 1 related to the core current frequency
                currentClockLevel[core][0] = 0;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * For each @param core this method will fill up the matrix of available frequencies for each core
     *
     * @param core
     */
    private void fillClockLevelMatrix(int core) {
        String line;
        String[] levels;
        int x;
        try {
            //This return a list of frequencies as: F1 F2 F3 ... Fn
            line = ReadWriteFile.returnStringFromProcess(Runtime.getRuntime().exec(new String[]{"su", "-c", "cat /sys/devices/system/cpu/cpu" + core + "/cpufreq/scaling_available_frequencies"}));
            levels = line.split("[ \t]");
            clockLevels[core] = new int[levels.length];
            //Set how many different frequencies this core has
            currentClockLevel[core][1] = levels.length;
            amountOfValuesPerCore = levels.length;
            if (core == 0) {
                currentClockLevel[core][2] = 1;
//                currentClockLevel[core][0] = Integer.valueOf(levels[levels.length/2]);
            }
            //Fill the matrix of frequencies for each fore
            //Each column represents a frequency F1 F2 F3 ... Fn
            for (x = 0; x < levels.length; x++)
                clockLevels[core][x] = Integer.valueOf(levels[x]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set a given frequency to the specific core. If speed is 0 then core will be turned off
     *
     * @param core
     * @param speed
     */
    private void writeSpeedOnCore(int core, int speed) {
        Log.i(this.getClass().getName(), "writeSpeedOnCore: " + core + ", " + speed);
        StringBuilder path = new StringBuilder();

        if (speed != 0) {
            //Check if core is off and turn it on
            if (currentClockLevel[core][2] == 0) {
                turnCoreOnOff(core, true);
            }
            adjustMinAndMaxSpeed(core);
            try {
                path.setLength(0);
                path.append(String.format("echo %d" + " > " + pathCPU + core + "/cpufreq/scaling_setspeed", speed));
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", path.toString()});
                proc.waitFor();
                proc.getInputStream().close();
                proc.getOutputStream().close();
                proc.getErrorStream().close();
                //!IMPORTANT
                //Update the current core frequency
                currentClockLevel[core][0] = speed;
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        } else
            turnCoreOnOff(core, false);
    }

    private int getSpeedOfCore(int coreNumber) {
        return currentClockLevel[coreNumber][0];
    }

    /**
     * Method use to set the SeekBar Percentage in FeedBackPopUpWindow based on how many cores are on
     * and their respective frequencies
     *
     * @return
     */
    public int getSumNumberCore() {
        return (calculation() * 100) / (amountOfValuesPerCore * numberOfCores);
    }

    private int calculation() {
        int sum = 0;
        for (int i = 0; i < numberOfCores && currentClockLevel[i][2] == 1; ++i)
            sum += SearchAlgorithms.binarySearch(currentClockLevel[i][0], i, clockLevels);
        return sum;
    }

    /**
     * Method will setup cores for new configuration in @param arrayConfiguration
     *
     * @param arrayConfiguration
     */
    public void adjustConfiguration(ArrayList<String> arrayConfiguration) {
        int x, i;
        //This means that default setup should be loaded
        //All cores but 0 are turned off
        //Core 0 frequency is set to be its middle frequency of all possible frequencies
        //Set all cores to the highest speed.
        if (arrayConfiguration.size() == 0) {
            for (i = 0; i < numberOfCores; i++) {
                turnCoreOnOff(i, true);
                //old one  ONLY WORKED ON THE CORE 0, NOT ALL CORES.
                writeSpeedOnCore(i, clockLevels[i][clockLevels[i].length - 1]);
            }
        } else
            //i starts at 2 because index 0 represents the name of the running app
            //and index 1 represents the brightness level
            for (i = 2, x = 0; i < numberOfCores + 2; i++, x++)
                //Write on core X the frequency represented by index i in arrayConfiguration
                writeSpeedOnCore(x, Integer.parseInt(arrayConfiguration.get(i)));
    }

    public void setToMinSpeed() {
        for (int i = 0; i < numberOfCores; i++) {
            if (i == 0) {
                writeSpeedOnCore(0, clockLevels[0][0]);
            } else {
                writeSpeedOnCore(i, 0);
            }
        }
    }

    public List<Integer> setSpeedByArrayListDESC(List<Integer> speedConfiguration) {
        int core = speedConfiguration.size();
        int speed = 0;
        outConfiguration:
        for (int i = numberOfCores - 1; i >= 0; i--) {
            //Write on core X the frequency represented by index i in arrayConfiguration
            for (int m = clockLevels[i].length - 1; m >= 0; m--) {
                if (speedConfiguration.get(i) == 0) {
                    if (currentClockLevel[i][2] == 1) {
                        writeSpeedOnCore(i, 0);
                    }
                    continue;
                }
                if (speedConfiguration.get(i) < clockLevels[i][0] && i != 0) {
                    speedConfiguration.set(i, 0);
                    if (currentClockLevel[i][2] == 1) {
                        writeSpeedOnCore(i, 0);
                    }
                    break;
                }
                if (speedConfiguration.get(i) >= clockLevels[i][m]) {
                    core = i;
                    if (m > 0) {
                        speed = clockLevels[i][m];
                    } else {
                        speed = clockLevels[i][0];
                    }
                    writeSpeedOnCore(core, speed);
                    break outConfiguration;
                }
            }
        }

        return speedConfiguration;
    }

    public List<Integer> setSpeedByArrayListASC(List<Integer> speedConfiguration) {
        try {
            outConfiguration:
            for (int i = 0; i < speedConfiguration.size() ; i++) {
                //Write on core X the frequency represented by index i in arrayConfiguration
                for (int m = 0; m < clockLevels[i].length; m++) {
                    if (speedConfiguration.get(i) < clockLevels[i][m]) {
                        writeSpeedOnCore(i, clockLevels[i][m]);
                        speedConfiguration.set(i, clockLevels[i][m]);
                        break outConfiguration;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return speedConfiguration;
    }


    /**
     * Get the current frequency of each core, 0 if it's off
     *
     * @return arrayList of frequencies where each index is the frequency related to that core
     */
    public ArrayList<Integer> getArrayListCoresSpeed() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < numberOfCores; i++) {
            arrayList.add(i, getSpeedOfCore(i));
        }
        return arrayList;
    }

    /**
     * Receives a value from the backgroundService that represents the % amount we should increase
     * the frequency(ies)
     *
     * @param value as percentage
     */
    //TODO amountOfValuesPerCore should be a matrix in case cores have different frequencies between them
    public void setCpuSpeedFromUserInput(int value) {
        int converter = (value * (numberOfCores * amountOfValuesPerCore)) / 100;
        setArrayListOfSpeedFromUserInput(converter);
    }

    /**
     * Method that receives how many values it should increase in frequencies IN A GLOBAL state
     * So, if you have 48 possible frequencies and converter = 12, then at least 2 cores should be
     * turned on
     *
     * @param converter
     */
    private void setArrayListOfSpeedFromUserInput(int converter) {
        int i = 0;
        boolean flag = true;
        ArrayList<Integer> arrayList = new ArrayList<>();
        //It will iterate for every core
        //To understand this while imagine the following scenario:
        //You have 4 cores with 12 possible frequencies for each. Suppose that core 0 is on at max
        //and converter = 15.
        //First it will add to vector 12 and will subtract from converter 12, which left 3
        //Next iteration it will reach the second if add 3 to array and break.
        //This means that core 1 will be turned on and frequency will be set at maximum
        //While core 2 (because there still 3 left in the array) will be set to frequency in
        //third position of clockLevels matrix
        while (i < numberOfCores) {
            if (converter >= amountOfValuesPerCore) {
                arrayList.add(i, amountOfValuesPerCore);
                flag = false;
            } else if (converter > 0 && converter < amountOfValuesPerCore) {
                arrayList.add(i, converter);
                break;
            } else if (converter == 0 && flag) {
                arrayList.add(i, 0);
                break;
            } else
                break;
            i++;
            converter -= amountOfValuesPerCore;
        }
        for (i = 0; i < arrayList.size(); i++) {
            //Turn core i on and set it to maximum frequency
            if (arrayList.get(i) == amountOfValuesPerCore)
                writeSpeedOnCore(i, clockLevels[i][arrayList.get(i) - 1]);
                //Turn core i on and set it to frequency of position i at clockLevels
            else
                writeSpeedOnCore(i, clockLevels[i][arrayList.get(i)]);
        }
        //Turn other cores off
        if (i < numberOfCores) {
            for (; i < numberOfCores; i++)
                turnCoreOnOff(i, false);
        }
    }
}

