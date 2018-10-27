package com.quikzon.add.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.quikzon.add.Homeactivity;
import com.quikzon.add.R;
import com.quikzon.add.utility.Utility;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.full_screen(this);
/*        this.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        setContentView(R.layout.activity_splash);
        initApp();
    }
    public void initApp(){
/*        final boolean login_status= Utlity.get_login(SplashActivity.this);*/
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                  /*  startService(new Intent(SplashActivity.this, GPSTracker.class));*/
                    startActivity(new Intent(SplashActivity.this , Homeactivity.class));
                    finish();
                }
            }
        };
        timer.start();
    }
}
