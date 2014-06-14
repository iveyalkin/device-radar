package org.bitbucket.rocketracoons.deviceradar;

import android.app.Application;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class RadarApplication extends Application {
    public static RadarApplication instancenstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instancenstance = this;
    }
}
