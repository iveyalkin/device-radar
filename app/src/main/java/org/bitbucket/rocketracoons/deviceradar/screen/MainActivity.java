package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.RadarApplication;
import org.bitbucket.rocketracoons.deviceradar.model.Device;
import org.bitbucket.rocketracoons.deviceradar.network.ApiClient;
import org.bitbucket.rocketracoons.deviceradar.screen.adapter.DevicesListAdapter;
import org.bitbucket.rocketracoons.deviceradar.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


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
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
//        devicesList = new ArrayList<Device>();
//        Device device = new Device();
//        device.name = "Android-004";
//        device.osVersion = "Android 4.2.1";
//        devicesList.add(device);
//
//        device = new Device();
//        device.name = "Android-005";
//        device.osVersion = "Android 4.0.4";
//        devicesList.add(device);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProgressBarVisibility(true);
        Utility.getApiClient().getDevicesList(new Callback<ArrayList<Device>>() {
            @Override
            public void success(ArrayList<Device> devices, Response response) {
                devicesList = devices;
                devicesListView.setAdapter(new DevicesListAdapter(MainActivity.this, devicesList));
                setProgressBarIndeterminateVisibility(false);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(MainActivity.this, "Sorry, service is unavailable", Toast.LENGTH_SHORT).show();
                devicesListView.setAdapter(new DevicesListAdapter(MainActivity.this, null));
                setProgressBarIndeterminateVisibility(false);
            }
        });

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
