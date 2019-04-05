package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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

import java.util.ArrayList;
import java.util.List;

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
    private ArrayList<AppStatsView> appStatsList = new ArrayList<>();
    private AppDbHelper dbHelper = new AppDbHelper(getActivity().getBaseContext());

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
        getAppStats();

        recyclerView = newView.findViewById(R.id.recyclerView);
        recManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(recManager);
        getAppStats();
        recAdapter = new RecyclerAdapter(appStatsList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recAdapter);

        return newView;
    }

    private void getAppStats()
    {
        ArrayList<String> appNames = getAppList();

        for(String s : appNames)
        {
            appStatsList.add(new AppStatsView(dbHelper.getAppData(4, s)));
        }
    }

    private ArrayList<String> getAppList()
    {
        ArrayList<String> appNames = new ArrayList<>();
        List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);

        for(int i = 0; i < packs.size(); i++)
        {
            PackageInfo p = packs.get(i);
            if((isSystemPackage(p) == false))
            {
                String name = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                appNames.add(name);
            }
        }

        return appNames;
    }

    private boolean isSystemPackage(PackageInfo pkginfo)
    {
        return ((pkginfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;

    }
}
