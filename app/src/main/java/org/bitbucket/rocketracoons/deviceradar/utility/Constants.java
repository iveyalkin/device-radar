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
}
