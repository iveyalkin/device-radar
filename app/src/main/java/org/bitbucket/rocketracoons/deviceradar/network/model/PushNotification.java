package org.bitbucket.rocketracoons.deviceradar.network.model;

import com.google.gson.annotations.SerializedName;

import org.bitbucket.rocketracoons.deviceradar.model.Message;

import java.io.Serializable;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class PushNotification implements Serializable {
    private static final long serialVersionUID = -8851064191604530172L;

    @SerializedName("authorId") public String authorId;
    @SerializedName("message") public Message message;

    private PushNotification() {}
}
