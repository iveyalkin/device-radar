package org.bitbucket.rocketracoons.deviceradar.utility;

import android.util.Log;

import retrofit.RestAdapter;

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

    public static class Retrofit implements RestAdapter.Log {
        private static final String TAG = Retrofit.class.getSimpleName();

        private static final int LOG_CHUNK_SIZE = 4000;

        @Override
        public void log(String message) {
            for (int i = 0, len = message.length(); i < len; i += LOG_CHUNK_SIZE) {
                int end = Math.min(len, i + LOG_CHUNK_SIZE);
                logChunk(message.substring(i, end));
            }
        }

        /**
         * Called one or more times for each call to {@link #log(String)}. The length of {@code chunk}
         * will be no more than 4000 characters to support Android's {@link android.util.Log} class.
         */
        public void logChunk(String chunk) {
            Logger.d(TAG, chunk);
        }
    }
}
