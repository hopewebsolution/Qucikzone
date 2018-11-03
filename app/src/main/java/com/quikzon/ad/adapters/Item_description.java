package com.quikzon.ad.adapters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.quikzon.ad.R;
import com.quikzon.ad.model.Item_descritiption;
import com.quikzon.ad.model.item_des_key;

import java.util.ArrayList;

public class Item_description extends BaseAdapter {
         Context context;
        ArrayList<item_des_key> arrayList;
        public Item_description(Context ctx, ArrayList<item_des_key> arrayList) {
            this.context = ctx;
            this.arrayList = arrayList;
        }
        @Override
        public int getCount() {
            return arrayList.size();
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
            if (listview==null){
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                listview = layoutInflater.inflate(R.layout.row_item_descrition,viewGroup,false);
                holder = new Holder();
                listview.setTag(holder);
            }else {
                holder = (Holder) listview.getTag();
            }

            holder.Tvlabel = (TextView)listview.findViewById(R.id.Tvlabel);
            holder.Tvvalue = (TextView) listview.findViewById(R.id.Tvvalue);
            holder.Tvlabel.setText(arrayList.get(positon).getKey());
            holder.Tvvalue.setText(arrayList.get(positon).getValue());
            return listview;
        }
        private static class Holder{
            TextView Tvlabel,Tvvalue;
        }
}
