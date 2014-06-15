package org.bitbucket.rocketracoons.deviceradar.screen.adapter;

import android.app.Activity;
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
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Igor.Veyalkin on 15.06.2014.
 */
public class MessagesListAdapter extends BaseAdapter {
    private List<Message> messages;
    private final LayoutInflater layoutInflater;

    public MessagesListAdapter(Activity context, Set<Message> messagesList) {
        setMessages(messagesList);
        this.layoutInflater = context.getLayoutInflater();
    }

    protected static class ViewHolder {
        @InjectView(R.id.messageField)
        public TextView messageField;
        @InjectView(R.id.authorField)
        public TextView authorField;
        @InjectView(R.id.dateField)
        public TextView dateField;
        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public void setMessages(Set<Message> messages) {
        ArrayList<Message> list = new ArrayList<Message>(messages.size());
        for (final Message msg : messages) {
            list.add(msg);
        }
        this.messages = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (messages != null) {
            return messages.size();
        }
        return 0;
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
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
            rowView = layoutInflater.inflate(R.layout.list_item_message_description, null);
            holder = new ViewHolder(rowView);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        final Message message = getItem(position);

        holder.messageField.setText(message.message);
        holder.authorField.setText(message.authorName);
        holder.dateField.setText(new SimpleDateFormat().format(message.date));

        return rowView;
    }
}
