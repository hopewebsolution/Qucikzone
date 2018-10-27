package com.quikzon.add.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.quikzon.add.R;
import com.quikzon.add.activities.Product_list;
import com.quikzon.add.model.Product_list_model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Product_list_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private  ArrayList<Product_list_model> emplist;
    LinkedHashMap<String, String> storeovertimeid = new LinkedHashMap<>();
    public Product_list_adapter(Context ctx , ArrayList<Product_list_model> emplists) {
        this.context = ctx;
        this.emplist = emplists;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_homecategories, parent, false);

        // set the view's size, margins, paddings and layout parameters
        Product_list_adapter.MyViewHolder vh = new Product_list_adapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((Product_list_adapter.MyViewHolder)viewHolder).Tvdescription.setText("sdasd");
    }

    @Override
    public int getItemCount() {
        return emplist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView Tvdescription,Tvaddress,Tvprice;
        ImageView Ivimg;
        Button Btbut_now;
        LinearLayout Llitems;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            Tvdescription = (TextView) itemView.findViewById(R.id.Tvdescription);
            Tvaddress = (TextView) itemView.findViewById(R.id.Tvaddress);
            Tvprice = (TextView) itemView.findViewById(R.id.Tvprice);
            Ivimg = (ImageView) itemView.findViewById(R.id.Ivimg);
            Btbut_now = (Button) itemView.findViewById(R.id.Btbut_now);
            Llitems = (LinearLayout) itemView.findViewById(R.id.Llitems);
        }
    }
}
