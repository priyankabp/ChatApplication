package com.example.priyankaphapale.chatapp.model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.example.priyankaphapale.chatapp.R;

/**
 * Created by m_alrajab on 4/3/17.
 */

public class ChatMsgAdapter extends ArrayAdapter<ChatMessageItem> {
    public ChatMsgAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ChatMessageItem> objects) {
        super(context, resource, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView= ((Activity)getContext()).getLayoutInflater().inflate(R.layout.chat_msg_item
                    ,parent,false);

        TextView msgBd=(TextView)convertView.findViewById(R.id.msg_body);
        TextView msgSndr=(TextView)convertView.findViewById(R.id.msg_sender_name);
        TextView msgTime=(TextView)convertView.findViewById(R.id.msg_time);
        ImageView msgPic=(ImageView)convertView.findViewById(R.id.msg_pic);

        ChatMessageItem messageItem=getItem(position);
        msgBd.setText(messageItem.getMsgBody());
        msgSndr.setText(messageItem.getSenderName());
        msgTime.setText(messageItem.getTimeStamp());

        if(messageItem.getPhotoUrl()!=null){
            Picasso.with(msgPic.getContext())
                    .load(messageItem.getPhotoUrl())
                    .resize(30,30)
                    .centerCrop()
                    .into(msgPic);
        }

        return convertView;
    }
}
