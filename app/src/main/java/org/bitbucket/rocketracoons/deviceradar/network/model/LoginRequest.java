package org.bitbucket.rocketracoons.deviceradar.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Stenopolz on 15.06.2014.
 */
public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 8127287304102400302L;

    @SerializedName("username")
    public String login;
    @SerializedName("password")
    public String password;

    private LoginRequest() {
    }

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
