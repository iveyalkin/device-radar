package org.bitbucket.rocketracoons.deviceradar.network.model;

import com.google.gson.annotations.SerializedName;

import org.bitbucket.rocketracoons.deviceradar.model.Message;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class PushNotification implements Serializable {
    private static final long serialVersionUID = -8851064191604530172L;

    @SerializedName("author") public String authorId;
    @SerializedName("text") public Message message;
    @SerializedName("date") public Date date;

    private PushNotification() {}
}
