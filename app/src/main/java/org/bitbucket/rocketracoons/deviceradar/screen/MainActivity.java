package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.RadarApplication;
import org.bitbucket.rocketracoons.deviceradar.model.Device;
import org.bitbucket.rocketracoons.deviceradar.network.ApiClient;
import org.bitbucket.rocketracoons.deviceradar.network.model.LoginRequest;
import org.bitbucket.rocketracoons.deviceradar.network.model.LoginResponse;
import org.bitbucket.rocketracoons.deviceradar.screen.adapter.DevicesListAdapter;
import org.bitbucket.rocketracoons.deviceradar.utility.DataCollector;
import org.bitbucket.rocketracoons.deviceradar.utility.GcmSupportedType;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;
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
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 0xff00;

    @InjectView(R.id.searchField)
    EditText searchField;
    @InjectView(R.id.startSearch)
    Button startSearch;
    @InjectView(R.id.listView)
    ListView devicesListView;

    List<Device> devicesList;
    private PullToRefreshLayout mPullToRefreshLayout;

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

        // Now find the PullToRefreshLayout to setup
       /* mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);

        // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(this)
                // Mark All Children as pullable
                .allChildrenArePullable()
                        // Set a OnRefreshListener
                .listener(...)
        // Finally commit the setup to our PullToRefreshLayout
        .setup(mPullToRefreshLayout);*/
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        tryGooglePlayServices();
    }

    private void tryGooglePlayServices() {
        GcmSupportedType gcmSupportedType = RadarApplication.instance.checkGooglePlayServices();
        switch (gcmSupportedType) {
            case SUPPORTED:
                requestDeviceList();
                break;
            case SUPPORTED_BUT_USER: {
                final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
                break;
            }
            case UNSUPPORTED:
                finish();
                break;
        }
    }

    private void requestDeviceList() {
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
            showLoginPrompt();
            return true;
        } else if(id == R.id.action_messages) {
            Intent intent = new Intent(this, MessagesListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.startSearch)
    public void startSearch(Button button) {
        ((DevicesListAdapter)devicesListView.getAdapter()).getFilter().filter(searchField.getText().toString());
    }

    @OnItemClick(R.id.listView)
    public void onDeviceSelected(AdapterView<?> parent, View view, int position, long id) {
        Device device = devicesList.get(position);
        Intent intent = new Intent(this, DeviceDetailsActivity.class);
        intent.putExtra(DeviceDetailsActivity.DEVICE_EXTRA_NAME, device);
        startActivity(intent);
    }

    private void showLoginPrompt() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
//        alertDialog.setTitle("Password");

        // Setting Dialog Message
        alertDialog.setMessage("Login, please");

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_password_dialog, null);
        final EditText login = (EditText) view.findViewById(R.id.loginField);
        final EditText password = (EditText) view.findViewById(R.id.passwordField);
        alertDialog.setView(view);

        // Setting Positive Button
        alertDialog.setPositiveButton("Login",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        processLogin(login.getText().toString(), password.getText().toString());
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

    private void processLogin(String login, String password) {
        final ApiClient apiClient = Utility.getApiClient();
        setProgressBarIndeterminateVisibility(true);
        apiClient.auth(new LoginRequest(login, password), new Callback<LoginResponse>() {
                            @Override
                public void success(LoginResponse loginResponse, Response response) {
                    Logger.v(TAG, "Login success for");
                    if (loginResponse.role.equalsIgnoreCase("admin")) {
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "You don't have enough rights to manage the device", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Logger.v(TAG, "Login failure with: "
                            + retrofitError);
                    Toast.makeText(MainActivity.this, "Can't log in", Toast.LENGTH_SHORT).show();
                    setProgressBarIndeterminateVisibility(false);
                }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PLAY_SERVICES_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                case Activity.RESULT_OK:
                    RadarApplication.instance.tryRegisterDevice();
                    requestDeviceList();
                    break;
                default:
                    Toast.makeText(this, "Oooops, User doesn't whant a GP services...",
                            Toast.LENGTH_LONG).show();
                    break;
                }
        }
    }
}
