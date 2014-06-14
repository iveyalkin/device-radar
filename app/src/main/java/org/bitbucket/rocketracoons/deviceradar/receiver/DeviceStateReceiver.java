package org.bitbucket.rocketracoons.deviceradar.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import org.bitbucket.rocketracoons.deviceradar.service.TrackerService;
import org.bitbucket.rocketracoons.deviceradar.utility.Constants;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;

public class DeviceStateReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = DeviceStateReceiver.class.getSimpleName();

    private static final int UNDEFINED_TYPE = -1;

    public DeviceStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.v(TAG, "Received broadcast: " + intent.getAction());
        if (processIntent(intent)) {
            Logger.v(TAG, "Launch track service");
            startWakefulService(context, TrackerService.getShortCollectingIntent(context));
        }
    }

    private boolean processIntent(Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.getAction()) {
            final int networkType = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE,
                    UNDEFINED_TYPE);

            Logger.v(TAG, "Handled Connectivity action for: " + networkType);

            return Constants.LISTENING_CONNECTIVITY_EVENTS.contains(networkType);
        } else {
            return true;
        }
    }
}
