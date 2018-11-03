package com.quikzon.ad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.quikzon.ad.R;
import com.quikzon.ad.model.Account_model;
import com.quikzon.ad.restapi.User_details;

import java.util.ArrayList;
import java.util.List;

public class Profile_adapter extends BaseAdapter {
    private Context context;
    private  ArrayList<Account_model> emplist;

    public Profile_adapter(Context applicationContext, ArrayList<Account_model> list) {
        this.context = applicationContext;
        this.emplist = list;
    }

    @Override
    public int getCount() {
        return emplist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int positon, View listview, ViewGroup viewGroup) {
        Holder holder;
        LayoutInflater layoutInflater;
        if (listview==null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listview = layoutInflater.inflate(R.layout.row_desc,viewGroup,false);
            holder = new Holder();
            listview.setTag(holder);
        }else {
            holder = (Holder) listview.getTag();
        }
        holder.lable = (TextView)listview.findViewById(R.id.lable);
        holder.value = (TextView) listview.findViewById(R.id.value);
        if (emplist.get(positon).getKey().equalsIgnoreCase(User_details.Name )|| emplist.get(positon).getKey().equalsIgnoreCase(User_details.Email)||emplist.get(positon).getKey().equalsIgnoreCase(User_details.Image)){
            holder.lable.setVisibility(View.GONE);
            holder.value.setVisibility(View.GONE);
        }else {
            holder.lable.setVisibility(View.VISIBLE);
            holder.value.setVisibility(View.VISIBLE);
            holder.lable.setText(emplist.get(positon).getKey());
            holder.value.setText(emplist.get(positon).getValue());
        }
        return listview;
    }
    private static class Holder{
        TextView lable,value;
    }
}