package org.bitbucket.rocketracoons.deviceradar;

import org.bitbucket.rocketracoons.deviceradar.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Igor.Veyalkin on 15.06.2014.
 */
public class MessageProvider {
    private static final Map<String, TreeSet<Message>> sMessages =
            new HashMap<String, TreeSet<Message>>();

    public static final void addMessage(String authorId, Message message) {
        TreeSet<Message> messages = sMessages.get(authorId);
        if (null == messages) {
            messages = new TreeSet<Message>();
            sMessages.put(authorId, messages);
        }
        messages.add(message);
    }

    public static final Set<Message> getMessages(String authorId) {
        TreeSet<Message> messages = sMessages.get(authorId);
        return null != messages ? messages : new TreeSet();
    }

    public static final Map<String, Integer> getStats() {
        Map<String, Integer> stats = new TreeMap<String, Integer>();
        for (final Map.Entry<String, TreeSet<Message>> entry : sMessages.entrySet()) {
            stats.put(entry.getKey(), entry.getValue().size());
        }
        return stats;
    }
}
