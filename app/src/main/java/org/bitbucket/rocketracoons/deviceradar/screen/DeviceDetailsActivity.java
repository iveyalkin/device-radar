package org.bitbucket.rocketracoons.deviceradar.screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

    private Device device;

    @InjectView(R.id.mapview)
    MapView m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        ButterKnife.inject(this);
        m.onCreate(savedInstanceState);
        
        Bundle intentBundle = getIntent().getExtras();
        if (intentBundle != null) {
            device = (Device) intentBundle.getSerializable(DEVICE_EXTRA_NAME);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        m.onResume();
        if (device != null) {
            titleTextView.setText(device.name);

            StringBuilder stringBuilder = new StringBuilder();

            if (!TextUtils.isEmpty(device.osVersion)) {
                stringBuilder.append("OS Version: ");
                stringBuilder.append(device.osVersion);
                stringBuilder.append('\n');
            }

            if (!TextUtils.isEmpty(device.accessPointName)) {
                stringBuilder.append("Last known location: ");
                stringBuilder.append(device.accessPointName);
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
            stringBuilder.append(" Gb");
            stringBuilder.append('\n');

            descriptionTextView.setText(stringBuilder.toString());

            if (device.latitude != 0.0 && device.longitude != 0.0) {
                configureMap(m.getMap(), device.latitude, device.longitude);
//                m.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
            } else {
                m.setVisibility(View.GONE);
            }
        }
    }

    private void
    configureMap(GoogleMap map, double lat, double lon)
    {
        if (map == null)
            return; // Google Maps not available

            MapsInitializer.initialize(this);

//        map.setMyLocationEnabled(true);

        LatLng latLng = new LatLng(lat, lon);
        map.addMarker(new MarkerOptions().position(latLng));
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(latLng, (float) 16.0);
        map.animateCamera(camera);
    }

    @Override
    public void onPause() {
        super.onPause();
        m.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        m.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        m.onLowMemory();
    }

    @OnClick(R.id.sendMessageButton)
    public void sendMessage(Button button) {
        final Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
        intent.putExtra(MessagesActivity.ARG_AUTHOR_ID, device.guid);
        startActivity(intent);
    }
}
