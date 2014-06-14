package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.RadarApplication;
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

    @InjectView(R.id.connectButton)
    Button connectButton;
    @InjectView(R.id.disconnectButton)
    Button disconnectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setupButtons();
    }

    private void setupButtons() {
        if (RadarApplication.instance.isDeviceRegistered()) {
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
        showNamePrompt();
    }

    @OnClick(R.id.disconnectButton)
    public void disconnectDevice(Button button) {
        Logger.v(TAG, "Disconnecting device");
        unregisterDevice();
    }

    private void registerDevice(final ExtendedDeviceData deviceToRegister) {
        Logger.v(TAG, "Registering device: " + deviceToRegister);

        final ApiClient apiClient = Utility.getApiClient();
        setProgressBarIndeterminateVisibility(true);
        apiClient.registerDevice(deviceToRegister, new Callback<Device>() {
            @Override
            public void success(Device device, Response response) {
                Logger.v(TAG, "Register success for: " + device);
                Toast.makeText(SettingsActivity.this, "Device successfully registered", Toast.LENGTH_SHORT).show();
                RadarApplication.instance.setDeviceRegistered(true);
                setupButtons();
                setProgressBarIndeterminateVisibility(false);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Logger.v(TAG, "Register failure for: " + deviceToRegister + " with: "
                        + retrofitError);
                Toast.makeText(SettingsActivity.this, "An error occurred while registering the device", Toast.LENGTH_SHORT).show();
                setProgressBarIndeterminateVisibility(false);
            }
        });
    }

    private void unregisterDevice() {
        Logger.v(TAG, "Unregistering device");

        final ApiClient apiClient = Utility.getApiClient();
        setProgressBarIndeterminateVisibility(true);
        apiClient.unregisterDevice(DataCollector.collectDeviceGUID(), new Callback<Device>() {
            @Override
            public void success(Device device, Response response) {
                Logger.v(TAG, "Unregister success");
                Toast.makeText(SettingsActivity.this, "Device successfully unregistered", Toast.LENGTH_SHORT).show();
                RadarApplication.instance.setDeviceRegistered(false);
                setupButtons();
                setProgressBarIndeterminateVisibility(false);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Logger.v(TAG, "Register failure with: "
                        + retrofitError);
                Toast.makeText(SettingsActivity.this, "An error occurred while unregistering the device", Toast.LENGTH_SHORT).show();
                setProgressBarIndeterminateVisibility(false);
            }
        });
    }

    private void showNamePrompt() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Device Name");

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_device_name_dialog, null);
        final EditText name = (EditText) view.findViewById(R.id.deviceNameField);
        alertDialog.setView(view);

        // Setting Positive Button
        alertDialog.setPositiveButton("Register",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String deviceName = name.getText().toString();
                        if (!TextUtils.isEmpty(deviceName)) {
                            RadarApplication.instance.setDeviceName(deviceName);
                            registerDevice(DataCollector.collectCompleteDeviceInformation());
                        } else {
                            Toast.makeText(SettingsActivity.this, "Device name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        // Setting Negative Button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.dismiss();
                    }
                }
        );
        // Showing Alert Message
        alertDialog.show();
    }

}
