package org.bitbucket.rocketracoons.deviceradar.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 */
public class DeviceData implements Serializable {
    private static final long serialVersionUID = -2883171303408492196L;

    @SerializedName("name") public String name;
    @SerializedName("guid") public String guid;
    @SerializedName("versionOS") public String osVersion;
    @SerializedName("longitude") public double longitude;
    @SerializedName("latitude") public double latitude;
    @SerializedName("ssid") public String ssid;
    @SerializedName("mac") public String macAddress;
    @SerializedName("pushToken") public String pushToken;

    protected DeviceData() {
    }

    public DeviceData(String name,
                      String guid,
                      String osVersion,
                      double latitude,
                      double longitude,
                      String ssid,
                      String macAddress,
                      String pushToken) {
        this.name = name;
        this.guid = guid;
        this.osVersion = osVersion;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ssid = ssid;
        this.macAddress = macAddress;
        this.pushToken = pushToken;
    }
}
