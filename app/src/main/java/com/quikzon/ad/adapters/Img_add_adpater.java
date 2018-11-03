package com.quikzon.ad.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quikzon.ad.R;
import com.quikzon.ad.activities.Post_add;
import com.quikzon.ad.model.Post_imgs;
import com.quikzon.ad.utility.Utility;

import java.util.ArrayList;

public class Img_add_adpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Post_imgs> view_img;
    Context context;
    Post_add post_add_obj;
    public Img_add_adpater(Context context, Post_add post_add, ArrayList<Post_imgs> imgs) {
        this.context = context;
        this.view_img = imgs;
        this.post_add_obj=post_add;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_img, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (view_img.get(position).isIs_system()) {
            ((MyViewHolder) viewHolder).imageView.setImageURI(Uri.parse(view_img.get(position).getUri()));

        } else {
            Utility.Set_image(view_img.get(position).getUrl(), ((MyViewHolder) viewHolder).imageView);
        }
        ((MyViewHolder) viewHolder).close.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                    post_add_obj.delete_img(position);
                 }
             });
    }

    @Override
    public int getItemCount() {
        return view_img.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       ImageView imageView;
       ImageView close;
        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            imageView = (ImageView) itemView.findViewById(R.id.thumb);
            close = (ImageView) itemView.findViewById(R.id.close);
        }
    }

}
