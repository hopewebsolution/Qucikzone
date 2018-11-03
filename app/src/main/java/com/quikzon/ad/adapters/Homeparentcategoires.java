package com.quikzon.ad.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.quikzon.ad.R;
import com.quikzon.ad.activities.Viewall;
import com.quikzon.ad.helper.EqualSpacingItemDecoration;
import com.quikzon.ad.model.Products;

import java.util.ArrayList;
public class Homeparentcategoires extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Products> personNames;
    Context context;
    Sub_catgory_detail sub_catgory;
    public Homeparentcategoires(Context context, ArrayList<Products> personNames) {
        this.context = context;
        this.personNames = personNames;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ((MyViewHolder)viewHolder).sub_catg.setNestedScrollingEnabled(false);
        ((MyViewHolder)viewHolder).sub_catg.setHasFixedSize(true);
        ((MyViewHolder)viewHolder).title.setText(personNames.get(position).getText());
        ((MyViewHolder)viewHolder).more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(personNames.get(position).getAds().size()>0) {
                    Gson gson = new Gson();
                    Log.d("sub_Cat", gson.toJson(personNames.get(position).getAds()));
                    Intent intent = new Intent(context, Viewall.class);
                    intent.putExtra("sub_Cat", gson.toJson(personNames.get(position).getAds()));
                    context.startActivity(intent);
                }
            }
        });
        DividerItemDecoration verticalDecoration = new DividerItemDecoration( ((MyViewHolder)viewHolder).sub_catg.getContext(),
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(context, R.drawable.button_border_tranfranet);
        verticalDecoration.setDrawable(verticalDivider);
        ((MyViewHolder)viewHolder).sub_catg.addItemDecoration(verticalDecoration);

        // vertical divider
        DividerItemDecoration horizontalDecoration = new DividerItemDecoration( ((MyViewHolder)viewHolder).sub_catg.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDivider = ContextCompat.getDrawable(context, R.drawable.button_border_tranfranet);
        horizontalDecoration.setDrawable(horizontalDivider);
        ((MyViewHolder)viewHolder).sub_catg.addItemDecoration(horizontalDecoration);
        int spancount = 1;
        if (personNames.get(position).getAds().size()>3){
            spancount = 2;
        }
       GridLayoutManager gridLayoutManagerl = new GridLayoutManager(context, spancount);
        gridLayoutManagerl.setOrientation(LinearLayoutManager.HORIZONTAL);
        ((MyViewHolder)viewHolder).sub_catg.setLayoutManager(gridLayoutManagerl);
         sub_catgory = new Sub_catgory_detail(context,personNames.get(position).getAds());
        ((MyViewHolder)viewHolder).sub_catg.addItemDecoration(new EqualSpacingItemDecoration(10));
        ((MyViewHolder)viewHolder).sub_catg.setAdapter(sub_catgory);
    }
    @Override
    public int getItemCount() {
        return personNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView title;
        TextView more;
        RecyclerView sub_catg;
        ImageView Ivimg;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            title = (TextView) itemView.findViewById(R.id.title);
            more = (TextView) itemView.findViewById(R.id.more);
            sub_catg = (RecyclerView) itemView.findViewById(R.id.sub_catg);

        }
    }
}
