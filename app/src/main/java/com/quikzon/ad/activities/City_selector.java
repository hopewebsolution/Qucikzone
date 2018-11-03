package com.quikzon.ad.activities;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.quikzon.ad.R;
import com.quikzon.ad.adapters.City_adpater;
import com.quikzon.ad.model.City_model;
import com.quikzon.ad.services.LocationService;
import com.quikzon.ad.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class City_selector extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "City_selector";
    @BindView(R.id.get_location)
    TextView get_location;
    @BindView(R.id.mainprogress)
    ProgressBar mainprogress;
    ListView lv;
    City_adpater sub_catgory_adpater;
    EditText serach;
    String city_name="";
    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    String lattitude,longitude;

    ArrayList<City_model> list = new ArrayList<>();
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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
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
                if (LocationService.lat!=0.0 && LocationService.lng!=0.0){
                    city_name  =  Utility.getlatlngcity(this,LocationService.lat,LocationService.lng);
                    if (!city_name.equalsIgnoreCase("")){
                        mainprogress.setVisibility(View.GONE);
                        Utility.setcity(this,city_name,"not_specfied");
                        Log.d("city_name",city_name);
                        finish();
                    }else{
                        mainprogress.setVisibility(View.VISIBLE);
                        Toast.makeText(this,"Please try again",Toast.LENGTH_LONG).show();
                        Log.d("city_name",city_name);
                    }
                }
                break;
        }
    }
}

