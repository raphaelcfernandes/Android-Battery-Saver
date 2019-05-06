package com.example.raphael.tcc;

import com.example.raphael.tcc.Managers.CpuManager;

/**
 * Created by rapha on 20-Oct-16.
 */


public final class SingletonClasses {
    private static CpuManager instance = null;
    private SingletonClasses(){}
    public static synchronized CpuManager getInstance(){
        if(instance==null)
            instance = new CpuManager();
        return instance;
    }
}
