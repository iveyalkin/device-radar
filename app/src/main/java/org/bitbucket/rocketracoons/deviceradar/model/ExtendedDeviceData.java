package org.bitbucket.rocketracoons.deviceradar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class ExtendedDeviceData extends DeviceData {
    private static final long serialVersionUID = 6365263726585438753L;

    @SerializedName("ram") public int ramAmount;
    @SerializedName("internalMemory") public int memoryAmount;
    @SerializedName("gsm") public Boolean gsmModule;
    @SerializedName("screen") public String screen;
    @SerializedName("accessPointName") public String accessPointName;

    protected ExtendedDeviceData() {
        super();
    }

    public ExtendedDeviceData(String name,
                              String guid,
                              String osVersion,
                              double latitude,
                              double longitude,
                              String ssid,
                              String macAddress,
                              String pushToken,
                              int ramAmount,
                              int memoryAmount,
                              Boolean gsmModule,
                              String screen) {
        super(name, guid, osVersion, longitude, latitude, ssid, macAddress, pushToken);
        this.ramAmount = ramAmount;
        this.memoryAmount = memoryAmount;
        this.gsmModule = gsmModule;
        this.screen = screen;
    }
}
