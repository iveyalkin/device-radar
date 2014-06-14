package org.bitbucket.rocketracoons.deviceradar.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class DeviceData {
    @SerializedName("name") public String name;
    @SerializedName("guid") public String guid;
    @SerializedName("versionOS") public String osVersion;
    @SerializedName("location") public String location;
    @SerializedName("ssid") public String ssid;
    @SerializedName("macAddress") public String macAddress;
}
