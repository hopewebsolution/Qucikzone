package com.quikzon.ad.adapters;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quikzon.ad.R;
import com.quikzon.ad.model.ChatMessage;
import com.quikzon.ad.restapi.User_details;
import com.quikzon.ad.utility.Utility;

import org.json.JSONException;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private  LayoutInflater inflater = null;
    private ArrayList<ChatMessage> chatMessageList;
    Context context;

    public ChatAdapter(Activity activity, ArrayList<ChatMessage> list) {
        chatMessageList = list;
        context = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ChatMessage message = chatMessageList.get(position);
        View vi = convertView;
        try {
            if (message.getSender().equalsIgnoreCase(Utility.single((Activity)context, User_details.id)))
                vi = inflater.inflate(R.layout.item_chat_received_layout, null);
            else
                vi = inflater.inflate(R.layout.item_chat_layout, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView tv_message = vi.findViewById(R.id.message);
        TextView tv_date = vi.findViewById(R.id.tv_date);
        ImageView imageView = vi.findViewById(R.id.profile_image);
        LinearLayout bubbleLayout = vi.findViewById(R.id.chat_bubble);

        tv_message.setText(message.getBody());
        tv_date.setText(message.getDate());

//            if (message.isMine()) {
//                tv_message.setPaddingRelative(10, 5, 30, 0);
//                tv_date.setPaddingRelative(10, 10, 30, 0);
//                bubbleLayout.setBackgroundResource(R.drawable.ic_rtl_send_message);
//            }
//            else {
//                bubbleLayout.setBackgroundResource(R.drawable.ic_rtl_received_message);
//                tv_message.setPaddingRelative(30, 5, 10, 0);
//                tv_date.setPaddingRelative(30, 10, 10, 0);
//            }
        return vi;
    }

    public void add(ChatMessage object) {
        chatMessageList.add(object);
    }
}