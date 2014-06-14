package org.bitbucket.rocketracoons.deviceradar.utility;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bitbucket.rocketracoons.deviceradar.network.ApiClient;
import org.bitbucket.rocketracoons.deviceradar.network.deserializer.DateDeserializer;

import java.util.Date;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class Utility {
    private static final String TAG = Utility.class.getSimpleName();

    public static final WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getConnectionInfo();
    }

    private static ApiClient sApiClientInstance = null;
    public static final ApiClient getApiClient() {
        if (null == sApiClientInstance) {
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateDeserializer()).create();

            final RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.URL)
                    .setConverter(new GsonConverter(gson))
                    .setLog(new Logger.Retrofit())
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            sApiClientInstance = restAdapter.create(ApiClient.class);
        }
        return sApiClientInstance;
    }
}
