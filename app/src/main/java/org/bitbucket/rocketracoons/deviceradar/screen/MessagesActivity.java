package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.bitbucket.rocketracoons.deviceradar.MessageProvider;
import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.RadarApplication;
import org.bitbucket.rocketracoons.deviceradar.model.Message;
import org.bitbucket.rocketracoons.deviceradar.network.model.SendMessageRequest;
import org.bitbucket.rocketracoons.deviceradar.screen.adapter.MessagesListAdapter;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;
import org.bitbucket.rocketracoons.deviceradar.utility.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
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
        adapter.setMessages(MessageProvider.getMessages(threadAuthorId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.sendButton)
    public void startSearch(Button button) {
        final String messageText = messageTextField.getText().toString();
        final SendMessageRequest request = new SendMessageRequest(threadAuthorId,
                messageText, RadarApplication.instance.getDeviceGuid());
        Utility.getApiClient().sendMessage(request,
                new Callback<Message>() {
            @Override
            public void success(Message message, Response response) {
                Logger.d(TAG, "Message sent successfully. Message: " + message);
                MessageProvider.addMessage(threadAuthorId, message);
                adapter.setMessages(MessageProvider.getMessages(threadAuthorId));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Logger.e(TAG, "Failed to send message. Message: " + messageText, retrofitError);
            }
        });
    }
}
