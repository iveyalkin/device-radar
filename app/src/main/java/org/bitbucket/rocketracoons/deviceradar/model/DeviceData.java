package org.bitbucket.rocketracoons.deviceradar.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class DeviceData {
    @SerializedName("name") public String name;
    @SerializedName("guid") public String guid;
    @SerializedName("versionOS") public String osVersion;
    @SerializedName("longitude") public long longitude;
    @SerializedName("latitude") public long latitude;
    @SerializedName("ssid") public String ssid;
    @SerializedName("mac") public String macAddress;
    @SerializedName("pushToken") public String pushToken;
}
