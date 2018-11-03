package com.quikzon.ad.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quikzon.ad.R;
import com.quikzon.ad.activities.Items_details;
import com.quikzon.ad.model.Adimg_model;
import com.quikzon.ad.model.Product_attrubuts;
import com.quikzon.ad.utility.Utility;

import java.util.ArrayList;

public class Rv_adapter_viewall extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Adimg_model> personNames;
    Context context;
    String ad_author_id,ad_id;
    public Rv_adapter_viewall(Context context, ArrayList<Adimg_model> personNames, String ad_author_id, String ad_id) {
        this.context = context;
        this.personNames = personNames;
        this.ad_author_id = ad_author_id;
        this.ad_id = ad_id;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_imgs, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        Utility.Set_image(personNames.get(position).getFull(),((MyViewHolder)viewHolder).Ivimg);
        ((MyViewHolder)viewHolder).Ll_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Items_details.class);
                intent.putExtra("userid",ad_author_id);
                intent.putExtra("ad_id",ad_id);
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
        ImageView Ivimg;
        LinearLayout Ll_click;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            Ivimg = (ImageView) itemView.findViewById(R.id.Ivimg);
            Ll_click = (LinearLayout) itemView.findViewById(R.id.Ll_click);
        }
    }
}
