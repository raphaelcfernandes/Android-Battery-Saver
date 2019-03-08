package com.example.raphael.tcc.AppUI;

import android.app.Activity;
import android.os.Bundle;

import com.example.raphael.tcc.Managers.AppManager;
import com.example.raphael.tcc.R;

import android.support.v4.view.ViewPager;

public class MainActivity extends Activity {
    private AppManager appManager = new AppManager();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        ViewPager viewPager = new ViewPager();
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
