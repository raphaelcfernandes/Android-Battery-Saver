package com.example.raphael.tcc.AppUI;

import android.content.Intent;
import android.os.Bundle;

import com.example.raphael.tcc.R;

import android.provider.Settings;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/*
    Android Battery Saver
    baw76 Capstone Research Spring 2019
    -This class is the main activity but functions more as a container that sets up the swipe tabs.
*/

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter fpa;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager); //Gets the layout from R class

        ViewPager viewPager = findViewById(R.id.viewPager); //Set new view pager
        fpa = new HomeScreen(getSupportFragmentManager());
        viewPager.setAdapter(fpa);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() //Tells if the user swipes pages
                                          {
                                              @Override
                                              public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                                  //This is where an animation can be placed when swiping between pages.
                                              }

                                              @Override
                                              public void onPageSelected(int position) {
                                                  //This code is executed when the page is the current position.
                                              }

                                              @Override
                                              public void onPageScrollStateChanged(int state) {
                                                  //Used to detect specifics about what is happening during the changing of pages.
                                              }
                                          }
        );
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
