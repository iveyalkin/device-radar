package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.bitbucket.rocketracoons.deviceradar.GcmBroadcastReceiver;
import org.bitbucket.rocketracoons.deviceradar.MessageProvider;
import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.RadarApplication;
import org.bitbucket.rocketracoons.deviceradar.model.Message;
import org.bitbucket.rocketracoons.deviceradar.network.HackedCallback;
import org.bitbucket.rocketracoons.deviceradar.network.model.SendMessageRequest;
import org.bitbucket.rocketracoons.deviceradar.screen.adapter.MessagesListAdapter;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;
import org.bitbucket.rocketracoons.deviceradar.utility.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MessagesActivity extends Activity {
    private static final String TAG = MessagesActivity.class.getSimpleName();

    private static final String PREFIX = MessagesActivity.class.getPackage().getName()
            + MessagesActivity.class.getSimpleName();
    public static final String ARG_AUTHOR_ID = PREFIX + ".argument.authorid";

    @InjectView(R.id.listView)
    ListView messagesListView;
    @InjectView(R.id.messageTextField)
    EditText messageTextField;
    @InjectView(R.id.sendButton)
    Button sendButton;

    private String threadAuthorId;
    private MessagesListAdapter adapter;
    private BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.setMessages(MessageProvider.getMessages(threadAuthorId));

            NotificationManager manager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(GcmBroadcastReceiver.DEFAULT_NOTIFICATION_ID);
        }
    };
    private IntentFilter localFilter = new IntentFilter(GcmBroadcastReceiver.EVENT_PUSH_RECEIVED);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.inject(this);

        Bundle extras = getIntent().getExtras();
        if (null != extras && extras.containsKey(ARG_AUTHOR_ID)) {
            threadAuthorId = extras.getString(ARG_AUTHOR_ID);
            Logger.w(TAG, "Show thread with authorId: " + threadAuthorId);
            adapter = new MessagesListAdapter(this, MessageProvider.getMessages(threadAuthorId));
            messagesListView.setAdapter(adapter);
        } else {
            Logger.w(TAG, "Nothing to show");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, localFilter);
        adapter.setMessages(MessageProvider.getMessages(threadAuthorId));

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(GcmBroadcastReceiver.DEFAULT_NOTIFICATION_ID);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver);
        super.onPause();
    }

    @OnClick(R.id.sendButton)
    public void startSearch(Button button) {
        final String messageText = messageTextField.getText().toString();
        final SendMessageRequest request = new SendMessageRequest(threadAuthorId,
                messageText, RadarApplication.instance.getDeviceGuid());
        Utility.getApiClient().sendMessage(request, new HackedCallback<Message>() {
            @Override
            public void success(Message message, Response response) {
                Logger.d(TAG, "Message sent successfully. Message: " + message);
                MessageProvider.addMessage(threadAuthorId, message);
                adapter.setMessages(MessageProvider.getMessages(threadAuthorId));
                messageTextField.setText("");
            }

            @Override
            protected void handleError(RetrofitError retrofitError) {
                Logger.e(TAG, "Failed to send message. Message: " + messageText, retrofitError);
            }

            @Override
            protected void repeat() {
                Utility.getApiClient().sendMessage(request, this);
            }
        });
    }
}
