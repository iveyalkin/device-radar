package org.bitbucket.rocketracoons.deviceradar.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class DeviceData extends LocationData {
    private static final long serialVersionUID = -2883171303408492196L;

    @SerializedName("name") public String name;
    @SerializedName("guid") public String guid;
    @SerializedName("versionOS") public String osVersion;
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
        super(longitude, latitude, ssid, macAddress);
        this.name = name;
        this.guid = guid;
        this.osVersion = osVersion;
        this.pushToken = pushToken;
    }
}
