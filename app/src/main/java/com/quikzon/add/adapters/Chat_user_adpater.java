package com.quikzon.add.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quikzon.add.R;
import com.quikzon.add.activities.Messagner;
import com.quikzon.add.model.Chat_list;

import java.util.ArrayList;

public class Chat_user_adpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Chat_list> chat_user;
    Context context;
    public Chat_user_adpater(Context context, ArrayList<Chat_list> chat_user) {
        this.context = context;
        this.chat_user = chat_user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_chat_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ((MyViewHolder)viewHolder).name.setText(chat_user.get(position).getAuthor_name());
        ((MyViewHolder)viewHolder).ad_title.setText(chat_user.get(position).getAd_title());
        ((MyViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Messagner.class);
                intent.putExtra("author_id",chat_user.get(position).getAuthor_id());
                intent.putExtra("ad_id",chat_user.get(position).getAd_id());
                intent.putExtra("author_name",chat_user.get(position).getAuthor_name());
                intent.putExtra("ad_title",chat_user.get(position).getAd_title());
                intent.putExtra("room_id",chat_user.get(position).getRoom_id());
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return chat_user.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name,ad_title;
        ImageView user_profile;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            ad_title = (TextView) itemView.findViewById(R.id.ad_title);

        }
    }
}
