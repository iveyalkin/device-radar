package org.bitbucket.rocketracoons.deviceradar.utility;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.bitbucket.rocketracoons.deviceradar.network.ApiClient;

import retrofit.RestAdapter;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class Utility {
    public static final WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getConnectionInfo();
    }

    private static ApiClient sApiClientInstance = null;
    public static final ApiClient getApiClient() {
        if (null == sApiClientInstance) {
            final RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.URL)
                    .build();
            sApiClientInstance = restAdapter.create(ApiClient.class);
        }
        return sApiClientInstance;
    }
}
