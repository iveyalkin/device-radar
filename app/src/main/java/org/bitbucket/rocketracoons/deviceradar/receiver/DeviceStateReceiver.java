package org.bitbucket.rocketracoons.deviceradar.receiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import org.bitbucket.rocketracoons.deviceradar.service.TrackerService;
import org.bitbucket.rocketracoons.deviceradar.utility.Constants;

public class DeviceStateReceiver extends WakefulBroadcastReceiver {

    public static final int UNDEFINED_TYPE = -1;

    public DeviceStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (processIntent(intent)) {
            ComponentName comp = new ComponentName(context.getPackageName(),
                    TrackerService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
        }
        setResultCode(Activity.RESULT_OK);
    }

    private boolean processIntent(Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.getAction()) {
            final int networkType = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE,
                    UNDEFINED_TYPE);
            return Constants.LISTENING_CONNECTIVITY_EVENTS.contains(networkType);
        } else {
            return true;
        }
    }
}
