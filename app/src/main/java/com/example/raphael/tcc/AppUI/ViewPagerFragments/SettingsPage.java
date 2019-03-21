package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.raphael.tcc.R;

public class SettingsPage extends Fragment
{
    private Switch bbSwitch;
    private Switch notifSwitch;

    public static SettingsPage newInstance()
    {
        Bundle args = new Bundle();
        SettingsPage instance = new SettingsPage();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstanceState)
    {
        View newView = inflate.inflate(R.layout.settings_page, container, false);
        bbSwitch = newView.findViewById(R.id.BubbleButtonSwitch);
        notifSwitch = newView.findViewById(R.id.NotificationSwitch);

        bbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b)
                {

                }
                else
                {

                }

            }
        });

        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(b)
                {

                }
                else
                {

                }
            }
        });


        return newView;
    }

    public boolean bbSwitchChecked()
    {
        if(bbSwitch.isChecked())
            return true;
        else
            return false;
    }

    public boolean notifSwitchChecked()
    {
        if(notifSwitch.isChecked())
            return true;
        else
            return false;
    }
}
