package org.bitbucket.rocketracoons.deviceradar.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 672135616360560604L;

    @SerializedName("date") public Date date;
    @SerializedName("author") public String authorName;
    @SerializedName("type") public Type type = Type.INBOUND;
    @SerializedName("text") public String message;

    public enum Type {
        INBOUND, OUTBOUND
    }

    private Message() {}


}
