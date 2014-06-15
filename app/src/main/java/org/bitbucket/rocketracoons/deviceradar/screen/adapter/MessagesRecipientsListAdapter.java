package org.bitbucket.rocketracoons.deviceradar.screen.adapter;

import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Igor.Veyalkin on 15.06.2014.
 */
public class MessagesRecipientsListAdapter extends BaseAdapter {
    private List<Pair<String, Integer>> recipients;
    private final LayoutInflater layoutInflater;

    public MessagesRecipientsListAdapter(Activity context, Map<String, Integer> recipientsList) {
        setRecipients(recipientsList);
        this.layoutInflater = context.getLayoutInflater();
    }

    protected static class ViewHolder {
        @InjectView(R.id.counterField)
        public TextView counterField;
        @InjectView(R.id.recipientField)
        public TextView recipientField;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public void setRecipients(Map<String, Integer> recipientsList) {
        ArrayList<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>(recipientsList.size());
        for (final Map.Entry<String, Integer> entry : recipientsList.entrySet()) {
            list.add(new Pair<String, Integer>(entry.getKey(), entry.getValue()));
        }
        this.recipients = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (recipients != null) {
            return recipients.size();
        }
        return 0;
    }

    @Override
    public Pair<String, Integer> getItem(int position) {
        return recipients.get(position);
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
            rowView = layoutInflater.inflate(R.layout.list_item_recipient_description, null);
            holder = new ViewHolder(rowView);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        final Pair<String, Integer> recipient = getItem(position);

        holder.recipientField.setText(recipient.first);
        holder.counterField.setText(recipient.second);

        return rowView;
    }
}
