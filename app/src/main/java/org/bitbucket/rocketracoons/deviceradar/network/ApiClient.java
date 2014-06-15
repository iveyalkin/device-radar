package org.bitbucket.rocketracoons.deviceradar.network;


import org.bitbucket.rocketracoons.deviceradar.model.Device;
import org.bitbucket.rocketracoons.deviceradar.model.DeviceData;
import org.bitbucket.rocketracoons.deviceradar.model.ExtendedDeviceData;
import org.bitbucket.rocketracoons.deviceradar.model.Message;
import org.bitbucket.rocketracoons.deviceradar.network.model.RegisterTokenRequest;
import org.bitbucket.rocketracoons.deviceradar.network.model.SendMessageRequest;
import org.bitbucket.rocketracoons.deviceradar.network.model.LoginRequest;
import org.bitbucket.rocketracoons.deviceradar.network.model.LoginResponse;

import java.util.ArrayList;

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
    void getDevicesList(HackedCallback<ArrayList<Device>> callback);

    @PUT("/update/{guid}")
    void updateDeviceData(@Path("guid") String deviceGuid, @Body DeviceData deviceData,
                          HackedCallback<Device> callback);

    @POST("/messenger/send")
    void sendMessage(@Body SendMessageRequest request, HackedCallback<Message> callback);

    @POST("/device")
    void registerDevice(@Body ExtendedDeviceData deviceData, HackedCallback<Device> callback);

    // TODO: something like url encoded
    @POST("/messenger/registration")
    void registerPushToken(@Body RegisterTokenRequest request, HackedCallback<Object> callback);

    @DELETE("/device/{guid}")
    void unregisterDevice(@Path("guid") String deviceGuid, HackedCallback<Device> callback);

    @POST("/auth")
    void auth(@Body LoginRequest loginRequest, HackedCallback<LoginResponse> callback);
}
