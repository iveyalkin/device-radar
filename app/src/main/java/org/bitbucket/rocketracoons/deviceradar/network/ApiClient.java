package org.bitbucket.rocketracoons.deviceradar.network;

import org.bitbucket.rocketracoons.deviceradar.model.Device;
import org.bitbucket.rocketracoons.deviceradar.model.DeviceData;
import org.bitbucket.rocketracoons.deviceradar.model.ExtendedDeviceData;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public interface ApiClient {
    @GET("/device")
    void getDevicesList(Callback<ArrayList<Device>> callback);

    @PUT("/update/{guid}")
    void updateDeviceData(@Path("guid") String deviceGuid, @Body DeviceData deviceData, Callback<Device> callback);

    @PUT("/register")
    void registerDevice(@Body ExtendedDeviceData deviceData, Callback<Device> callback);
}
