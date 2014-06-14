package org.bitbucket.rocketracoons.deviceradar.network;

import org.bitbucket.rocketracoons.deviceradar.model.Device;
import org.bitbucket.rocketracoons.deviceradar.model.DeviceData;
import org.bitbucket.rocketracoons.deviceradar.model.ExtendedDeviceData;
import org.bitbucket.rocketracoons.deviceradar.network.model.RegisterTokenRequest;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public interface ApiClient {
    @GET("/device")
    void getDevicesList(Callback<ArrayList<Device>> callback);

    @PUT("/update/{guid}")
    void updateDeviceData(@Path("guid") String deviceGuid, @Body DeviceData deviceData,
                          Callback<Device> callback);

    @POST("/device")
    void registerDevice(@Body ExtendedDeviceData deviceData, Callback<Device> callback);

    @POST("/register")
    Device registerDevice(@Body ExtendedDeviceData deviceData);

    @POST("/register/push/{guid}")
    void registerPushToken(@Path("guid") String guid, @Body String pushId,
                           Callback<RegisterTokenRequest> callback);

    @DELETE("/device/{guid}")
    void unregisterDevice(@Path("guid") String deviceGuid, Callback<Device> callback);
}
