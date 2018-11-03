package com.quikzon.ad.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.quikzon.ad.Homeactivity;
import com.quikzon.ad.R;
import com.quikzon.ad.adapters.Sub_catgory_adpater;
import com.quikzon.ad.adapters.VIew_all_adapter;
import com.quikzon.ad.helper.NonScrollRecyclerview;
import com.quikzon.ad.helper.ScrollViewExt;
import com.quikzon.ad.model.Product_attrubuts;
import com.quikzon.ad.model.Products;
import com.quikzon.ad.model.Super_cat;
import com.quikzon.ad.restapi.Apiconfig;
import com.quikzon.ad.restapi.ScrollViewListener;
import com.quikzon.ad.services.LocationService;
import com.quikzon.ad.utility.Utility;

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
    @BindView(R.id.Lllayout)
    LinearLayout Lllayout;
    @BindView(R.id.no_ads)
    TextView no_ads;
    @BindView(R.id.Llsort)
    LinearLayout Llsort;
    @BindView(R.id.Llfilter)
    LinearLayout Llfilter;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.progress)
    ProgressBar progress;
  /*  @BindView(R.id.Svview)
    ScrollViewExt Svview;*/
    String cat_id = "";
    String sort_by="";
    String city_name="";
    int  page=1;
    boolean has_next_page=true;
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
        if (!Utility.getcity(this).equalsIgnoreCase("All India")) {
            city_name=Utility.getcity(this);
        }
        if (getIntent() != null) {
            String sub_Cat = getIntent().getStringExtra("sub_Cat");
            if (sub_Cat != null) {
                list = Utility.gson.fromJson(sub_Cat, new TypeToken<List<Product_attrubuts>>() {
                }.getType());
                if(list.size()>0) {
                    Lllayout.setVisibility(View.GONE);
                    Rvviewall.setVisibility(View.VISIBLE);
                    Rvviewall.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    view_all_adpater = new VIew_all_adapter(this, list);
                    Rvviewall.setAdapter(view_all_adpater);
                }
                else
                {
                    Rvviewall.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    view_all_adpater = new VIew_all_adapter(this, list);
                    Rvviewall.setAdapter(view_all_adpater);
                    Lllayout.setVisibility(View.VISIBLE);
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
                callapi(cat_id);
            }
        }
        Llfilter.setOnClickListener(this);
        Llsort.setOnClickListener(this);
        if(view_all_adpater!=null) {
            view_all_adpater.setOnBottomReachedListener(new OnBottomReachedListener() {
                @Override
                public void onBottomReached(int position) {
                    if (has_next_page) {
                        page++;
                        callapi(cat_id);
                    }
                }
            });
        }

    }
    public void callapi(String cat_id) {
        progress.setVisibility(View.VISIBLE);
        LinkedHashMap<String, String> login_detail = new LinkedHashMap<>();
        login_detail.put("ad_cats1", cat_id);
        login_detail.put("nearby_latitude", String.valueOf(LocationService.lat));
        login_detail.put("nearby_longitude", String.valueOf(LocationService.lng));
        login_detail.put("page_number", String.valueOf(page));
        if(sort_by.equalsIgnoreCase("low_to_hight")) {
            login_detail.put("sort_by", "price");
            login_detail.put("order", "asc");
        }
        else if(sort_by.equalsIgnoreCase("hight_to_to"))
        {
            login_detail.put("sort_by", "price");
            login_detail.put("order", "desc");
        }
        else if(sort_by.equalsIgnoreCase("recent_poset"))
        {

            login_detail.put("sort_by", "");
            login_detail.put("order", "");
        }

        else if(sort_by.equalsIgnoreCase("nearby"))
        {
            login_detail.put("sort_by", "nearby");
            login_detail.put("order", "desc");
        }

        login_detail.put("city_name", city_name);

        Request result = Utility.post(login_detail, Apiconfig.add_post);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        progress.setVisibility(View.GONE);
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
                            progress.setVisibility(View.GONE);
                            JSONObject obj = new JSONObject(response.body().string());
                            has_next_page=obj.getBoolean("has_next_page");
                            if(list.size()>0) {
                                JSONArray array=obj.getJSONObject("data").getJSONArray("ads");
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject object=array.getJSONObject(i);
                                    Product_attrubuts product_attrubuts=Utility.gson.fromJson(object.toString(),Product_attrubuts.class);
                                    list.add(product_attrubuts);
                                }

                            }
                            else {
                                list = Utility.gson.fromJson(obj.getJSONObject("data").getJSONArray("ads").toString(), new TypeToken<List<Product_attrubuts>>() {
                                }.getType());
                            }
                            if(list.size()>0) {
                                Rvviewall.setVisibility(View.VISIBLE);
                                Rvviewall.setLayoutManager(new LinearLayoutManager(Viewall.this, LinearLayoutManager.VERTICAL, false));
                                view_all_adpater = new VIew_all_adapter(Viewall.this, list);
                                Rvviewall.setAdapter(view_all_adpater);
                            }
                            else
                            {
                                no_ads.setVisibility(View.GONE);
                                Lllayout.setVisibility(View.VISIBLE);
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
            case R.id.Llsort:
                showCustomDialog();
                break;
        }
    }
    @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {

    }
    public void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view=layoutInflater.inflate(R.layout.activity_sort, null);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.drawable.button_border_tranfranet);
        window.setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        RelativeLayout Rvclose=(RelativeLayout)view.findViewById(R.id.Rvclose);
        Rvclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }
            }
        });
        TextView higlow=(TextView)view.findViewById(R.id.higlow);
        higlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_by="hight_to_to";
                dialog.dismiss();
                callapi(cat_id);
            }
        });
        TextView nearby=(TextView)view.findViewById(R.id.nearby);
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_by="nearby";
                dialog.dismiss();
                callapi(cat_id);
            }
        });

        TextView recent_post=(TextView)view.findViewById(R.id.recent_post);
        recent_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_by="";
                dialog.dismiss();
                callapi(cat_id);
            }
        });
        TextView lowhigh=(TextView)view.findViewById(R.id.lowhigh);
        lowhigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_by="low_to_hight";
                dialog.dismiss();
                callapi(cat_id);
            }
        });
        dialog.show();
    }
}
