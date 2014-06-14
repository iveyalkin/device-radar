package org.bitbucket.rocketracoons.deviceradar.network;

import org.bitbucket.rocketracoons.deviceradar.model.Device;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public interface ApiClient {
    @GET("/device")
    void getDevicesList(Callback<ArrayList<Device>> callback);
}
