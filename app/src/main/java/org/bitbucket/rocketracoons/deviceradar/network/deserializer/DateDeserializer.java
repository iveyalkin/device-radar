package org.bitbucket.rocketracoons.deviceradar.network.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.bitbucket.rocketracoons.deviceradar.utility.Logger;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class DateDeserializer implements JsonDeserializer<Date>, JsonSerializer<Date> {
    private static final String TAG = DateDeserializer.class.getSimpleName();

    @Override
    public Date deserialize(JsonElement json,
                            Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();
            try {
                return new Date(primitive.getAsLong() * 1000); // UNIX to millisec
            } catch (NumberFormatException nfe) {
                Logger.w(TAG, "Failed to parse Date, unexpected JSON primitive: " + primitive);
            }
        }
        return new Date();
    }

    @Override
    public JsonElement serialize(Date date,
                                 Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(date.getTime());
    }
}
