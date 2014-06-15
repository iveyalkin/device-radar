package org.bitbucket.rocketracoons.deviceradar.network.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Igor.Veyalkin on 15.06.2014.
 */
public class SendMessageRequest implements Serializable {
    private static final long serialVersionUID = -376612939372121088L;

    @SerializedName("guid") public String guid;
    @SerializedName("text") public String text;
    @SerializedName("authorId") public String authorId;

    public SendMessageRequest(String guid, String text, String authorId) {
        this.guid = guid;
        this.text = text;
        this.authorId = authorId;
    }
}
