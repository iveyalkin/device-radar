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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.bitbucket.rocketracoons.deviceradar.GcmBroadcastReceiver;
import org.bitbucket.rocketracoons.deviceradar.MessageProvider;
import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.screen.adapter.MessagesRecipientsListAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;


public class MessagesListActivity extends Activity {
    @InjectView(R.id.listView)
    ListView messagesListView;

    private MessagesRecipientsListAdapter adapter;
    private BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.setRecipients(MessageProvider.getStats());

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(GcmBroadcastReceiver.DEFAULT_NOTIFICATION_ID);
        }
    };
    private IntentFilter localFilter = new IntentFilter(GcmBroadcastReceiver.EVENT_PUSH_RECEIVED);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);
        ButterKnife.inject(this);

        adapter = new MessagesRecipientsListAdapter(this, MessageProvider.getStats());
        messagesListView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.messages_list, menu);
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

    @OnItemClick(R.id.listView)
    public void onMessageSelected(AdapterView<?> parent, View view, int position, long id) {
        Intent messageActivity = new Intent(getApplicationContext(), MessagesActivity.class);
        messageActivity.putExtra(MessagesActivity.ARG_AUTHOR_ID, adapter.getItem(position).first);
        startActivity(messageActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, localFilter);
        adapter.setRecipients(MessageProvider.getStats());

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(GcmBroadcastReceiver.DEFAULT_NOTIFICATION_ID);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver);
        super.onPause();
    }
}
