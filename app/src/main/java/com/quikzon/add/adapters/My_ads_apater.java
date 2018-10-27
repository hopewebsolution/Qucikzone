package com.quikzon.add.adapters;

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

import com.google.gson.Gson;
import com.quikzon.add.R;
import com.quikzon.add.activities.Account_ad_details;
import com.quikzon.add.activities.Items_details;
import com.quikzon.add.activities.Post_add;
import com.quikzon.add.fragements.Addpost_fragement;
import com.quikzon.add.model.Product_attrubuts;
import com.quikzon.add.restapi.User_details;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;

import java.util.ArrayList;

public class My_ads_apater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Product_attrubuts> personNames;
    Context context;
    boolean is_my;
    Account_ad_details account_ad_details;
    public My_ads_apater(Context context, ArrayList<Product_attrubuts> personNames,boolean is_my,Account_ad_details account_ad_details) {
        this.context = context;
        this.personNames = personNames;
        this.is_my=is_my;
        this.account_ad_details=account_ad_details;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_ads, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final Gson gson=new Gson();
        ((MyViewHolder)viewHolder).Tvdescription.setText(personNames.get(position).getAd_title());
        ((MyViewHolder)viewHolder).Tvaddress.setText(personNames.get(position).getAd_location().get(0).getAddress());
        ((MyViewHolder)viewHolder).Tvprice.setText(personNames.get(position).getAd_price().get(0).getPrice());
         Utility.Set_image(personNames.get(position).getAd_images().get(0).getThumb(),((MyViewHolder)viewHolder).Ivimg);
        ((MyViewHolder)viewHolder).items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Items_details.class);
                intent.putExtra("userid",personNames.get(position).getAd_author_id());
                intent.putExtra("ad_id",personNames.get(position).getAd_id());
                context.startActivity(intent);
            }
        });

        ((MyViewHolder)viewHolder).Btbut_edit.setVisibility(View.GONE);
        ((MyViewHolder)viewHolder).Btbut_delete.setVisibility(View.GONE);
        ((MyViewHolder)viewHolder).Btbut_now.setVisibility(View.VISIBLE);
        if(is_my)
        {
            ((MyViewHolder)viewHolder).Btbut_edit.setVisibility(View.VISIBLE);
            ((MyViewHolder)viewHolder).Btbut_delete.setVisibility(View.VISIBLE);
            ((MyViewHolder)viewHolder).Btbut_now.setVisibility(View.GONE);
        }

        ((MyViewHolder)viewHolder).Btbut_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addpost_fragement.parent_cat_name =personNames.get(position).getAd_cats().get(1).getName() ;
                Addpost_fragement.sub_cat_name =personNames.get(position).getAd_cats().get(0).getName();
                Addpost_fragement.sub_cat_id=personNames.get(position).getAd_cats().get(0).getId();
                Intent intent = new Intent(context, Post_add.class);
                intent.putExtra("ad_id",personNames.get(position).getAd_id());
                intent.putExtra("title",personNames.get(position).getAd_title());
                intent.putExtra("desc",personNames.get(position).getAd_desc());
                intent.putExtra("adress",personNames.get(position).getAd_location().get(0).getAddress());
                intent.putExtra("name",personNames.get(position).getPoster_name());
                intent.putExtra("mobile",personNames.get(position).getPhone());
                intent.putExtra("imgs",gson.toJson(personNames.get(position).getAd_images()));
                context.startActivity(intent);
            }
        });

        ((MyViewHolder)viewHolder).Btbut_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    account_ad_details.delete_ad(personNames.get(position).getAd_id(),personNames.get(position).getAd_author_id(),position);

            }
        });
    }
    @Override
    public int getItemCount() {
        return personNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView Tvdescription,Tvaddress,Tvprice;
        ImageView Ivimg;
        LinearLayout items;
        Button Btbut_edit,Btbut_delete,Btbut_now;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            Tvdescription = (TextView) itemView.findViewById(R.id.Tvdescription);
            Tvaddress = (TextView) itemView.findViewById(R.id.Tvaddress);
            Tvprice = (TextView) itemView.findViewById(R.id.Tvprice);
            Ivimg = (ImageView) itemView.findViewById(R.id.Ivimg);
            items = (LinearLayout) itemView.findViewById(R.id.Llitems);
            Btbut_edit = (Button) itemView.findViewById(R.id.Btbut_edit);
            Btbut_delete = (Button) itemView.findViewById(R.id.Btbut_delete);
            Btbut_now = (Button) itemView.findViewById(R.id.Btbut_now);
        }
    }
}
