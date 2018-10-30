package com.quikzon.add;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quikzon.add.activities.Chat_user;
import com.quikzon.add.activities.City_selector;
import com.quikzon.add.activities.Login;
import com.quikzon.add.fragements.Account_fragement;
import com.quikzon.add.fragements.Addpost_fragement;
import com.quikzon.add.fragements.Chat_fragement;
import com.quikzon.add.fragements.Homefragement;
import com.quikzon.add.services.MyLocationService;
import com.quikzon.add.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Homeactivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    @BindView(R.id.select_city)
    FrameLayout select_city;
    @BindView(R.id.city)
    TextView tv_city;
    @BindView(R.id.rel_main_header)
    RelativeLayout rel_main_header;
    @BindView(R.id.rel_extra_header)
    LinearLayout rel_extra_header;
    @BindView(R.id.account_header)
    LinearLayout account_header;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);
        ButterKnife.bind(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setSelectedItemId(R.id.navigation_home);
        init();
    }

    private void init() {
        select_city.setOnClickListener(this);
        tv_city.setText(Utility.getcity(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
        switch (Item.getItemId()) {
            case R.id.navigation_home:
                start_fragment(new Homefragement());
                rel_main_header.setVisibility(View.VISIBLE);
                rel_extra_header.setVisibility(View.GONE);
                account_header.setVisibility(View.GONE);
                return true;
            case R.id.navigation_chat:
                if(Utility.get_login(this)) {
                    start_fragment(new Chat_user());
                }
                else
                {
                    Intent i = new Intent(this, Login.class);
                    startActivityForResult(i, Utility.login);
                }
                return true;
            case R.id.navigation_post:
                if(Utility.get_login(this)) {
                    start_fragment(new Addpost_fragement());
                    rel_main_header.setVisibility(View.GONE);
                    account_header.setVisibility(View.GONE);
                    rel_extra_header.setVisibility(View.VISIBLE);
                }
                else
                {
                    Intent i = new Intent(this, Login.class);
                    startActivityForResult(i, Utility.login);
                }
                return true;
            case R.id.navigation_account:
                if(Utility.get_login(this)) {
                    rel_extra_header.setVisibility(View.GONE);
                    rel_main_header.setVisibility(View.GONE);
                    account_header.setVisibility(View.VISIBLE);
                    start_fragment(new Account_fragement());
                }
                else
                {
                    Intent i = new Intent(this, Login.class);
                    startActivityForResult(i, Utility.login);
                }
                return true;
        }
        return false;
    }
    public void start_fragment(Fragment fragment)
    {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_city:
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    }, 10);
                }
                else {
                    Intent intent = new Intent(this, City_selector.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_city.setText(Utility.getcity(this));
    }
}