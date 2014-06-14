package org.bitbucket.rocketracoons.deviceradar;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.JsonElement;

import org.bitbucket.rocketracoons.deviceradar.network.model.RegisterTokenRequest;
import org.bitbucket.rocketracoons.deviceradar.task.GcmRegistrationTask;
import org.bitbucket.rocketracoons.deviceradar.utility.Constants;
import org.bitbucket.rocketracoons.deviceradar.utility.DataCollector;
import org.bitbucket.rocketracoons.deviceradar.utility.GcmSupportedType;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;
import org.bitbucket.rocketracoons.deviceradar.utility.Utility;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class RadarApplication extends Application {
    private static final String TAG = RadarApplication.class.getSimpleName();

    private static final String GCM_PROPERTY_REG_ID = "gcm_property_reg_id";
    public static String DEVICE_REGISTERED_PREFERENCE_NAME = "device_registered_preference_name";
    public static String DEVICE_NAME_PREFERENCE_NAME = "device_name_preference_name";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    public static RadarApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        tryRegisterDevice();
    }

    private void tryRegisterDevice() {
        if (GcmSupportedType.SUPPORTED == checkGooglePlayServices()) {
            final String registrationId = getRegistrationId();

            if (TextUtils.isEmpty(registrationId)) {
                registerInBackground();
            }
        } else {
            Logger.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    public GcmSupportedType checkGooglePlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(
                getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Logger.i(TAG, "This device is supported by GCM, by user action required.");
                return GcmSupportedType.SUPPORTED_BUT_USER;
            } else {
                Logger.i(TAG, "This device is not supported by GCM.");
                return GcmSupportedType.UNSUPPORTED;
            }
        }
        return GcmSupportedType.SUPPORTED;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public String getRegistrationId() {
        final SharedPreferences prefs = getSharedPreferences();
        String registrationId = prefs.getString(GCM_PROPERTY_REG_ID, "");
        if (TextUtils.isEmpty(registrationId)) {
            Logger.i(TAG, "Registration not found.");
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            Logger.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public int getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(RadarApplication.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new GcmRegistrationTask(this, Constants.SENDER_ID){
            @Override
            protected void onPostExecute(final String registrationId) {
                if (!TextUtils.isEmpty(registrationId)) {
                    if (!isDeviceRegistered()) {
                        Logger.d(TAG, "Device is not registered. Doesn't bother backend");
                        return;
                    }
                    final RegisterTokenRequest request = new RegisterTokenRequest(getDeviceGuid(),
                            registrationId);
                    final JsonElement json = Utility.getGsonInstance().toJsonTree(request,
                            RegisterTokenRequest.class);
                    Utility.getApiClient().registerPushToken(json, new Callback<String>() {
                        @Override
                        public void success(String result, Response response) {
//                            if (registerTokenRequest.isSuccess) {
                                Logger.d(TAG, "Registration GCM token success (our side)");
                                // Persist the regID - no need to register again.
                                storeRegistrationId(registrationId);
                            /*} else {
                                Logger.w(TAG, "Registration GCM token failed (our side): " +
                                        registerTokenRequest);
                            }*/
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Logger.e(TAG, "Registration token failed (our side)", retrofitError);
                        }
                    });
                }
            }
        }.execute();
    }

    private String getDeviceGuid() {
        return DataCollector.collectDeviceGUID();
    }

    private void storeRegistrationId(String registrationId) {
        int appVersion = getAppVersion();
        Logger.i(TAG, "Saving GCM registrationId on app version " + appVersion);
        getSharedPreferences().edit()
                .putString(GCM_PROPERTY_REG_ID, registrationId)
                .putInt(PROPERTY_APP_VERSION, appVersion)
                .apply();
    }

    public boolean isDeviceRegistered() {
        return getSharedPreferences().getBoolean(DEVICE_REGISTERED_PREFERENCE_NAME, false);
    }

    public void setDeviceRegistered(boolean isRegistered) {
        if (isDeviceRegistered() == isRegistered) {
            return;
        }

        getSharedPreferences().edit()
                .putBoolean(DEVICE_REGISTERED_PREFERENCE_NAME, isRegistered)
                .apply();

        if (isRegistered) {
            tryRegisterDevice();
        }
    }

    public void setDeviceName(String name) {
        getSharedPreferences().edit()
                .putString(DEVICE_NAME_PREFERENCE_NAME, name)
                .apply();
    }

    public String getDeviceName() {
        return getSharedPreferences().getString(DEVICE_NAME_PREFERENCE_NAME, "Unknown");
    }
}
