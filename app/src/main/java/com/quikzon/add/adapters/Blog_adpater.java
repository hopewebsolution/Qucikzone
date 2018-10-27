package com.quikzon.add.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.quikzon.add.R;
import com.quikzon.add.model.Ad_post_view;
import com.quikzon.add.model.News_blog;
import com.quikzon.add.utility.Utility;

import java.util.ArrayList;

public class Blog_adpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<News_blog> view;
    Context context;

    public Blog_adpater(Context context, ArrayList<News_blog> view) {
        this.context = context;
        this.view = view;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

            ((MyViewHolder) viewHolder).title.setText(view.get(position).getTitle());
            ((MyViewHolder) viewHolder).date.setText(view.get(position).getDate());
            Utility.Set_image(view.get(position).getImage(), ((MyViewHolder) viewHolder).icon);

    }

    @Override
    public int getItemCount() {
        return view.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView title,date;
        ImageView icon;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            title = (TextView) itemView.findViewById(R.id.news_title);
            date = (TextView) itemView.findViewById(R.id.news_date);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }
}
