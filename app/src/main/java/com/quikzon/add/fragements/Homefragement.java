package com.quikzon.add.fragements;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.quikzon.add.R;
import com.quikzon.add.activities.Searchactivity;
import com.quikzon.add.adapters.Blog_adpater;
import com.quikzon.add.adapters.Homecategories;
import com.quikzon.add.adapters.Homeparentcategoires;
import com.quikzon.add.adapters.VIewpager;
import com.quikzon.add.helper.AutoScrollViewPager;
import com.quikzon.add.helper.NonScrollRecyclerview;
import com.quikzon.add.model.News_blog;
import com.quikzon.add.model.Products;
import com.quikzon.add.model.Slider;
import com.quikzon.add.model.Super_cat;
import com.quikzon.add.restapi.Apiconfig;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Homefragement extends Fragment implements View.OnClickListener {
    @BindView(R.id.rVcategories)
    RecyclerView rVcategories;
    @BindView(R.id.rVparent)
    NonScrollRecyclerview rVparent;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.Llsearch)
    LinearLayout Llsearch;
    @BindView(R.id.rVnews)
    RecyclerView rVnews;
    GridLayoutManager gridLayoutManagerl;
    Dialog dialog;
    JSONObject obj;
    View view;
    int spancount = 3;
    Homecategories homecategories;
    Homeparentcategoires homeparentcategoires;
    VIewpager vIewpager;
    ArrayList<Products> parents = new ArrayList<>();
    boolean isset = false;
    // for viewpager homefragement
    private ArrayList<Slider> imgArray = new ArrayList<>();
    ArrayList<Super_cat> superCats = new ArrayList<>();
    ArrayList<News_blog> newblog = new ArrayList<>();

    public Homefragement() {

    }

    Timer timer;
    TimerTask timerTask;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_homefragement, container, false);
        ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    private void init(View view) {
        if (TextUtils.isEmpty(Utility.get_home(getActivity())) && Utility.is_online(getActivity())) {
            callapi();
        } else {
            set_catgory(Utility.get_home(getActivity()));
        }
        Llsearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Llsearch:
                Intent intent = new Intent(getActivity(), Searchactivity.class);
                startActivity(intent);
        }
    }

    private void callapi() {
        LinkedHashMap<String, String> login_detail = new LinkedHashMap<>();
        login_detail.put("nearby_latitude", "");
        login_detail.put("nearby_longitude", "");
        Request result = Utility.post(login_detail, Apiconfig.home);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) throws
                    IOException {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject(response.body().string());
                                Utility.clear_home(getActivity());
                                Utility.save_home(getActivity(), obj.toString());
                                if (!isset) {
                                    set_catgory(obj.toString());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    // INTERFACE FOR FRAGEMENT LAYOUT OPEN
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void set_catgory(String object) {
        try {
            obj = new JSONObject(object);

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //catgorys
                    try {
                        // horizontal divider
                        superCats = Utility.gson.fromJson(obj.getJSONObject("data").getJSONArray("cat_icons").toString(), new TypeToken<List<Super_cat>>() {
                        }.getType());
                        DividerItemDecoration verticalDecoration = new DividerItemDecoration(rVcategories.getContext(),
                                DividerItemDecoration.HORIZONTAL);
                        Drawable verticalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.vertical_divider);
                        verticalDecoration.setDrawable(verticalDivider);
                        rVcategories.addItemDecoration(verticalDecoration);

                        // vertical divider
                        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(rVcategories.getContext(),
                                DividerItemDecoration.VERTICAL);
                        Drawable horizontalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider);
                        horizontalDecoration.setDrawable(horizontalDivider);
                        rVcategories.addItemDecoration(horizontalDecoration);
                        gridLayoutManagerl = new GridLayoutManager(getActivity(), spancount);
                        gridLayoutManagerl.setOrientation(LinearLayoutManager.VERTICAL);
                        rVcategories.setLayoutManager(gridLayoutManagerl);
                        Homecategories homecategories = new Homecategories(getActivity(), superCats);
                        rVcategories.setAdapter(homecategories);

                        //slider images
                        imgArray = Utility.gson.fromJson(obj.getJSONObject("settings").getJSONArray("home_slider").toString(), new TypeToken<List<Slider>>() {
                        }.getType());

                        VIewpager slider = new VIewpager(getActivity(), imgArray);
                        viewPager.setAdapter(slider);
                        satrt_auto_acrolling();
                        //ads adpater
                        parents = Utility.gson.fromJson(obj.getJSONObject("data").getJSONArray("home_blocks").toString(), new TypeToken<List<Products>>() {
                        }.getType());
                            rVparent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
                        Homeparentcategoires homeparentcategoires = new Homeparentcategoires(getActivity(), parents);
                        rVparent.setAdapter(homeparentcategoires);
                        // news adapter blog
                        //ads adpater
                        newblog = Utility.gson.fromJson(obj.getJSONObject("data").getJSONObject("latest_blog").getJSONArray("blogs").toString(), new TypeToken<List<News_blog>>() {
                        }.getType());
                        rVnews.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        Blog_adpater blog_adpater = new Blog_adpater(getActivity(), newblog);
                        rVnews.setAdapter(blog_adpater);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    isset = true;
                    if (Utility.is_online(getActivity())) {
                        callapi();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void satrt_auto_acrolling() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable() {

                    @Override
                    public void run() {
                        viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % imgArray.size());
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerTask != null) {
            timerTask.cancel();
        }
    }
}
