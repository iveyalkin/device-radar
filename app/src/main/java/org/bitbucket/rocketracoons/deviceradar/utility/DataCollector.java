package org.bitbucket.rocketracoons.deviceradar.utility;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Stenopolz on 14.06.2014.
 */
public class DataCollector {
    private static final String TAG = DataCollector.class.getSimpleName();

    public static ExtendedDeviceData collectCompleteDeviceInformation() {
        Logger.v(TAG, "Collecting complete data");
        WifiInfo wifiInfo = getWifiInfo();
        return new ExtendedDeviceData(RadarApplication.instance.getDeviceName(), collectDeviceGUID(),
                "Android "+ Build.VERSION.RELEASE, 01, 01, wifiInfo.getSSID(),
                wifiInfo.getMacAddress(),
                RadarApplication.instance.getRegistrationId(),
                getTotalRAM(), getInternalStorageSpace(),
                hasMobileNetwork(), getScreenResolution());
    }

    public static DeviceData collectShortDeviceInformation() {
        Logger.v(TAG, "Collecting short data");
        WifiInfo wifiInfo = getWifiInfo();
        return new DeviceData(RadarApplication.instance.getDeviceName(), collectDeviceGUID(),
                "Android "+ Build.VERSION.RELEASE, 01, 0l, wifiInfo.getSSID(),
                wifiInfo.getMacAddress(),
                RadarApplication.instance.getRegistrationId());
    }

    public static String collectDeviceGUID() {
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

    private static int getTotalRAM() {
        RandomAccessFile reader;
        String load;
        double totRam;
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
            }
            reader.close();

            totRam = Double.parseDouble(value);

            double mb = totRam / 1024.0;
            lastValue = (int) Math.round(mb);


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Streams.close(reader);
        }

        return lastValue;
    }

    private static int getInternalStorageSpace() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        long total = ((long) statFs.getBlockCount() * (long) statFs.getBlockSize()) / (1024L * 1024L * 1024L);
        return (int) total;
    }

    private static boolean hasMobileNetwork() {
        ConnectivityManager cm = (ConnectivityManager) RadarApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo.isAvailable();
    }

    private static WifiInfo getWifiInfo() {
        WifiManager wifiManager = (WifiManager) RadarApplication.instance.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getConnectionInfo();
    }
}