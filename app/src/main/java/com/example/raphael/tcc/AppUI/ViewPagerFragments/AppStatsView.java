package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import java.util.ArrayList;

public class AppStatsView
{
    private String appName;
    private int brightness;
    private int core_speed1, core_speed2, core_speed3, core_speed4;

    public AppStatsView(ArrayList<String> statsFromDB)
    {
        appName = statsFromDB.get(0);
        brightness = Integer.parseInt(statsFromDB.get(1));

        if(statsFromDB.size() == 3)
        {
            core_speed1 = Integer.parseInt(statsFromDB.get(2));
            core_speed2 = 0;
            core_speed3 = 0;
            core_speed4 = 0;
        }
        if(statsFromDB.size() == 4)
        {
            core_speed1 = Integer.parseInt(statsFromDB.get(2));
            core_speed2 = Integer.parseInt(statsFromDB.get(3));
            core_speed3 = 0;
            core_speed4 = 0;
        }
        if(statsFromDB.size() == 5)
        {
            core_speed1 = Integer.parseInt(statsFromDB.get(2));
            core_speed2 = Integer.parseInt(statsFromDB.get(3));
            core_speed3 = Integer.parseInt(statsFromDB.get(4));
            core_speed4 = 0;
        }
        if(statsFromDB.size() == 6)
        {
            core_speed1 = Integer.parseInt(statsFromDB.get(2));
            core_speed2 = Integer.parseInt(statsFromDB.get(3));
            core_speed3 = Integer.parseInt(statsFromDB.get(4));
            core_speed4 = Integer.parseInt(statsFromDB.get(5));
        }
    }

    public String getAppName()
    {
        return appName;
    }

    public int getBrightness()
    {
        return brightness;
    }

    public int getCoreSpeed1()
    {
        return core_speed1;
    }

    public int getCoreSpeed2()
    {
        return core_speed2;
    }

    public int getCoreSpeed3()
    {
        return core_speed3;
    }

    public int getCoreSpeed4()
    {
        return core_speed4;
    }
}
