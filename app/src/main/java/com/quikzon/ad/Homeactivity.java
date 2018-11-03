package com.quikzon.ad;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quikzon.ad.activities.Chat_user;
import com.quikzon.ad.activities.City_selector;
import com.quikzon.ad.activities.Login;
import com.quikzon.ad.fragements.Account_fragement;
import com.quikzon.ad.fragements.Addpost_fragement;
import com.quikzon.ad.fragements.Homefragement;
import com.quikzon.ad.utility.Utility;

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
    String curent_city = "";
    public static boolean is_call_api = false;
    FragmentTransaction fragmentTransaction;
    public static Activity activity;
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
        if(TextUtils.isEmpty(Utility.getcity(this))||Utility.getcity(this).equalsIgnoreCase(""))
        {
            Utility.setcity(this,"All India","00");
            curent_city = "All India";
        }
        tv_city.setText(Utility.getcity(this));
        curent_city = Utility.getcity(this);
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
                Log.d("Homeactivity","fragement called");
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
                    if (!is_call_api)
                    startActivityForResult(new Intent(this, City_selector.class),1);
                    else
                     Toast.makeText(getApplicationContext(), R.string.wait_msg,Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            if (curent_city.equalsIgnoreCase(Utility.getcity(this))){
                tv_city.setText(Utility.getcity(this));
                Log.d("after ","same city");
            }else{
                curent_city = Utility.getcity(this);
                tv_city.setText(Utility.getcity(this));
                Utility.clear_home(this);
                start_fragment(new Homefragement());
                Log.d("after ","city selected chang`e");
            }
        }else if (resultCode==2){
            tv_city.setText(Utility.getcity(this));
 /*           Utility.clear_home(this);
            start_fragment(new Homefragement());*/
            Log.d("after ","RESULTOK");
        }
    }
}