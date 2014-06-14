package org.bitbucket.rocketracoons.deviceradar.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class RegisterTokenRequest implements Serializable {
    private static final long serialVersionUID = -5919734431426525558L;

    @SerializedName("resultState") public boolean isSuccess;
}
