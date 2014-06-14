package org.bitbucket.rocketracoons.deviceradar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class Device {
    @SerializedName("name") public String name;
    @SerializedName("versionOS") public String osVersion;
}
