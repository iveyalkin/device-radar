package org.bitbucket.rocketracoons.deviceradar.screen.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Stenopolz on 14.06.2014.
 */
public class DevicesListAdapter extends BaseAdapter {
        private final Activity context;
        private List<Device> devices;
        private final LayoutInflater layoutInflater;

        public BookingsListAdapter(Activity context, List<Device> BookingList) {
            this.context = context;
            setBookings(BookingList);
            this.layoutInflater = context.getLayoutInflater();
        }

        protected static class ViewHolder {
            public TextView dateFrom;
            public TextView dateTo;
        }

        public void setBookings(List<Booking> listingBlocks) {
            Bookings = listingBlocks;
            notifyDataSetChanged();
        }

        public List<Booking> getBookings() {
            return Bookings;
        }

        @Override
        public int getCount() {
            if (Bookings != null) {
                return Bookings.size();
            }
            return -1;
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
                rowView = layoutInflater.inflate(R.layout.list_item_bookings, null, true);
                holder = new ViewHolder();
                holder.dateFrom = (TextView) rowView.findViewById(R.id.dateFrom);
                holder.dateTo = (TextView) rowView.findViewById(R.id.dateTo);
                rowView.setTag(holder);
            } else {
                holder = (ViewHolder) rowView.getTag();
            }

            final Booking booking = getItem(position);

            holder.dateFrom.setText(android.text.format.DateFormat.format("hh.mm", booking.fromDate));
            holder.dateTo.setText(android.text.format.DateFormat.format("hh.mm", booking.toDate));

            return rowView;
        }
}
