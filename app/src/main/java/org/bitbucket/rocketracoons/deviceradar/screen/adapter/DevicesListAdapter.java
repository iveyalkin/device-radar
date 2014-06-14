package org.bitbucket.rocketracoons.deviceradar.screen.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.model.Device;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Stenopolz on 14.06.2014.
 */
public class DevicesListAdapter extends BaseAdapter {
        private List<Device> devices;
        private final LayoutInflater layoutInflater;

        public DevicesListAdapter(Activity context, List<Device> DeviceList) {
            setDevices(DeviceList);
            this.layoutInflater = context.getLayoutInflater();
        }

        protected static class ViewHolder {
            @InjectView(R.id.titleTextView)
            public TextView titleView;
            @InjectView(R.id.subtitleTextView)
            public TextView subtitleView;
            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }

        public void setDevices(List<Device> devices) {
            this.devices = devices;
            notifyDataSetChanged();
        }

        public List<Device> getDevices() {
            return devices;
        }

        @Override
        public int getCount() {
            if (devices != null) {
                return devices.size();
            }
            return 0;
        }

        @Override
        public Device getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View rowView = convertView;
            if (rowView == null) {
                rowView = layoutInflater.inflate(R.layout.list_item_device_description, null, true);
                holder = new ViewHolder(rowView);
                rowView.setTag(holder);
            } else {
                holder = (ViewHolder) rowView.getTag();
            }

            final Device device = getItem(position);

            holder.titleView.setText(device.name);
            holder.subtitleView.setText(device.osVersion);

            return rowView;
        }
}
