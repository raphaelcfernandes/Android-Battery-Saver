package com.example.raphael.tcc.AppUI.ViewPagerFragments;

import java.util.ArrayList;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raphael.tcc.R;

/*
    Android Battery Saver
    baw76@pitt.edu CS1980 Capstone
    -This class handles the communication between the individual AppStats and the View on screen
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView appTitle;
        public TextView brightness;
        public TextView cpuFreq1;
        public TextView cpuFreq2;
        public TextView cpuFreq3;
        public TextView cpuFreq4;

        public ViewHolder(View itemView)
        {
            super(itemView);

            appTitle = itemView.findViewById(R.id.app_view_title);
            brightness = itemView.findViewById(R.id.brightness);
            cpuFreq1 = itemView.findViewById(R.id.cpu_freq1);
            cpuFreq2 = itemView.findViewById(R.id.cpu_freq2);
            cpuFreq3 = itemView.findViewById(R.id.cpu_freq3);
            cpuFreq4 = itemView.findViewById(R.id.cpu_freq4);
        }
    }

    private ArrayList<AppStatsView> appViews; //List of all the apps on the system and their data
    public RecyclerAdapter(ArrayList<AppStatsView> apps)
    {
        appViews = apps;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View appView = inflater.inflate(R.layout.app_view_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(appView);
        return viewHolder;
    }

    @Override //Add all the appropriate values to the holder
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position)
    {
        AppStatsView app = appViews.get(position);
        viewHolder.appTitle.setText(app.getAppName());
        viewHolder.brightness.setText(Integer.toString(app.getBrightness()));
        viewHolder.cpuFreq1.setText(Integer.toString(app.getCoreSpeed1()));
        viewHolder.cpuFreq2.setText(Integer.toString(app.getCoreSpeed2()));
        viewHolder.cpuFreq3.setText(Integer.toString(app.getCoreSpeed3()));
        viewHolder.cpuFreq4.setText(Integer.toString(app.getCoreSpeed4()));
    }

    @Override
    public int getItemCount()
    {
        return appViews.size();
    }
}

