package org.bitbucket.rocketracoons.deviceradar.receiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.WakefulBroadcastReceiver;

import org.bitbucket.rocketracoons.deviceradar.service.TrackerService;

public class DeviceStateReceiver extends WakefulBroadcastReceiver {
    public DeviceStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiState != null) {
            if (wifiState.isConnected()) {
                //start service
                ComponentName comp = new ComponentName(context.getPackageName(),
                        TrackerService.class.getName());
                // Start the service, keeping the device awake while it is launching.
                startWakefulService(context, (intent.setComponent(comp)));
            } else {
                //stop service
                // TODO: notify that device has lost connection?
            }

            setResultCode(Activity.RESULT_OK);
        }
    }
}
