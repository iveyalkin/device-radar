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

        return new ExtendedDeviceData("", "", "", 0l, 0l, "", "", "", 0,
                0, false, "");
    }

    public static DeviceData collectShortDeviceInformation() {
        Logger.v(TAG, "Collecting short data");

        return new DeviceData("", "", "", 0l, 0l, "", "", "");

    }
}
