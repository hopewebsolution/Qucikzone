package com.quikzon.add.fragements;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.quikzon.add.R;
import com.quikzon.add.activities.Searchactivity;
import com.quikzon.add.activities.Viewall;
import com.quikzon.add.adapters.ExpandableAdapter;
import com.quikzon.add.adapters.Homecategories;
import com.quikzon.add.adapters.Homeparentcategoires;
import com.quikzon.add.adapters.VIew_all_adapter;
import com.quikzon.add.adapters.VIewpager;
import com.quikzon.add.helper.NonScrollRecyclerview;
import com.quikzon.add.model.Product_attrubuts;
import com.quikzon.add.model.Products;
import com.quikzon.add.model.Super_cat;
import com.quikzon.add.restapi.Apiconfig;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

public class Addpost_fragement extends Fragment implements View.OnClickListener {
    View view;
    ArrayList <Super_cat> superCats;
    Addpost_fragement addpost_fragement;
    public Addpost_fragement() {

    }
    ExpandableListView expandableListView;
    public static String parent_cat_name="";
    public static String sub_cat_name="";
    public static String sub_cat_id="";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_addpost, container, false);
        setRetainInstance(true);
        ButterKnife.bind(this, view);
        init(view);
        setdata();
        return view;
    }
    private void init(View view) {
        if (superCats==null){
            superCats = new ArrayList<>();
        }else{
            superCats.clear();
        }
        addpost_fragement = new Addpost_fragement();
        expandableListView=(ExpandableListView)view.findViewById(R.id.expandableListView1);
    }
    private void setdata() {
        String object = Utility.get_home(getActivity());
        try {
            JSONObject obj = new JSONObject(object);
            superCats = Utility.gson.fromJson(obj.getJSONObject("data").getJSONArray("cat_icons").toString(), new TypeToken<List<Super_cat>>() {}.getType());
            ExpandableAdapter adapter=new ExpandableAdapter(getActivity(),superCats,addpost_fragement);
            expandableListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Llsearch:
        }
    }

    public  void showaddpostlayout(int listPosition, int expandedListPosition){
           /* superCats.get(listPosition).getName();
            superCats.get(listPosition).getSub_cat().get(expandedListPosition).getName();*/

    }

    public  void hidelayout(){

    }
    // INTERFACE FOR FRAGEMENT LAYOUT OPEN
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}