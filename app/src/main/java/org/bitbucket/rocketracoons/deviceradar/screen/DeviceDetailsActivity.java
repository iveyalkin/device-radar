package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.model.Device;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeviceDetailsActivity extends Activity {
    public static String DEVICE_EXTRA_NAME = "DEVICE_EXTRA_NAME";
    @InjectView(R.id.titleTextView)
    TextView titleTextView;
    @InjectView(R.id.descriptionTextView)
    TextView descriptionTextView;
    @InjectView(R.id.sendMessageButton)
    Button sendMessageButton;

    Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        ButterKnife.inject(this);

        Bundle intentBundle = getIntent().getExtras();
        if (intentBundle != null) {
            device = (Device) intentBundle.getSerializable(DEVICE_EXTRA_NAME);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (device != null) {
            titleTextView.setText(device.name);

            StringBuilder stringBuilder = new StringBuilder();

            if (!TextUtils.isEmpty(device.osVersion)) {
                stringBuilder.append("OS Version: ");
                stringBuilder.append(device.osVersion);
                stringBuilder.append('\n');
            }

            if (!TextUtils.isEmpty(device.screen)) {
                stringBuilder.append("Screen Resolution: ");
                stringBuilder.append(device.screen);
                stringBuilder.append('\n');
            }

            stringBuilder.append("3G Module: ");
            if (device.gsmModule) {
                stringBuilder.append("available");
            } else {
                stringBuilder.append("n/a");
            }
            stringBuilder.append('\n');

            stringBuilder.append("RAM Amount: ");
            stringBuilder.append(device.ramAmount);
            stringBuilder.append(" Mb");
            stringBuilder.append('\n');

            stringBuilder.append("Internal Memory: ");
            stringBuilder.append(device.memoryAmount);
            stringBuilder.append(" Mb");
            stringBuilder.append('\n');
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.device_details, menu);
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

    @OnClick(R.id.sendMessageButton)
    public void sendMessage(Button button) {

    }
}
