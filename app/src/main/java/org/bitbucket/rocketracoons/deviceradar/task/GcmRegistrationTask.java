package org.bitbucket.rocketracoons.deviceradar.task;

import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.bitbucket.rocketracoons.deviceradar.RadarApplication;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;

import java.io.IOException;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public abstract class GcmRegistrationTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = GcmRegistrationTask.class.getSimpleName();

    private final RadarApplication context;
    private final String senderId;

    public GcmRegistrationTask(RadarApplication appContext, String senderId) {
        context = appContext;
        this.senderId = senderId;
    }

    @Override
    protected final String doInBackground(Void... params) {
        try {
            final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
            final String registrationId = gcm.register(senderId);
            Logger.d(TAG, "GCM registration success (Google side): registrationID=" + registrationId);
            return registrationId;
        } catch (IOException ex) {
            Logger.d(TAG, "GCM registration error: " + ex.getMessage());
            return "";
        }
    }

    @Override
    protected abstract void onPostExecute(String registrationId);
}
