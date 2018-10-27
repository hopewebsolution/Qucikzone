package com.quikzon.add.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quikzon.add.R;
import com.quikzon.add.activities.Items_details;
import com.quikzon.add.helper.EqualSpacingItemDecoration;
import com.quikzon.add.model.Product_attrubuts;
import com.quikzon.add.utility.Utility;

import java.util.ArrayList;

public class VIew_all_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Product_attrubuts> personNames;
    Context context;
    Rv_adapter_viewall slider;
    public VIew_all_adapter(Context context, ArrayList<Product_attrubuts> personNames) {
        this.context = context;
        this.personNames = personNames;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_catgory, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        ((MyViewHolder)viewHolder).Tvdescription.setText(personNames.get(position).getAd_title());
        if(personNames.get(position).getIs_feature()) {
            ((MyViewHolder)viewHolder).Tvtxtfiled.setVisibility(View.VISIBLE);
        }

//      ((MyViewHolder)viewHolder).Tvcondition.setText(personNames.get(position).getAd_cats_name());
        ((MyViewHolder)viewHolder).Tvaddress.setText(personNames.get(position).getAd_location().get(0).getAddress());
        ((MyViewHolder)viewHolder).Tvprice.setText(personNames.get(position).getAd_price().get(0).getPrice());
        ((MyViewHolder)viewHolder).Tvcondition.setText(personNames.get(position).getAd_condition());
        ((MyViewHolder)viewHolder).img_scroll.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
         slider = new Rv_adapter_viewall(context, personNames.get(position).getAd_images());
        ((MyViewHolder)viewHolder).img_scroll.setAdapter(slider);
        ((MyViewHolder)viewHolder).img_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Items_details.class);
                intent.putExtra("userid",personNames.get(position).getAd_author_id());
                intent.putExtra("ad_id",personNames.get(position).getAd_id());
                context.startActivity(intent);
            }
        });
        ((MyViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
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
        TextView Tvdescription,Tvaddress,Tvprice,Tvcondition,Tvtxtfiled;
        RecyclerView img_scroll;
        Button Btbut_now;
        LinearLayout mainLl;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            Tvdescription = (TextView) itemView.findViewById(R.id.Tvdescription);
            Tvcondition = (TextView) itemView.findViewById(R.id.Tvcondition);
            Tvaddress = (TextView) itemView.findViewById(R.id.Tvaddress);
            Tvprice = (TextView) itemView.findViewById(R.id.Tvprice);
            Tvtxtfiled = (TextView) itemView.findViewById(R.id.Tvtxtfiled);
            Btbut_now = (Button) itemView.findViewById(R.id.Btbut_now);
            mainLl = (LinearLayout) itemView.findViewById(R.id.mainLl);
            img_scroll = (RecyclerView) itemView.findViewById(R.id.img_scroll);
        }
    }
}
