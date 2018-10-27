package com.quikzon.add.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quikzon.add.R;
import com.quikzon.add.model.Adimg_model;
import com.quikzon.add.model.Slider;
import com.quikzon.add.utility.Utility;

import java.util.ArrayList;

public class Full_screen_slider extends PagerAdapter {
    private ArrayList<Adimg_model> images;
    private LayoutInflater inflater;
    private Context context;

    public Full_screen_slider(Context context, ArrayList<Adimg_model> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slider_screen, view, false);
        ImageView imags = (ImageView) myImageLayout
                .findViewById(R.id.image);
        Utility.Set_image(images.get(position).getFull(),imags);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
