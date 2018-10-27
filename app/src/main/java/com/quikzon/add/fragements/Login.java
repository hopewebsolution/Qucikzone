package com.quikzon.add.fragements;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.quikzon.add.adapters.VIewpager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import com.quikzon.add.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends Fragment implements View.OnClickListener {

    @BindView(R.id.tabDots)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.continuesl)
    Button continue_btn;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.firstnameWrapper)
    TextInputLayout firstnameWrapper;


    private static final String EMAIL = "email";
    private static int currentPage = 0;
    private final Integer[] img = {R.drawable.first,R.drawable.second,R.drawable.third};

    private ArrayList<Integer> imgArray = new ArrayList<Integer>();
    CallbackManager callbackManager;
    View view;
    public Login(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_login, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ButterKnife.bind(this,view);
        init(view);
        setdata();
        return view;
    }
    private void init(View view) {
        for (int i = 0; i < img.length; i++) {
            imgArray.add(img[i]);
        }
        continue_btn.setOnClickListener(this);
    }
    private void setdata(){
/*        viewPager.setAdapter(new VIewpager(getActivity(),imgArray));
        tabLayout.setupWithViewPager(viewPager,true);*/
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == img.length) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continuesl:
                Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                firstnameWrapper.startAnimation(shake);
        }

    }

    // INTERFACE FOR FRAGEMENT LAYOUT OPEN
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
