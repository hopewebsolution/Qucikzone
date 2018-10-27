package com.quikzon.add.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.quikzon.add.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Product_list extends AppCompatActivity {
    @BindView(R.id.Rvproductlist)
    RecyclerView Rvproductlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        inti();
        setdata();
    }

    private void inti() {
    }

    private void setdata() {
    }
}
