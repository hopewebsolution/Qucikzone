package com.quikzon.add.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.quikzon.add.Homeactivity;
import com.quikzon.add.R;
import com.quikzon.add.adapters.Sub_catgory_adpater;
import com.quikzon.add.adapters.VIew_all_adapter;
import com.quikzon.add.helper.ScrollViewExt;
import com.quikzon.add.model.Product_attrubuts;
import com.quikzon.add.model.Products;
import com.quikzon.add.model.Super_cat;
import com.quikzon.add.restapi.Apiconfig;
import com.quikzon.add.restapi.ScrollViewListener;
import com.quikzon.add.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Viewall extends AppCompatActivity implements View.OnClickListener,ScrollViewListener {
    Sub_catgory_adpater sub_catgory_adpater;
    ArrayList<Product_attrubuts> list = new ArrayList<>();
    VIew_all_adapter view_all_adpater;
    @BindView(R.id.Rvviewall)
    RecyclerView Rvviewall;
    @BindView(R.id.rel_extra_header)
    LinearLayout rel_extra_header;
    @BindView(R.id.rel_main_header)
    RelativeLayout rel_main_header;
    @BindView(R.id.Ivback)
    ImageView Ivback;
  /*  @BindView(R.id.Svview)
    ScrollViewExt Svview;*/
    @BindView(R.id.no_ads)
    TextView no_ads;
    String cat_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        ButterKnife.bind(this);
        inti();
    }

    @SuppressLint("SetTextI18n")
    private void inti() {
        //set toolbar layout
        rel_main_header.setVisibility(View.GONE);
        rel_extra_header.setVisibility(View.VISIBLE);
        Ivback.setOnClickListener(this);

        if (getIntent() != null) {
            String sub_Cat = getIntent().getStringExtra("sub_Cat");
            if (sub_Cat != null) {
                list = Utility.gson.fromJson(sub_Cat, new TypeToken<List<Product_attrubuts>>() {
                }.getType());
                if(list.size()>0) {
                    no_ads.setVisibility(View.GONE);
                    Rvviewall.setVisibility(View.VISIBLE);
                    Rvviewall.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    view_all_adpater = new VIew_all_adapter(this, list);
                    Rvviewall.setAdapter(view_all_adpater);
                }
                else
                {
                    no_ads.setVisibility(View.VISIBLE);
                    Rvviewall.setVisibility(View.GONE);
                }
            } else {
//                try {
//                    JSONObject obj = new JSONObject(Utility.get_home(this));
//                    ArrayList<Products> parents = Utility.gson.fromJson(obj.getJSONObject("data").getJSONArray("home_blocks").toString(), new TypeToken<List<Products>>() {
//                    }.getType());
//                    for (int i = 0; i < parents.size(); i++) {
//                        for (int j = 0; j < parents.get(i).getAds().size(); j++) {
//                            if (parents.get(i).getAds().get(j).getAd_cats().get(0).getId().equalsIgnoreCase(getIntent().getStringExtra("cat_id"))) {
//                                list.add(parents.get(i).getAds().get(j));
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                cat_id = getIntent().getStringExtra("cat_id");
                no_ads.setVisibility(View.VISIBLE);
                no_ads.setText(R.string.loading_ads);
                callapi(cat_id);
            }
        }
     /*   Svview.setScrollViewListener(this);*/
    }

    private void callapi(String cat_id) {
        LinkedHashMap<String, String> login_detail = new LinkedHashMap<>();
        login_detail.put("ad_cats1", cat_id);
        Request result = Utility.post(login_detail, Apiconfig.add_post);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        no_ads.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) throws
                    IOException {

                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    public void run() {
                        try {
                            no_ads.setVisibility(View.GONE);
                            JSONObject obj = new JSONObject(response.body().string());
                            list = Utility.gson.fromJson(obj.getJSONObject("data").getJSONArray("ads").toString(), new TypeToken<List<Product_attrubuts>>() {
                            }.getType());
                            if(list.size()>0) {
                                no_ads.setVisibility(View.GONE);
                                Rvviewall.setVisibility(View.VISIBLE);
                                Rvviewall.setLayoutManager(new LinearLayoutManager(Viewall.this, LinearLayoutManager.VERTICAL, false));
                                view_all_adpater = new VIew_all_adapter(Viewall.this, list);
                                Rvviewall.setAdapter(view_all_adpater);
                            }
                            else
                            {
                                no_ads.setVisibility(View.VISIBLE);
                                no_ads.setText(R.string.no_ads);
                                Rvviewall.setVisibility(View.GONE);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Ivback:
                Intent intent = new Intent(Viewall.this, Homeactivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {

    }
}
