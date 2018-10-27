package com.quikzon.add.activities;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.quikzon.add.R;
import com.quikzon.add.adapters.Full_screen_slider;
import com.quikzon.add.adapters.VIewpager;
import com.quikzon.add.model.Adimg_model;
import com.quikzon.add.model.Slider;
import com.quikzon.add.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Full_screen_img extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.pager)
    ViewPager pager;
    ArrayList<Adimg_model> imgs = new ArrayList<>();
    @BindView(R.id.img_status)
    TextView img_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_img);
        ButterKnife.bind(this);
        inti();
    }

    private void inti() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent() != null) {
            //slider images
            imgs = Utility.gson.fromJson(getIntent().getStringExtra("imgs"), new TypeToken<List<Adimg_model>>() {
            }.getType());

            Full_screen_slider slider = new Full_screen_slider(this, imgs);
            pager.setAdapter(slider);

            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    img_status.setText(i+1 + "/" + imgs.size());
                }

                @Override
                public void onPageSelected(int i) {

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }
    }
}
