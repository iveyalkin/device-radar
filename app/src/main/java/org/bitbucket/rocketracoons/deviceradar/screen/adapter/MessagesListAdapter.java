package org.bitbucket.rocketracoons.deviceradar.screen.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bitbucket.rocketracoons.deviceradar.R;
import org.bitbucket.rocketracoons.deviceradar.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Igor.Veyalkin on 15.06.2014.
 */
public class MessagesListAdapter extends BaseAdapter {
    private ArrayList<Message> messages;
    private final LayoutInflater layoutInflater;

    public MessagesListAdapter(Activity context, LinkedList<Message> messagesList) {
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

        public LinearLayout container;

        public ViewHolder(LinearLayout view) {
            ButterKnife.inject(this, view);
            container = view;
        }
    }

    public void setMessages(LinkedList<Message> messages) {
        this.messages =  new ArrayList<Message>(messages);
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
            LinearLayout layout = (LinearLayout) layoutInflater
                    .inflate(R.layout.list_item_message_description, null);
            holder = new ViewHolder(layout);
            rowView = layout;
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        final Message message = getItem(position);

        holder.messageField.setText(message.message);
        holder.authorField.setText(message.authorName);
        holder.dateField.setText(new SimpleDateFormat().format(message.date));

        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) holder.container.getLayoutParams();
        if (null == layoutParams) {
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (Message.Type.OUTBOUND == message.type) {
            layoutParams.gravity = Gravity.RIGHT;
        } else {
            layoutParams.gravity = Gravity.LEFT;
        }
        holder.container.setLayoutParams(layoutParams);

        return rowView;
    }
}
