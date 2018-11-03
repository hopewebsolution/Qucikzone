package com.quikzon.ad.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.quikzon.ad.R;
import com.quikzon.ad.activities.Sub_catgory;
import com.quikzon.ad.fragements.Homefragement;
import com.quikzon.ad.model.Super_cat;
import com.quikzon.ad.utility.Utility;

import java.util.ArrayList;

public class Homecategories extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList <Super_cat> superCats;
    Context context;
    Gson gson;

    public Homecategories(Context activity, ArrayList<Super_cat> superCatsg) {
        this.context = activity;
        this.superCats = superCatsg;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_homecategories, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        ((MyViewHolder)viewHolder).name.setText(superCats.get(position).getName());
        Utility.Set_image(superCats.get(position).getImg(), ((MyViewHolder)viewHolder).image);
        ((MyViewHolder) viewHolder).Llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Gson gson = new Gson();
                    Intent intent = new Intent(context, Sub_catgory.class);
                    intent.putExtra("position",String.valueOf(position));
                    intent.putExtra("sub_catgory",gson.toJson(superCats.get(position).getSub_cat()));
                    intent.putExtra("banner", superCats.get(position).getCat_banner());
                    intent.putExtra("title", superCats.get(position).getName());
                    context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return superCats.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        ImageView image;
        LinearLayout Llmain;
        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.Tvname);
            image = (ImageView) itemView.findViewById(R.id.Ivimg);
            Llmain = (LinearLayout) itemView.findViewById(R.id.Llmain);
        }
    }
}
