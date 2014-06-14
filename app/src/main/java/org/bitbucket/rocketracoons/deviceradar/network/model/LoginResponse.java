package org.bitbucket.rocketracoons.deviceradar.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Stenopolz on 15.06.2014.
 */
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = 2952365021990455490L;

    @SerializedName("sessionId")
    public String sessionId;
    @SerializedName("role")
    public String role;


    private LoginResponse() {
    }
}
