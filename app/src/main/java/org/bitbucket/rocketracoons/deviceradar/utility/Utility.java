package org.bitbucket.rocketracoons.deviceradar.utility;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bitbucket.rocketracoons.deviceradar.model.Message;
import org.bitbucket.rocketracoons.deviceradar.network.ApiClient;
import org.bitbucket.rocketracoons.deviceradar.network.deserializer.DateDeserializer;
import org.bitbucket.rocketracoons.deviceradar.network.deserializer.MessageTypeDeserializer;

import java.util.Date;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class Utility {
    private static final String TAG = Utility.class.getSimpleName();

    private static ApiClient sApiClientInstance = null;
    public static final ApiClient getApiClient() {
        if (null == sApiClientInstance) {
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateDeserializer())
                    .registerTypeAdapter(Message.Type.class, new MessageTypeDeserializer())
                    .create();

            final RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.Config.getCurrentConfig().getUrl())
                    .setConverter(new GsonConverter(gson))
                    .setLog(new Logger.Retrofit())
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            sApiClientInstance = restAdapter.create(ApiClient.class);
        }
        return sApiClientInstance;
    }

    private static Gson sGsonInstance = null;
    public static Gson getGsonInstance() {
        if (null == sGsonInstance) {
            sGsonInstance = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateDeserializer())
                    .create();
        }
        return sGsonInstance;
    }
}
