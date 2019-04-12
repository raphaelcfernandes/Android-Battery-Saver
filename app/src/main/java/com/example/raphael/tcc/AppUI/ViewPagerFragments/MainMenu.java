package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.raphael.tcc.BackgroundServices.BackgroundService;
import com.example.raphael.tcc.R;

/*
    Android Battery Saver
    baw76 Capstone Research Spring 2019
    -This class handles the main menu(activate/deactivate) in the swipe fragments. Can be improved in future.
 */

public class MainMenu extends Fragment
{
    private Button activate; //Set variables to hold the two buttons
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
        View newView = inflate.inflate(R.layout.main_menu, container, false); //Set up container
        activate = newView.findViewById(R.id.activate); //Get the layouts from R
        deactivate = newView.findViewById(R.id.deactivate);

        activate.setOnClickListener(new View.OnClickListener()
        {   //Detects the click for the activate button
            @Override
            public void onClick(View view)
            {
                activate.setEnabled(false); //No repeat presses of activate
                deactivate.setEnabled(true); //Allow for press of deactivate
                getActivity().startService(new Intent(getActivity(), BackgroundService.class)); //Start the background
            }
        });

        deactivate.setOnClickListener(new View.OnClickListener()
        {   //Detects click on the deactivate button
            @Override
            public void onClick(View view)
            {
                activate.setEnabled(true); //Allow for activate to be pressed
                deactivate.setEnabled(false); //No repeat press of deactivate
                getActivity().stopService(new Intent(getActivity(), BackgroundService.class)); //Stop background
            }
        });

        return newView;
    }
}
