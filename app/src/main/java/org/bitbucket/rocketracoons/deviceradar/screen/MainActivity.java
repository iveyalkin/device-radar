package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.model.Device;
import org.bitbucket.rocketracoons.deviceradar.screen.adapter.DevicesListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class MainActivity extends Activity {
    @InjectView(R.id.searchField)
    EditText searchField;
    @InjectView(R.id.startSearch)
    Button startSearch;
    @InjectView(R.id.listView)
    ListView devicesListView;

    List<Device> devicesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        devicesList = new ArrayList<Device>();
        Device device = new Device();
        device.name = "Android-004";
        device.osVersion = "Android 4.2.1";
        devicesList.add(device);
        device = new Device();
        device.name = "Android-005";
        device.osVersion = "Android 4.0.4";
        devicesList.add(device);
    }

    @Override
    protected void onResume() {
        super.onResume();

        devicesListView.setAdapter(new DevicesListAdapter(this, devicesList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @OnClick(R.id.startSearch)
    public void startSearch(Button button) {

    }

    @OnItemClick(R.id.listView)
    public void onDeviceSelected(AdapterView<?> parent, View view, int position, long id) {
        Device device = devicesList.get(position);
        Intent intent = new Intent(this, DeviceDetailsActivity.class);
        intent.putExtra(DeviceDetailsActivity.DEVICE_EXTRA_NAME, device);
        startActivity(intent);

    }
}
