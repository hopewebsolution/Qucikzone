package com.quikzon.add.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.quikzon.add.Homeactivity;
import com.quikzon.add.R;
import com.quikzon.add.model.City_model;
import com.quikzon.add.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class City_adpater extends BaseAdapter implements Filterable {
    private Activity context;
    private ArrayList<City_model> emplist;
    ArrayList<City_model> filteremplist;
    public City_adpater(Activity applicationContext, ArrayList<City_model> list) {
        this.context = applicationContext;
        this.emplist = list;
        this.filteremplist = list;
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
            listview = layoutInflater.inflate(R.layout.row_city,viewGroup,false);
            holder = new Holder();
            listview.setTag(holder);
        }else {
            holder = (Holder) listview.getTag();
        }
        holder.sub_catgory = (TextView)listview.findViewById(R.id.sub_catgory);
        holder.sub_catgory.setText(emplist.get(positon).getCity_name());
        listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.setcity(context, emplist.get(positon).getCity_name(), emplist.get(positon).getCity_id());
                Intent intent=new Intent(context, Homeactivity.class);
                context.startActivity(intent);
                context.finish();
            }
        });
        return listview;
    }
    private static class Holder{
        TextView sub_catgory;
        ImageView icon;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    emplist = filteremplist;
                } else {
                    List<City_model> filteredList = new ArrayList<>();
                    if (filteremplist != null) {
                        for (City_model row : filteremplist) {

// name match condition. this might differ depending on your requirement
// here we are looking for name or phone number match
                            if (row.getCity_name().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        emplist = (ArrayList<City_model>) filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = emplist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                emplist = (ArrayList<City_model>) filterResults.values;
// refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}

