package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.bitbucket.rocketracoons.deviceradar.MessageProgider;
import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.model.Message;
import org.bitbucket.rocketracoons.deviceradar.screen.adapter.MessagesListAdapter;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;
import org.bitbucket.rocketracoons.deviceradar.utility.Utility;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
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
            adapter = new MessagesListAdapter(this, MessageProgider.getMessages((threadAuthorId));
        } else {
            Logger.w(TAG, "Nothing to show");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setMessages(MessageProgider.getMessages(threadAuthorId));
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
        Utility.getApiClient().sendMessage(threadAuthorId, messageText,
                new Callback<Message>() {
            @Override
            public void success(Message message, Response response) {
                Logger.d(TAG, "Message sent successfully. Message: " + message);
                MessageProgider.addMessage(threadAuthorId, message);
                adapter.setMessages(MessageProgider.getMessages(threadAuthorId));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Logger.e(TAG, "Failed to send message. Message: " + messageText, retrofitError);
            }
        });
    }
}
