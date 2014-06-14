package org.bitbucket.rocketracoons.deviceradar.utility;

import org.bitbucket.rocketracoons.deviceradar.model.DeviceData;
import org.bitbucket.rocketracoons.deviceradar.model.ExtendedDeviceData;

/**
 * Created by Stenopolz on 14.06.2014.
 */
public class DataCollector {
    private static final String TAG = DataCollector.class.getSimpleName();

    public static ExtendedDeviceData collectCompleteDeviceInformation() {
        Logger.v(TAG, "Collecting complete data");

        return new ExtendedDeviceData("Android-004", collectDeviceGUID(), "dfg", 0l, 0l, "sadfga", "ASDF", "sdf", 1024,
                15, false, "hdpi");
    }

    public static DeviceData collectShortDeviceInformation() {
        Logger.v(TAG, "Collecting short data");

        return new DeviceData("", collectDeviceGUID(), "", 0l, 0l, "", "", "");
    }

    public static String collectDeviceGUID() {
        Logger.v(TAG, "Collecting GUID");
        return "666";
    }
}
