package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
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

public class MainMenu extends Fragment {
    private Button activate; //Set variables to hold the two buttons

    private Button deactivate;

    public static MainMenu newInstance() {
        Bundle args = new Bundle();
        MainMenu fragment = new MainMenu();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState) {
        View newView = inflate.inflate(R.layout.main_menu, container, false); //Set up container
        activate = newView.findViewById(R.id.activate); //Get the layouts from R
        deactivate = newView.findViewById(R.id.deactivate);

        Context context = getActivity().getBaseContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("menu_state", Context.MODE_PRIVATE);
        String state = sharedPreferences.getString("current_state", "deactivate");
        if(state.equals("activate")) {
            activate.setEnabled(false);
            deactivate.setEnabled(true);
        }
        else if(state.equals("deactivate"))
        {
            activate.setEnabled(true);
            deactivate.setEnabled(false);
        }

        //Detects the click for the activate button
        activate.setOnClickListener(view -> {
            Context context2 = getActivity().getBaseContext();
            SharedPreferences sharedPreferences2 = context2.getSharedPreferences("menu_state", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
            editor2.putString("current_state", "activate");
            editor2.apply();

            activate.setEnabled(false); //No repeat presses of activate
            deactivate.setEnabled(true); //Allow for press of deactivate
            getActivity().startService(new Intent(getActivity(), BackgroundService.class)); //Start the background
        });

        //Detects click on the deactivate button
        deactivate.setOnClickListener(view -> {
            Context context2 = getActivity().getBaseContext();
            SharedPreferences sharedPreferences2 = context2.getSharedPreferences("menu_state", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = sharedPreferences2.edit();
            editor2.putString("current_state", "deactivate");
            editor2.apply();

            activate.setEnabled(true); //Allow for activate to be pressed
            deactivate.setEnabled(false); //No repeat press of deactivate
            getActivity().stopService(new Intent(getActivity(), BackgroundService.class)); //Stop background
        });
        return newView;
    }
}
