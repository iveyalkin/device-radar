package org.bitbucket.rocketracoons.deviceradar.utility;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class Utility {
    public static final WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getConnectionInfo();
    }
}
