package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recAdapter;
    private RecyclerView.LayoutManager recManager;

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

        recyclerView = newView.findViewById(R.id.recyclerView);
        recManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(recManager);
        recyclerView.setAdapter(recAdapter);

        return newView;
    }
}
