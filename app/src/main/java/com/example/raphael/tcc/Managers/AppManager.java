package com.example.raphael.tcc.Managers;

import com.example.raphael.tcc.ReadWriteFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by rapha on 17-Sep-16.
 */
public class AppManager {
    private ReadWriteFile readWriteFile = new ReadWriteFile();
    public String getAppRunningForeground(){
        int pid;
        File[] files = new File("/proc").listFiles();
        int lowestOomScore = Integer.MAX_VALUE;
        String foregroundProcess = null;
        for (File file : files) {
            if (!file.isDirectory() || (!file.getName().matches(("\\d+"))))
                continue;
            pid = Integer.parseInt(file.getName());
            try {
                String cgroup = readWriteFile.read(String.format("/proc/%d/cgroup", pid));
                String[] lines = cgroup.split("\n");
                if (lines.length != 2)
                    continue;
                String cpuSubsystem = lines[0];
                String cpuaccctSubsystem = lines[1];
                if (!cpuaccctSubsystem.endsWith(Integer.toString(pid)) || cpuSubsystem.endsWith("bg_non_interactive"))
                    continue;
                String cmdline = readWriteFile.read(String.format("/proc/%d/cmdline", pid));
                if (cmdline.contains("com.android.systemui")) {
                    continue;
                }
                int uid = Integer.parseInt(cpuaccctSubsystem.split(":")[2].split("/")[1].replace("uid_", ""));
                if (uid >= 1000 && uid <= 1038)//System process
                    continue;
                File oomScoreAdj = new File(String.format("/proc/%d/oom_score_adj", pid));
                if (oomScoreAdj.canRead()) {
                    int oomAdj = Integer.parseInt(readWriteFile.read(oomScoreAdj.getAbsolutePath()));
                    if (oomAdj != 0) {
                        continue;
                    }
                }
                int oomscore = Integer.parseInt(readWriteFile.read(String.format("/proc/%d/oom_score", pid)));
                if (oomscore < lowestOomScore) {
                    lowestOomScore = oomscore;
                    foregroundProcess = cmdline.replaceAll("\\p{Cntrl}", "");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return foregroundProcess;
    }
}
