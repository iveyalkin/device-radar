package org.bitbucket.rocketracoons.deviceradar.utility;

import android.util.Log;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class Logger {
    public static final void i(String tag, String message) {
        if (Constants.DEBUG_MODE) {
            Log.i(tag, message);
        }
    }

    public static final void i(String tag, String message, Throwable throwable) {
        if (Constants.DEBUG_MODE) {
            Log.i(tag, message, throwable);
        }
    }

    public static final void v(String tag, String message) {
        if (Constants.DEBUG_MODE) {
            Log.v(tag, message);
        }
    }

    public static final void v(String tag, String message, Throwable throwable) {
        if (Constants.DEBUG_MODE) {
            Log.v(tag, message, throwable);
        }
    }

    public static final void d(String tag, String message) {
        if (Constants.DEBUG_MODE) {
            Log.d(tag, message);
        }
    }

    public static final void d(String tag, String message, Throwable throwable) {
        if (Constants.DEBUG_MODE) {
            Log.d(tag, message, throwable);
        }
    }

    public static final void w(String tag, String message) {
        if (Constants.DEBUG_MODE) {
            Log.w(tag, message);
        }
    }

    public static final void w(String tag, String message, Throwable throwable) {
        if (Constants.DEBUG_MODE) {
            Log.w(tag, message, throwable);
        }
    }

    public static final void e(String tag, String message) {
        if (Constants.DEBUG_MODE) {
            Log.e(tag, message);
        }
    }

    public static final void e(String tag, String message, Throwable throwable) {
        if (Constants.DEBUG_MODE) {
            Log.e(tag, message, throwable);
        }
    }
}
