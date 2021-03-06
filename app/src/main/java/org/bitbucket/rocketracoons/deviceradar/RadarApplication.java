package org.bitbucket.rocketracoons.deviceradar;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.bitbucket.rocketracoons.deviceradar.network.HackedCallback;
import org.bitbucket.rocketracoons.deviceradar.network.model.RegisterTokenRequest;
import org.bitbucket.rocketracoons.deviceradar.task.GcmRegistrationTask;
import org.bitbucket.rocketracoons.deviceradar.utility.Constants;
import org.bitbucket.rocketracoons.deviceradar.utility.DataCollector;
import org.bitbucket.rocketracoons.deviceradar.utility.GcmSupportedType;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;
import org.bitbucket.rocketracoons.deviceradar.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class RadarApplication extends Application {
    private static final String TAG = RadarApplication.class.getSimpleName();

    private static final String GCM_PROPERTY_REG_ID = "gcm_property_reg_id";
//    private static final String USER_TOKEN = "user_token";
    public static final String DEVICE_REGISTERED_PREFERENCE_NAME = "device_registered_preference_name";
    public static final String DEVICE_NAME_PREFERENCE_NAME = "device_name_preference_name";
    private static final String PROPERTY_APP_VERSION = "appVersion";
//    private static final String USER_ROLE = "user_role";

    public static RadarApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        tryRegisterDevice();
    }

    public void tryRegisterDevice() {
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
                    Utility.getApiClient().registerPushToken(request, new HackedCallback<Object>() {
                        @Override
                        public void success(Object result, Response response) {
                            Logger.d(TAG, "Registration GCM token success (our side)");
                            storeRegistrationId(registrationId);
                        }

                        @Override
                        protected void handleError(RetrofitError retrofitError) {
                            Logger.e(TAG, "Registration token failed (our side)", retrofitError);
                        }

                        @Override
                        protected void repeat() {
                            Utility.getApiClient().registerPushToken(request, this);
                        }
                    });
                }
            }
        }.execute();
    }

    public String getDeviceGuid() {
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

    private static String sUserToken;
    private static String sUserRole;
    public String getUserToken() {
//        return getSharedPreferences().getString(USER_TOKEN, "");
        return sUserToken;
    }

    public void setUserToken(String token) {
//        getSharedPreferences().edit()
//                .putString(USER_TOKEN, token)
//                .apply();
        sUserToken = token;
    }

    public void setUserRole(String role) {
//        getSharedPreferences().edit()
//                .putString(USER_ROLE, role)
//                .apply();
        sUserRole = role;
    }

    public String getUserRole() {
//        return getSharedPreferences().getString(USER_ROLE, "");
        return sUserRole;
    }

    public double[] getLocation() {
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ArrayList<Location> locations = new ArrayList<Location>();
        List<String> providers = locationManager.getAllProviders();
        for (final String provider: providers) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (null != location) {
                locations.add(location);
            } else {
                Logger.w(TAG, "Last known location not found for provider " + provider);
            }
        }

        if (locations.size() != 0) {
            Location bestLocation = locations.get(0);
            for (Location location: locations) {
                if (location.getAccuracy() > bestLocation.getAccuracy()) {
                    bestLocation = location;
                }
            }
            final double[] coordinates = {bestLocation.getLatitude(), bestLocation.getLongitude()};
            Logger.d(TAG, "Found last known location {" + coordinates[0] + "; "
                    + coordinates[1] + "}");
            return coordinates;
        } else {
            return new double[]{0.0d, 0.0d};
        }
    }
}
