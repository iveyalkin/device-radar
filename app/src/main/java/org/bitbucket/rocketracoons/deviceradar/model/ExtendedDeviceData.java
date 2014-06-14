package org.bitbucket.rocketracoons.deviceradar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class ExtendedDeviceData extends DeviceData {
    private static final long serialVersionUID = 6365263726585438753L;

    @SerializedName("ram") public String ramAmount;
    @SerializedName("internalMemory") public String memoryAmount;
    @SerializedName("gsm") public Boolean gsmModule;
    @SerializedName("screen") public String screen;
}
