package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.raphael.tcc.R;

import java.util.ArrayList;

/*
    Android Battery Saver
    baw76 Capstone Research Spring 2019
    -This class handles the statistics for the algorithms and displays them to the user.
    *****Still in progress
 */

public class UsageStats extends Fragment
{
    ListView leftList;
    ListView rightList;
    ArrayList leftArrayList;
    ArrayList rightArrayList;

    public static UsageStats newInstance()
    {
        UsageStats newUS = new UsageStats();
        Bundle args = new Bundle();
        newUS.setArguments(args);
        return newUS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState)
    {
        View newView = inflate.inflate(R.layout.usage_stats, container, false);

        leftList = newView.findViewById(R.id.left_list);
        rightList = newView.findViewById(R.id.right_list);

        return newView;
    }
}
