package org.bitbucket.rocketracoons.deviceradar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.bitbucket.rocketracoons.deviceradar.network.model.PushNotification;
import org.bitbucket.rocketracoons.deviceradar.screen.MessagesActivity;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;
import org.bitbucket.rocketracoons.deviceradar.utility.Utility;

/**
 * Created by Igor.Veyalkin on 14.06.2014.
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = GcmBroadcastReceiver.class.getSimpleName();
    private static final String GCM_PAYLOAD = "message";
    private static final int REQUEST_CODE = 0xff00;
    private static final int DEFAULT_NOTIFICATION_ID = 0xff00;

    public static final String EVENT_PUSH_RECEIVED = "event_push_received";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            final String messageType = gcm.getMessageType(intent);
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Logger.e(TAG, "GCM send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                Logger.d(TAG, "Deleted GCM messages on server: " +
                        extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.d(TAG, "Got GCM message: " + extras.toString());
                proccessGcmMessage(context, extras);
            }
        }
    }

    private void proccessGcmMessage(Context context, Bundle gcmMessage) {
        PushNotification pushNotification = Utility.getGsonInstance().fromJson(
                gcmMessage.getString(GCM_PAYLOAD), PushNotification.class);
        if (null == pushNotification) {
            Logger.w(TAG, "Unexpected gcm message");
            return;
        }

        MessageProvider.addMessage(pushNotification.authorId, pushNotification.message);
        final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        manager.sendBroadcast(new Intent(GcmBroadcastReceiver.EVENT_PUSH_RECEIVED));

        raiseNotification(context, pushNotification);
    }

    private void raiseNotification(Context context, PushNotification pushNotification) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent messageActivity = new Intent(context, MessagesActivity.class);
        messageActivity.putExtra(MessagesActivity.ARG_AUTHOR_ID, pushNotification.authorId);
        PendingIntent contentIntent = PendingIntent.getActivity(context, REQUEST_CODE,
                messageActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.ic_popup_sync)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(pushNotification.message.message))
                        .setAutoCancel(true)
                        .setContentText(pushNotification.message.message);

        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify(DEFAULT_NOTIFICATION_ID, mBuilder.build());
    }
}
