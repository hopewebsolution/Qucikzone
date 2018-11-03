package com.quikzon.ad.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.quikzon.ad.R;
import com.quikzon.ad.activities.Items_details;
import com.quikzon.ad.model.Product_attrubuts;
import com.quikzon.ad.utility.Utility;

import java.util.ArrayList;
public class Sub_catgory_detail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Product_attrubuts> personNames;
    Context context;
    public Sub_catgory_detail(Context context, ArrayList<Product_attrubuts> personNames) {
        this.context = context;
        this.personNames = personNames;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_categories, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
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
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            Tvdescription = (TextView) itemView.findViewById(R.id.Tvdescription);
            Tvaddress = (TextView) itemView.findViewById(R.id.Tvaddress);
            Tvprice = (TextView) itemView.findViewById(R.id.Tvprice);
            Ivimg = (ImageView) itemView.findViewById(R.id.Ivimg);
            items = (LinearLayout) itemView.findViewById(R.id.items);
        }
    }
}
