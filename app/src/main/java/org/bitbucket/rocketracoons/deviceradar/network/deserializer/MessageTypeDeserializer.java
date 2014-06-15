package org.bitbucket.rocketracoons.deviceradar.network.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.bitbucket.rocketracoons.deviceradar.model.Message;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;

import java.lang.reflect.Type;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class MessageTypeDeserializer implements JsonDeserializer<Message.Type>,
                                                JsonSerializer<Message.Type> {
    private static final String TAG = MessageTypeDeserializer.class.getSimpleName();

    @Override
    public Message.Type deserialize(JsonElement jsonElement,
                                    Type type,
                                    JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive asJsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (asJsonPrimitive.isString()) {
                try {
                    return Message.Type.valueOf(asJsonPrimitive.getAsString().toUpperCase());
                } catch (IllegalArgumentException iae) {
                    Logger.e(TAG, "Undefined message type: " + asJsonPrimitive.getAsString(), iae);
                }
            }
        }
        Logger.e(TAG, "Unexpected message type: " + jsonElement.toString());
        return Message.Type.INBOUND;
    }

    @Override
    public JsonElement serialize(Message.Type messageType,
                                 Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(messageType.name());
    }
}
