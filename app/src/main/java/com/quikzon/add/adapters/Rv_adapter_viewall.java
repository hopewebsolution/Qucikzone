package com.quikzon.add.adapters;

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

import com.quikzon.add.R;
import com.quikzon.add.activities.Items_details;
import com.quikzon.add.model.Adimg_model;
import com.quikzon.add.model.Product_attrubuts;
import com.quikzon.add.utility.Utility;

import java.util.ArrayList;

public class Rv_adapter_viewall extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Adimg_model> personNames;
    Context context;
    public Rv_adapter_viewall(Context context, ArrayList<Adimg_model> personNames) {
        this.context = context;
        this.personNames = personNames;
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
    }

    @Override
    public int getItemCount() {
        return personNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        ImageView Ivimg;
        LinearLayout items;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            Ivimg = (ImageView) itemView.findViewById(R.id.Ivimg);
            items = (LinearLayout) itemView.findViewById(R.id.items);
        }
    }
}
