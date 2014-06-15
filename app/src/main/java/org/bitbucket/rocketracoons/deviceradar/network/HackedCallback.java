package org.bitbucket.rocketracoons.deviceradar.network;

import org.bitbucket.rocketracoons.deviceradar.utility.Logger;

import java.io.EOFException;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by Igor.Veyalkin on 15.06.2014.
 */
public abstract class HackedCallback<T> implements Callback<T> {
    private static final String TAG = HackedCallback.class.getSimpleName();

    @Override
    public final void failure(RetrofitError retrofitError) {
        if (checkHack(retrofitError)) {
            Logger.w(TAG, "Request failed with EOF", retrofitError);
            repeat();
        } else {
            handleError(retrofitError);
        }
    }

    protected abstract void handleError(RetrofitError retrofitError);

    protected abstract void repeat();

    protected final boolean checkHack(RetrofitError retrofitError) {
        Throwable cause = retrofitError.getCause();
        return null != cause && cause.getClass().equals(EOFException.class);
    }
}
