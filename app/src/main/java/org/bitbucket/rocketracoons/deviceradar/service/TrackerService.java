package org.bitbucket.rocketracoons.deviceradar.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import org.bitbucket.rocketracoons.deviceradar.model.Device;
import org.bitbucket.rocketracoons.deviceradar.model.DeviceData;
import org.bitbucket.rocketracoons.deviceradar.network.ApiClient;
import org.bitbucket.rocketracoons.deviceradar.network.HackedCallback;
import org.bitbucket.rocketracoons.deviceradar.utility.DataCollector;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;
import org.bitbucket.rocketracoons.deviceradar.utility.Utility;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class TrackerService extends IntentService {
    private static final String TAG = TrackerService.class.getSimpleName();

    // FIXME: not queue work! only one at a time!
    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static Intent getShortCollectingIntent(Context context) {
        Intent intent = new Intent(context, TrackerService.class);
        context.startService(intent);
        return intent;
    }

    public TrackerService() {
        super(TrackerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.v(TAG, "Handle intent service for: " + intent);
        updateDeviceData(DataCollector.collectShortDeviceInformation(), intent);
    }

    private void updateDeviceData(final DeviceData data, final Intent intent) {
        Logger.v(TAG, "Updating device data: " + data);

        final ApiClient apiClient = Utility.getApiClient();
        apiClient.updateDeviceData(data.guid, data, new HackedCallback<Device>() {
            @Override
            public void success(Device device, Response response) {
                Logger.v(TAG, "Update success for: " + device);
                WakefulBroadcastReceiver.completeWakefulIntent(intent);
            }

            @Override
            protected void handleError(RetrofitError retrofitError) {
                Logger.v(TAG, "Update failure for: " + data + " with: " + retrofitError);
                WakefulBroadcastReceiver.completeWakefulIntent(intent);
            }

            @Override
            protected void repeat() {
                apiClient.updateDeviceData(data.guid, data, this);
            }
        });
    }
}
