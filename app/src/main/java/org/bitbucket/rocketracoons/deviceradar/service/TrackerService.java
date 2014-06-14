package org.bitbucket.rocketracoons.deviceradar.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import org.bitbucket.rocketracoons.deviceradar.model.Device;
import org.bitbucket.rocketracoons.deviceradar.model.DeviceData;
import org.bitbucket.rocketracoons.deviceradar.model.ExtendedDeviceData;
import org.bitbucket.rocketracoons.deviceradar.network.ApiClient;
import org.bitbucket.rocketracoons.deviceradar.utility.Constants;
import org.bitbucket.rocketracoons.deviceradar.utility.Utility;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class TrackerService extends IntentService {
    private static final String ACTION_COLLECT_SHORT =
            "org.bitbucket.rocketracoons.deviceradar.action.COLLECT_SHORT";
    private static final String ACTION_COLLECT_COMPLETE =
            "org.bitbucket.rocketracoons.deviceradar.action.COLLECT_COMPLETE";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // FIXME: not queue work! only one at a time!
    public static Intent getCompleteCollectingIntent(Context context) {
        Intent intent = new Intent(context, TrackerService.class);
        intent.setAction(ACTION_COLLECT_SHORT);
        return intent;
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static Intent getShortCollectingIntent(Context context) {
        Intent intent = new Intent(context, TrackerService.class);
        intent.setAction(ACTION_COLLECT_COMPLETE);
        context.startService(intent);
        return intent;
    }

    public TrackerService() {
        super(TrackerService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (null != intent) {
            final String action = intent.getAction();
            if (ACTION_COLLECT_SHORT.equals(action)) {
                collectShortDeviceInformation();
            } else if (ACTION_COLLECT_COMPLETE.equals(action)) {
                collectCompleteDeviceInformation();
            }
        } else {
            collectCompleteDeviceInformation();
        }
    }

    private void collectCompleteDeviceInformation() {
        final ExtendedDeviceData device = new ExtendedDeviceData("", "", "", 0l, 0l, "", "", "", 0,
                0, false, "");
        registerDevice(device);
    }

    private void collectShortDeviceInformation() {
        final DeviceData device = new DeviceData("", "", "", 0l, 0l, "", "", "");
        updateDevicceData(device);
    }

    private void registerDevice(ExtendedDeviceData device) {
        final ApiClient apiClient = Utility.getApiClient();
        apiClient.registerDevice(device, new Callback<Device>() {
            @Override
            public void success(Device device, Response response) {
                // TODO: stub
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                // TODO: stub
            }
        });
    }

    private void updateDevicceData(DeviceData data) {
        final ApiClient apiClient = Utility.getApiClient();
        apiClient.updateDeviceData(data.guid, data, new Callback<Device>() {
            @Override
            public void success(Device device, Response response) {
                // TODO: stub
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                // TODO: stub
            }
        });
    }
}
