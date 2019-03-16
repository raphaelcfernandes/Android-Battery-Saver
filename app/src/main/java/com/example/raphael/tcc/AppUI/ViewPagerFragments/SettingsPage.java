package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raphael.tcc.R;

public class SettingsPage extends Fragment
{
    private boolean bbButton;
    private boolean notif;

    public static SettingsPage newInstance()
    {
        Bundle args = new Bundle();
        SettingsPage instance = new SettingsPage();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState)
    {
        View newView = inflate.inflate(R.layout.settings_page, container, false);
        return newView;
    }
}
