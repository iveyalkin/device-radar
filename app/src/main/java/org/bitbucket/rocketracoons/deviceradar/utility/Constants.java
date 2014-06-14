package org.bitbucket.rocketracoons.deviceradar.utility;

import android.net.ConnectivityManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class Constants {
    public static final List<Integer> LISTENING_CONNECTIVITY_EVENTS =
            Arrays.asList(ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI);

    public static final String SENDER_ID = "462794445107";

    public static final boolean DEBUG_MODE = true;

    public enum Config {
        LIVE("http://10.168.1.90", 8080),
        STAGE("http://10.168.1.90", 8081);

        private static final String API_PATH = "/api";

        private final String url;
        private final int port;

        Config(String url, int port) {
            this.url = url;
            this.port = port;
        }

        public static final Config getCurrentConfig() {
            return LIVE;
        }

        public String getUrl() {
            return url + ":" + String.valueOf(port) + API_PATH;
        }
    }
}
