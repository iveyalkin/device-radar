package org.bitbucket.rocketracoons.deviceradar.utility;

import android.content.Context;
import android.graphics.Point;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;

import org.bitbucket.rocketracoons.deviceradar.RadarApplication;
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
        return Settings.Secure.getString(RadarApplication.instance.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    private static String getScreenResolution() {
        WindowManager wm = (WindowManager) RadarApplication.instance.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return String.valueOf(size.x) + "x" + String.valueOf(size.y);
    }
}
