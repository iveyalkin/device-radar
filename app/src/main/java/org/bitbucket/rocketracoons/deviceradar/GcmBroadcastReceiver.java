package org.bitbucket.rocketracoons.deviceradar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: mockup
        setResultCode(Activity.RESULT_OK);
    }
}
