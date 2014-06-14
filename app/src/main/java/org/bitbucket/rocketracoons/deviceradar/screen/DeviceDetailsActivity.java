package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.os.Bundle;
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
            device = (Device)intentBundle.getSerializable(DEVICE_EXTRA_NAME);
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
