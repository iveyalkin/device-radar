package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;

import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.RadarApplication;
import org.bitbucket.rocketracoons.deviceradar.model.Device;
import org.bitbucket.rocketracoons.deviceradar.network.ApiClient;
import org.bitbucket.rocketracoons.deviceradar.network.HackedCallback;
import org.bitbucket.rocketracoons.deviceradar.network.model.LoginRequest;
import org.bitbucket.rocketracoons.deviceradar.network.model.LoginResponse;
import org.bitbucket.rocketracoons.deviceradar.screen.adapter.DevicesListAdapter;
import org.bitbucket.rocketracoons.deviceradar.utility.GcmSupportedType;
import org.bitbucket.rocketracoons.deviceradar.utility.Logger;
import org.bitbucket.rocketracoons.deviceradar.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 0xff00;

    @InjectView(R.id.searchField)
    EditText searchField;
    @InjectView(R.id.listView)
    ListView devicesListView;

    List<Device> devicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    DevicesListAdapter adapter = (DevicesListAdapter) devicesListView.getAdapter();
                    Filter filter = adapter.getFilter();
                    String constraint = s.toString();
                    filter.filter(constraint);
                } catch (NullPointerException e) {
                    Logger.e(TAG, "Almost unhandled error", e);
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        tryGooglePlayServices();

        String deviceName = RadarApplication.instance.getDeviceName();
        if (!TextUtils.isEmpty(deviceName)) {
            getActionBar().setTitle(deviceName);
        } else {
            getActionBar().setTitle(R.string.app_name);
        }
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
        Utility.getApiClient().getDevicesList(new HackedCallback<ArrayList<Device>>() {
            @Override
            public void success(ArrayList<Device> devices, Response response) {
                devicesList = devices;
                devicesListView.setAdapter(new DevicesListAdapter(MainActivity.this, devicesList));
                setProgressBarIndeterminateVisibility(false);
                searchField.setText("");
            }

            @Override
            protected void handleError(RetrofitError retrofitError) {
                Toast.makeText(MainActivity.this, "Sorry, service is unavailable", Toast.LENGTH_SHORT).show();
                devicesListView.setAdapter(new DevicesListAdapter(MainActivity.this, null));
                setProgressBarIndeterminateVisibility(false);
            }

            @Override
            protected void repeat() {
                Utility.getApiClient().getDevicesList(this);
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
            if (TextUtils.isEmpty(RadarApplication.instance.getUserToken())) {
                showLoginPrompt();
            } else {
                tryOpenSettings(RadarApplication.instance.getUserRole());
            }
            return true;
        } else if(id == R.id.action_messages) {
            Intent intent = new Intent(this, MessagesListActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_refresh) {
            requestDeviceList();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void processLogin(final String login, final String password) {
        final ApiClient apiClient = Utility.getApiClient();
        setProgressBarIndeterminateVisibility(true);
        apiClient.auth(new LoginRequest(login, password), new HackedCallback<LoginResponse>() {
                @Override
                public void success(LoginResponse loginResponse, Response response) {
                    Logger.v(TAG, "Login success for");
                    RadarApplication.instance.setUserToken(loginResponse.sessionId);
                    RadarApplication.instance.setUserRole(loginResponse.role);
                    tryOpenSettings(loginResponse.role);
                }

                @Override
                protected void handleError(RetrofitError retrofitError) {
                    Logger.v(TAG, "Login failure with: "
                            + retrofitError);
                    Toast.makeText(MainActivity.this, "Can't log in", Toast.LENGTH_SHORT).show();
                    setProgressBarIndeterminateVisibility(false);
                }

                @Override
                protected void repeat() {
                    apiClient.auth(new LoginRequest(login, password), this);
                }
        });
    }

    private void tryOpenSettings(String role) {
        if (role.equalsIgnoreCase("admin")) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "You don't have enough rights to manage the device", Toast.LENGTH_SHORT).show();
        }
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
