package org.bitbucket.rocketracoons.deviceradar.utility;

import android.content.Context;
import android.graphics.Point;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;

import org.bitbucket.rocketracoons.deviceradar.RadarApplication;
import org.bitbucket.rocketracoons.deviceradar.model.DeviceData;
import org.bitbucket.rocketracoons.deviceradar.model.ExtendedDeviceData;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String getTotalRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Streams.close(reader);
        }

        return lastValue;
    }
}
