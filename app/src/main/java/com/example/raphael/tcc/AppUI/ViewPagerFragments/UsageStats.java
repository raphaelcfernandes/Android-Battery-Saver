package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raphael.tcc.DataBase.AppDbHelper;
import com.example.raphael.tcc.R;

/*
    Android Battery Saver
    baw76 Capstone Research Spring 2019
    -This class handles the statistics for the algorithms and displays them to the user.
 */

public class UsageStats extends Fragment
{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recAdapter;
    private RecyclerView.LayoutManager recManager;
    private ArrayList<AppStatsView> appStatsList = new ArrayList<>();
    private AppDbHelper dbHelper;

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

        //Get all the data from the database and create the list
        dbHelper = new AppDbHelper(getActivity().getApplicationContext());
        appStatsList = dbHelper.getAllAppData();

        //Set all the necessary handlers for the RecyclerView
        recyclerView = newView.findViewById(R.id.recyclerView);
        recManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(recManager);
        recAdapter = new RecyclerAdapter(appStatsList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recAdapter);

        return newView;
    }
}
