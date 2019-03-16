package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raphael.tcc.R;

public class UsageStats extends Fragment
{
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
        return newView;
    }
}
