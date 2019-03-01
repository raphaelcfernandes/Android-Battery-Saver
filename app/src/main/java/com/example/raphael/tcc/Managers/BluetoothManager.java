package com.example.raphael.tcc.Managers;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by Raphael on 20-Apr-16.
 */
public class BluetoothManager {
    private BluetoothAdapter blueT;
    public int getBluetoothStatus(){
        blueT = BluetoothAdapter.getDefaultAdapter();
        if(blueT.isEnabled())
            return 1;
        else
            return 0;
    }
}

//Funcionando 100%
