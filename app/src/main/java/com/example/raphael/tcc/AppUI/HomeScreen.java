package com.example.raphael.tcc.AppUI;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.raphael.tcc.AppUI.ViewPagerFragments.SettingsPage;
import com.example.raphael.tcc.AppUI.ViewPagerFragments.MainMenu;
import com.example.raphael.tcc.AppUI.ViewPagerFragments.UsageStats;
import com.example.raphael.tcc.R;

//baw76 class to be the main screen and swipe tabs
public class HomeScreen extends FragmentPagerAdapter
{
    private static int NUM_PAGES = 3;
    private boolean bbButton;
    private boolean notif;

    public HomeScreen(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public int getCount()
    {
        return NUM_PAGES;
    }

    @Override
    public Fragment getItem(int pos)
    {
        switch(pos)
        {
            case 0:
                return MainMenu.newInstance();
            case 1:
                return SettingsPage.newInstance();
            case 2:
                return UsageStats.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int pos)
    {
        if(pos == 0)
            return "Main Menu";
        else if(pos == 1)
            return "Settings";
        else if(pos == 2)
            return "Usage Stats";
        else
            return null;
    }
}
