package com.example.raphael.tcc.AppUI;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.raphael.tcc.AppUI.ViewPagerFragments.SettingsPage;
import com.example.raphael.tcc.AppUI.ViewPagerFragments.MainMenu;
import com.example.raphael.tcc.AppUI.ViewPagerFragments.UsageStats;

//baw76 class to be the main screen and swipe tabs
public class HomeScreen extends FragmentPagerAdapter
{
    private static int NUM_PAGES = 3;

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
        switch(pos) //Based on page number return the page
        {
            case 0: //First page to be loaded
                return MainMenu.newInstance(); //Want main menu to be first
            case 1: //Second page
                return SettingsPage.newInstance(); //Settings is the second page
            case 2: //Third page
                return UsageStats.newInstance(); //Usage stats will be the third page
            default:
                return null;
        }
    }

    @Override //Return the title of the page to be in the top tab strip
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
