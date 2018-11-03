package com.quikzon.ad.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quikzon.ad.R;
import com.quikzon.ad.activities.Account_ad_details;
import com.quikzon.ad.activities.Edit_profile_data;
import com.quikzon.ad.model.Account_details;
import com.quikzon.ad.model.Product_attrubuts;
import com.quikzon.ad.restapi.User_details;
import com.quikzon.ad.utility.Utility;

import org.json.JSONException;

import java.util.ArrayList;

public class Account_list_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Account_details> personNames;
    Context context;
    public Account_list_adapter(Context context, ArrayList<Account_details> personNames) {
        this.context = context;
        this.personNames = personNames;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ((MyViewHolder)viewHolder).Tvtitle.setText(personNames.get(position).getTitle());
//        ((MyViewHolder)viewHolder).Tvcondition.setText(personNames.get(position).getAd_cats_name());
        ((MyViewHolder)viewHolder).Tvdesc.setText(personNames.get(position).getDesc());

        ((MyViewHolder)viewHolder).Llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==0){
                    Intent intent=new Intent(context, Account_ad_details.class);
                    try {
                        intent.putExtra("userid",Utility.single((Activity)context, User_details.id));
                        intent.putExtra("author_name",Utility.single((Activity)context, User_details.display_name));
                        intent.putExtra("author_type",Utility.single((Activity)context, User_details._sb_user_type));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(position==1)
                {
                    context.startActivity(new Intent(context, Edit_profile_data.class));
                }
                else if(position==2)
                {
                    context.startActivity(new Intent(context, Edit_profile_data.class));
                }
                else if(position==3)
                {
                    Utility.Logout((Activity)context);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return personNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView Tvtitle,Tvdesc;
        ImageView Ivimg;
        LinearLayout Llmain;
        Button Btbut_now;
        LinearLayout items;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            Tvtitle = (TextView) itemView.findViewById(R.id.Tvtitle);
            Tvdesc = (TextView) itemView.findViewById(R.id.Tvdesc);
            Llmain = (LinearLayout)itemView.findViewById(R.id.Llmain);
        }
    }
}
