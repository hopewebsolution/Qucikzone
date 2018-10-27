package com.quikzon.add.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.quikzon.add.MainActivity;
import com.quikzon.add.R;
import com.quikzon.add.adapters.City_adpater;
import com.quikzon.add.model.City_model;
import com.quikzon.add.services.MyLocationService;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class City_selector extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.get_location)
    TextView get_location;
    ListView lv;
    City_adpater sub_catgory_adpater;
    EditText serach;
    MyLocationService locationService;
    ArrayList<City_model> list = new ArrayList<>();
    private static final int REQUEST_CODE_PERMISSION = 2;
    LocationManager locationManager;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    double longitude=0.0;
    double latitude=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selector);
        ButterKnife.bind(this);
        inti();
        setdata();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        getSupportActionBar().setTitle(R.string.select_city);
        serach=(EditText)findViewById(R.id.edt_search);

        serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sub_catgory_adpater.getFilter().filter(s.toString());
                Log.d("cityis", String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void inti() {

        // get all data from JSON
        if (list==null){
            list = new ArrayList<>();
        }else{
            list.clear();
        }
        get_location.setOnClickListener(this);
    }

    private void setdata() {
/*        City_model city_model = new City_model();
        city_model.setCity_id("00");
        city_model.setCity_name("All india");
        list.add(0, city_model);*/
        String object = Utility.get_home(this);
        Log.d("dssdsd",object);
        try {
            JSONObject obj = new JSONObject(object);
            list = Utility.gson.fromJson(obj.getJSONObject("settings").getJSONArray("citys").toString(), new TypeToken<List<City_model>>() {
           }.getType());
            City_model city_model = new City_model();
            city_model.setCity_id("00");
            city_model.setCity_name("All india");
            list.add(0, city_model);
            Log.d("type",obj.getJSONObject("settings").getJSONArray("citys").toString());
            lv = (ListView) findViewById(R.id.lv);
            sub_catgory_adpater = new City_adpater(this, list);
            lv.setAdapter(sub_catgory_adpater);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_location:
                if(latitude!=0.0&&longitude!=0.0) {
                    boolean is_get = true;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getCity_name().equalsIgnoreCase(Utility.get_city(this, latitude, longitude))) {
                            Utility.setcity(this, list.get(i).getCity_name(), list.get(i).getCity_id());
                            is_get = false;
                            break;
                        }
                    }
                    if (is_get) {
                        Utility.setcity(this, "All India", "00");
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Try  Again ",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
