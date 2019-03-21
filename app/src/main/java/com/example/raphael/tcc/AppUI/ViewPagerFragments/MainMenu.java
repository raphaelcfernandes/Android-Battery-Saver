package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.raphael.tcc.BackgroundServices.BackgroundService;
import com.example.raphael.tcc.Managers.AppManager;
import com.example.raphael.tcc.R;

public class MainMenu extends Fragment
{
    private AppManager appManager = new AppManager();
    private boolean bbSwitch;
    private boolean notifSwitch;
    private Button activate;
    private Button deactivate;

    public static MainMenu newInstance()
    {
        Bundle args = new Bundle();
        MainMenu fragment = new MainMenu();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState)
    {
        View newView = inflate.inflate(R.layout.main_menu, container, false);
        activate = newView.findViewById(R.id.activate);
        deactivate = newView.findViewById(R.id.deactivate);

        activate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                activate.setEnabled(false);
                deactivate.setEnabled(true);
                getActivity().startService(new Intent(getActivity(), BackgroundService.class));
            }
        });

        deactivate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                activate.setEnabled(true);
                deactivate.setEnabled(false);
                getActivity().stopService(new Intent(getActivity(), BackgroundService.class));
            }
        });

        return newView;
    }
}
