package com.example.raphael.tcc.Managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Raphael on 09-Jun-16.
 */
public class NetworkManager {
    public String get_network(Context context) {
        String network_type="UNKNOWN";//maybe usb reverse tethering
        NetworkInfo active_network=((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (active_network!=null /*&& active_network.isConnectedOrConnecting()*/) {
            if (active_network.getType()==ConnectivityManager.TYPE_WIFI) {
                network_type="WIFI";
            }
            else if (active_network.getType()==ConnectivityManager.TYPE_MOBILE) {
                network_type=((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().getSubtypeName();
            }
        }
        return network_type;
    }
}
//Funcionando