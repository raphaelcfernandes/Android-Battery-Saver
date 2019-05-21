package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.raphael.tcc.DataBase.AndroidDatabaseManager;
import com.example.raphael.tcc.R;

/*
    Android Battery Saver
    baw76 Capstone Research Spring 2019
    -This class is a fragment that handles the two buttons for settings. Can be expanded in future.
 */

public class SettingsPage extends Fragment
{
    private Switch bbSwitch; //Will hold the reference to the switch dedicated to BubbleButton
    private Switch notifSwitch; //Will hold the reference to the switch dedicated to SpeedUpNotification
    private Button database;

    public static SettingsPage newInstance()
    {
        SettingsPage instance = new SettingsPage();
        Bundle args = new Bundle();
        instance.setArguments(args);
        return instance;
    }

    @Override //Standard method needed but has changes nothing
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState)
    {
        View newView = inflate.inflate(R.layout.settings_page, container, false); //Create fragment
        bbSwitch = newView.findViewById(R.id.BubbleButtonSwitch); //Find the layout of BubbleButton
        notifSwitch = newView.findViewById(R.id.NotificationSwitch); //Find layout of Notification

        database = newView.findViewById(R.id.buttonData); //Find layout of Notification


        loadSettings();

        bbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {   //Detects a change in the switch position
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if(isChecked)
                {
                    Context context = getActivity().getBaseContext();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("shared_settings", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("bubble_button", true);
                    editor.apply();
                }
                else
                {
                    Context context = getActivity().getBaseContext();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("shared_settings", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("bubble_button", false);
                    editor.apply();
                }

            }
        });

        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {   //Detects a change in the switch position
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if(isChecked) //True if the switch has been set to 'On' position
                {
                    Context context = getActivity().getBaseContext();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("shared_settings", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notification", true);
                    editor.apply();
                }
                else //True if the switch is in the 'Off' position
                {
                    Context context = getActivity().getBaseContext();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("shared_settings", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("notification", false);
                    editor.apply();
                }
            }
        });

        database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dbmanager = new Intent(getActivity(), AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });

        return newView;
    }

    private void loadSettings() {
        Context context = getActivity().getBaseContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_settings", Context.MODE_PRIVATE);

        //
        boolean bubbleButtonSavedSetting = sharedPreferences.getBoolean("bubble_button", false);
        bbSwitch.setChecked(bubbleButtonSavedSetting);

        //
        boolean notificationSavedSetting = sharedPreferences.getBoolean("notification", false);
        notifSwitch.setChecked(notificationSavedSetting);

    }
}
