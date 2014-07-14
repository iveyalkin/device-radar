package org.bitbucket.rocketracoons.deviceradar.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Igor.Veyalkin on 7/10/2014.
 */
@Table(name = "Location")
public class LocationData extends Model implements Serializable {
    private static final long serialVersionUID = 1530661517089630985L;

    @Column(name = "longitude")
    @SerializedName("longitude")
    @Expose
    public double longitude;

    @Column(name = "latitude")
    @SerializedName("latitude")
    @Expose
    public double latitude;

    @Column(name = "ssid")
    @SerializedName("ssid")
    @Expose
    public String ssid;

    @Column(name = "mac")
    @SerializedName("mac")
    @Expose
    public String macAddress;

    /** For internal use purposes only **/
    public LocationData() {
    }

    public LocationData(double longitude, double latitude, String ssid, String macAddress) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.ssid = ssid;
        this.macAddress = macAddress;
    }
}
