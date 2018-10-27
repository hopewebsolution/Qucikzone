package com.quikzon.add.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quikzon.add.R;
import com.quikzon.add.activities.Post_add;
import com.quikzon.add.activities.Viewall;
import com.quikzon.add.fragements.Addpost_fragement;
import com.quikzon.add.model.Super_cat;
import com.quikzon.add.model.Super_sub_Cat;
import com.quikzon.add.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Super_cat> expandableListTitle;
    private  Addpost_fragement addpost_fragement;
    public ExpandableAdapter(Context context, ArrayList<Super_cat> expandableListTitle, Addpost_fragement addpost_fragement) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.addpost_fragement = addpost_fragement;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return  this.expandableListTitle.get(listPosition).getSub_cat().get(expandedListPosition).getName();
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent_view, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.parent_id);
        expandedListTextView.setText(expandableListTitle.get(listPosition).getSub_cat().get(expandedListPosition).getName());
        Log.d("main",expandedListTextView.getText().toString());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addpost_fragement.parent_cat_name = expandableListTitle.get(listPosition).getName();
                Addpost_fragement.sub_cat_name = expandableListTitle.get(listPosition).getSub_cat().get(expandedListPosition).getName();
                Addpost_fragement.sub_cat_id=expandableListTitle.get(listPosition).getSub_cat().get(expandedListPosition).getSub_cat_id();
                Intent intent = new Intent(context, Post_add.class);
                intent.putExtra("ad_id","");
                intent.putExtra("title","");
                intent.putExtra("desc","");
                intent.putExtra("adress","");
                intent.putExtra("name","");
                intent.putExtra("mobile","");
                intent.putExtra("imgs","");
                context.startActivity(intent);

            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListTitle.get(listPosition).getSub_cat().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_view, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.child_id);
        ImageView  icon = (ImageView) convertView.findViewById(R.id.icon);
        listTitleTextView.setText(expandableListTitle.get(listPosition).getName());
        Utility.Set_image(expandableListTitle.get(listPosition).getImg(),icon);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
