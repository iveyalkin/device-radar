package org.bitbucket.rocketracoons.deviceradar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class Device extends ExtendedDeviceData {
    @SerializedName("date") public String updateDate;
}
