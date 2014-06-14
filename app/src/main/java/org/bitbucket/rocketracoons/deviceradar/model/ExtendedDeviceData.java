package org.bitbucket.rocketracoons.deviceradar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class ExtendedDeviceData extends DeviceData {
    @SerializedName("ram") public String ramAmount;
    @SerializedName("memory") public String memoryAmount;
    @SerializedName("gsm") public Boolean gsmModule;
    @SerializedName("screen") public String screen;
}
