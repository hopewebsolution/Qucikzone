package com.quikzon.add.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.quikzon.add.R;
import com.quikzon.add.model.Sub_catgory_model;
import com.quikzon.add.model.Super_sub_Cat;
import com.quikzon.add.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class Sub_catgory_adpater extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<Super_sub_Cat> emplist;
    ArrayList<Super_sub_Cat> filteremplist;
    public Sub_catgory_adpater(Context applicationContext, ArrayList<Super_sub_Cat> list) {
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
            listview = layoutInflater.inflate(R.layout.row_catgory,viewGroup,false);
            holder = new Holder();
            listview.setTag(holder);
        }else {
            holder = (Holder) listview.getTag();
        }
        holder.sub_catgory = (TextView)listview.findViewById(R.id.sub_catgory);
        holder.icon = (ImageView) listview.findViewById(R.id.icon);
        holder.sub_catgory.setText(emplist.get(positon).getName());
        if (!TextUtils.isEmpty(emplist.get(positon).getCat_icon())) {
/*            Picasso.with(context).load(emplist.get(positon).getCat_icon())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.icon);*/
            Utility.Set_image(emplist.get(positon).getCat_icon(),holder.icon);
        }
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
                    List<Super_sub_Cat> filteredList = new ArrayList<>();
                    if (filteremplist != null) {
                        for (Super_sub_Cat row : filteremplist) {

                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        emplist = (ArrayList<Super_sub_Cat>) filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = emplist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                emplist = (ArrayList<Super_sub_Cat>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}

