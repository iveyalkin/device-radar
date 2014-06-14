package org.bitbucket.rocketracoons.deviceradar.utility;

import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.os.StatFs;
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

        return new ExtendedDeviceData("Android-004", collectDeviceGUID(), "dfg", 01, 01, "sadfga", "ASDF", "sdf", getTotalRAM(), getInternalStorageSpace(), false, "hdpi");
    }

    public static DeviceData collectShortDeviceInformation() {
        Logger.v(TAG, "Collecting short data");

        return new DeviceData("", collectDeviceGUID(), "", 01, 0l, "", "", "");
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

    public static int getTotalRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        int lastValue = 0;
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
            lastValue = (int)Math.round(mb);


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Streams.close(reader);
        }

        return lastValue;
    }

    public static int getInternalStorageSpace()
    {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        //StatFs statFs = new StatFs("/data");
        long total = ((long)statFs.getBlockCount() * (long)statFs.getBlockSize()) / 1024*1024*1024;
        return (int)total;
    }
}
