package com.quikzon.add.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.quikzon.add.R;
import com.quikzon.add.adapters.My_ads_apater;
import com.quikzon.add.adapters.Sub_catgory;
import com.quikzon.add.helper.NonScrollRecyclerview;
import com.quikzon.add.model.Product_attrubuts;
import com.quikzon.add.restapi.Apiconfig;
import com.quikzon.add.restapi.User_details;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Account_ad_details extends Activity {
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.verfied)
    TextView verfied;
    @BindView(R.id.progressbars)
    ProgressBar progressbars;
    @BindView(R.id.Ivprofile)
    ImageView Ivprofile;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.Lvlist)
    RecyclerView Lvlistl;
    @BindView(R.id.scroll)
    ScrollView scroll;
    String  userid ="";
    ArrayList<Product_attrubuts> my_ads=new ArrayList<>();
    boolean is_my=false;
    My_ads_apater blog_adpater;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_ad_details);
        ButterKnife.bind(this);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        scroll.setNestedScrollingEnabled(true);
        Log.d("String","NOT ACTIVE USERS");
        Log.d("String","NOT ACTIVE USERS");
        Log.d("String","NOT ACTIVE USERS");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent()!=null){
            userid = getIntent().getStringExtra("userid");
            user_name.setText(getIntent().getStringExtra("author_name"));
            verfied.setText(getIntent().getStringExtra("author_type"));
            profilead_details(userid);
            try {
                if(userid.equalsIgnoreCase(Utility.single(Account_ad_details.this, User_details.id)))
                {
                    is_my=true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        profilead_details(userid);

    }

    private void profilead_details(String userid) {
        progressbars.setVisibility(View.VISIBLE);
        LinkedHashMap<String, String> selad_detail = new LinkedHashMap<>();
        selad_detail.put("userid", userid);
        Request result = Utility.post(selad_detail, Apiconfig.myads);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /*        no_ads.setVisibility(View.GONE);*/
                        Log.d("exce[", e.toString());
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
                            progressbars.setVisibility(View.GONE);
                            JSONObject obj = new JSONObject(response.body().string());
                            my_ads.clear();
                            my_ads = Utility.gson.fromJson(obj.getJSONObject("data").getJSONArray("ads").toString(), new TypeToken<List<Product_attrubuts>>() {
                            }.getType());

                            Lvlistl.setLayoutManager(new LinearLayoutManager(Account_ad_details.this, LinearLayoutManager.VERTICAL, false));
                            blog_adpater = new My_ads_apater(Account_ad_details.this, my_ads,is_my,Account_ad_details.this);
                            Lvlistl.setAdapter(blog_adpater);
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


    public  void delete_ad(String ad_id, String  userid, final int pos)
    {
        progressbars.setVisibility(View.VISIBLE);
        LinkedHashMap<String, String> selad_detail = new LinkedHashMap<>();
        selad_detail.put("ad_id", ad_id);
        selad_detail.put("userid", userid);
        Request result = Utility.post(selad_detail, Apiconfig.delete_ad);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /*        no_ads.setVisibility(View.GONE);*/
                        Log.d("exce[", e.toString());
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
                            progressbars.setVisibility(View.GONE);
                            JSONObject obj = new JSONObject(response.body().string());
                            Toast.makeText(Account_ad_details.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            if(obj.getString("success").equalsIgnoreCase("1"))
                            {
                                my_ads.remove(pos);
                                blog_adpater.notifyDataSetChanged();
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
}
