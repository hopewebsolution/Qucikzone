package com.quikzon.ad.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quikzon.ad.R;
import com.quikzon.ad.activities.Items_details;
import com.quikzon.ad.activities.OnBottomReachedListener;
import com.quikzon.ad.helper.EqualSpacingItemDecoration;
import com.quikzon.ad.helper.NonScrollRecyclerview;
import com.quikzon.ad.model.Product_attrubuts;
import com.quikzon.ad.utility.Utility;

import java.util.ArrayList;

public class VIew_all_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    ArrayList<Product_attrubuts> personNames;
    Context context;
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
        Rv_adapter_viewall slider = new Rv_adapter_viewall(context, personNames.get(position).getAd_images(),personNames.get(position).getAd_author_id(),personNames.get(position).getAd_id());
        ((MyViewHolder)viewHolder).img_scroll.addItemDecoration(new EqualSpacingItemDecoration(2));
         ((MyViewHolder)viewHolder).img_scroll.setAdapter(slider);

/*        ((MyViewHolder)viewHolder).img_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Items_details.class);
                intent.putExtra("userid",personNames.get(position).getAd_author_id());
                intent.putExtra("ad_id",personNames.get(position).getAd_id());
                context.startActivity(intent);
            }
        });*/
        ((MyViewHolder)viewHolder).Llitems.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onClick(View v) {
        Toast.makeText(context,"sdfss",Toast.LENGTH_LONG).show();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView Tvdescription,Tvaddress,Tvprice,Tvcondition,Tvtxtfiled;
        RecyclerView img_scroll;
        Button Btbut_now;
        LinearLayout Llitems;
        CardView card_view1;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            Tvdescription = (TextView) itemView.findViewById(R.id.Tvdescription);
            Tvcondition = (TextView) itemView.findViewById(R.id.Tvcondition);
            Tvaddress = (TextView) itemView.findViewById(R.id.Tvaddress);
            Tvprice = (TextView) itemView.findViewById(R.id.Tvprice);
            Tvtxtfiled = (TextView) itemView.findViewById(R.id.Tvtxtfiled);
            Btbut_now = (Button) itemView.findViewById(R.id.Btbut_now);
            Llitems = (LinearLayout) itemView.findViewById(R.id.Llitems);
            img_scroll = (RecyclerView) itemView.findViewById(R.id.img_scroll);
            card_view1 = (CardView) itemView.findViewById(R.id.card_view1);
        }
    }
}
