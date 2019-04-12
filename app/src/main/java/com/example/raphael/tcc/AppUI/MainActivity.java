package com.example.raphael.tcc.AppUI;

import android.os.Bundle;
import com.example.raphael.tcc.R;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/*
    Android Battery Saver
    baw76 Capstone Research Spring 2019
    -This class is the main activity but functions more as a container that sets up the swipe tabs.
*/

public class MainActivity extends AppCompatActivity
{
    FragmentPagerAdapter fpa;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager); //Gets the layout from R class

        ViewPager viewPager = findViewById(R.id.viewPager); //Set new view pager
        fpa = new HomeScreen(getSupportFragmentManager());
        viewPager.setAdapter(fpa);
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
