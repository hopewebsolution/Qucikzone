package com.quikzon.add.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.quikzon.add.R;
import com.quikzon.add.activities.Viewall;
import com.quikzon.add.helper.EqualSpacingItemDecoration;
import com.quikzon.add.model.Products;

import java.util.ArrayList;
public class Homeparentcategoires extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Products> personNames;
    Context context;
    Sub_catgory sub_catgory;
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
        ((MyViewHolder)viewHolder).sub_catg.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
         sub_catgory = new Sub_catgory(context,personNames.get(position).getAds());

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
