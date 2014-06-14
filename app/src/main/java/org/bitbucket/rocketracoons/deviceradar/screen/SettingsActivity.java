package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.model.Device;
import org.bitbucket.rocketracoons.deviceradar.model.ExtendedDeviceData;
import org.bitbucket.rocketracoons.deviceradar.network.ApiClient;
import org.bitbucket.rocketracoons.deviceradar.utility.DataCollector;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;
import org.bitbucket.rocketracoons.deviceradar.utility.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Stenopolz on 14.06.2014.
 */
public class SettingsActivity extends Activity {
    private static String TAG = SettingsActivity.class.getSimpleName();
    private static String DEVICE_CONNECTED_NAME = "DEVICE_CONNECTED_NAME";
    @InjectView(R.id.connectButton)
    Button connectButton;
    @InjectView(R.id.disconnectButton)
    Button disconnectButton;

    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        isConnected = sPref.getBoolean(DEVICE_CONNECTED_NAME, false);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupButtons();
    }

    private void setupButtons() {
        if (isConnected) {
            connectButton.setEnabled(false);
            disconnectButton.setEnabled(true);
        } else {
            connectButton.setEnabled(true);
            disconnectButton.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
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

    @OnClick(R.id.connectButton)
    public void connectDevice(Button button) {
        Logger.v(TAG, "Connecting device");
        registerDevice(DataCollector.collectCompleteDeviceInformation());
    }

    @OnClick(R.id.disconnectButton)
    public void disconnectDevice(Button button) {
        //TODO: unreg the device
        Logger.v(TAG, "Disconnecting device");
    }
    private void registerDevice(final ExtendedDeviceData deviceToRegister) {
        Logger.v(TAG, "Registering device: " + deviceToRegister);

        final ApiClient apiClient = Utility.getApiClient();
        apiClient.registerDevice(deviceToRegister, new Callback<Device>() {
            @Override
            public void success(Device device, Response response) {
                // TODO: stub
                Logger.v(TAG, "Register success for: " + device);
                Toast.makeText(SettingsActivity.this, "Device successfully connected", Toast.LENGTH_SHORT).show();
                SharedPreferences sPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sPref.edit();
                editor.putBoolean(DEVICE_CONNECTED_NAME, true);
                editor.commit();
                isConnected = true;
                setupButtons();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                // TODO: stub
                Logger.v(TAG, "Register failure for: " + deviceToRegister + " with: "
                        + retrofitError);
                Toast.makeText(SettingsActivity.this, "An error occurred while registering the device", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
