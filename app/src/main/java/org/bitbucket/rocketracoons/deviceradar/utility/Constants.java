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

    public static final String URL = "http://10.168.1.90:8080/api";

    public static final boolean DEBUG_MODE = true;

    public static String DEVICE_REGISTERED_PREFERENCE_NAME = "DEVICE_REGISTERED_PREFERENCE_NAME";
}
