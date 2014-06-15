package org.bitbucket.rocketracoons.deviceradar.screen.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
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

        alighMessageView(holder, message);

        return rowView;
    }

    private void alighMessageView(ViewHolder holder, Message message) {
        LinearLayout.LayoutParams messageLp =
                (LinearLayout.LayoutParams) holder.messageField.getLayoutParams();
        LinearLayout.LayoutParams authorLp =
                (LinearLayout.LayoutParams) holder.authorField.getLayoutParams();
        LinearLayout.LayoutParams dataLp =
                (LinearLayout.LayoutParams) holder.dateField.getLayoutParams();
        if (null == messageLp) {
            messageLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (null == authorLp) {
            authorLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (null == dataLp) {
            dataLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (Message.Type.OUTBOUND == message.type) {
            messageLp.gravity = authorLp.gravity = dataLp.gravity = Gravity.RIGHT;
        } else {
            messageLp.gravity = authorLp.gravity = dataLp.gravity = Gravity.LEFT;
        }
        holder.messageField.setLayoutParams(messageLp);
        holder.authorField.setLayoutParams(authorLp);
        holder.dateField.setLayoutParams(dataLp);
    }
}
