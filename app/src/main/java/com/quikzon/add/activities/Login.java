package com.quikzon.add.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.quikzon.add.R;
import com.quikzon.add.adapters.VIewpager;
import com.quikzon.add.helper.SmsBroadcastReceiver;
import com.quikzon.add.model.Slider;
import com.quikzon.add.restapi.Apiconfig;
import com.quikzon.add.utility.SendSms;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.callback.CallbackHandler;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.tabDots)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.continuesl)
    Button continue_btn;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.password_wrapper)
    TextInputLayout password_wrapper;
    @BindView(R.id.firstnameWrapper)
    TextInputLayout firstnameWrapper;
    @BindView(R.id.mainprogress)
    ProgressBar mainprogress;
    @BindView(R.id.Chk)
    CheckBox Chk;
    @BindView(R.id.Btnlogins)
    Button Btnlogins;
    @BindView(R.id.Btnsendotp)
    Button Btnsendotp;
    int currentPage = 0;
    boolean isChecked = false;
    private Random random;
    String otp_code;
    Timer timer;
    boolean is_mobile = false;
    TimerTask timerTask;
    private ArrayList<Slider> imgArray = new ArrayList<>();
    Dialog register_progress;
    SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        setdata();


    }

    private void init() {
        random = new Random();
        continue_btn.setOnClickListener(this);
        Chk.setOnClickListener(this);
        Btnsendotp.setOnClickListener(this);
        Btnlogins.setOnClickListener(this);
        email.setHint("Enter Mobile Number/ Email");


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean digitsOnly = TextUtils.isDigitsOnly(s.toString());
                if (digitsOnly) {
                    password_wrapper.setVisibility(View.GONE);
                    Btnsendotp.setVisibility(View.VISIBLE);
                    Btnlogins.setVisibility(View.GONE);
                    continue_btn.setVisibility(View.GONE);
                    email.setHint("Mobile");
                    password.setHint("OTP");
                    is_mobile = true;
                } else {
                    is_mobile = false;
                    password_wrapper.setVisibility(View.VISIBLE);
                    continue_btn.setVisibility(View.GONE);
                    Btnsendotp.setVisibility(View.GONE);
                    email.setHint("Email");
                    password.setHint("Password");
                    Btnlogins.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setdata() {
        try {
            JSONObject obj = new JSONObject(Utility.get_home(this));

            imgArray = Utility.gson.fromJson(obj.getJSONObject("settings").getJSONArray("home_slider").toString(), new TypeToken<List<Slider>>() {
            }.getType());
            VIewpager slider = new VIewpager(Login.this, imgArray);
            viewPager.setAdapter(slider);
            satrt_auto_acrolling();
        } catch (JsonIOException e) {

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continuesl:
                //chkvalidation();
                if (chkvalidation()) {
                    Chk.setVisibility(View.GONE);
                }
                break;
            case R.id.Chk:
                if (Chk.isChecked()) {
                    password_wrapper.setVisibility(View.VISIBLE);
                    continue_btn.setVisibility(View.GONE);
                    Btnsendotp.setVisibility(View.GONE);
                    Btnlogins.setVisibility(View.VISIBLE);
                } else {
                    password_wrapper.setVisibility(View.GONE);
                    Btnsendotp.setVisibility(View.VISIBLE);
                    Btnlogins.setVisibility(View.GONE);
                    continue_btn.setVisibility(View.GONE);
                }
                break;
            case R.id.Btnsendotp:
                if (Utility.isValidMobile(email.getText().toString())) {
                    Loginapi();
                } else {
                    email.setEnabled(true);
                    chkvalidation();
                }
                break;
            case R.id.Btnlogins:
                if (is_mobile) {
                    if (password.getText().toString().equalsIgnoreCase(otp_code)) {
                        Utility.set_login(Login.this, true);
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Utility.show_toast(Login.this,getString(R.string.wron_otp));
                    }
                } else {
                    if (Utility.isValidMail(email.getText().toString()) && !Utility.is_empty(password.getText().toString()))
                        Loginapi();
                    else
                        Utility.show_toast(Login.this, getString(R.string.wrong_detail));
                }
                break;
        }
    }

    private boolean chkvalidation() {
        if (Utility.isValidMobile(email.getText().toString())) {

        } else if (Utility.isValidMail(email.getText().toString())) {

        } else {
            firstnameWrapper.startAnimation(Utility.animationeffect(this));
            return false;
        }
        return true;
    }

    @SuppressLint("NewApi")
    public void Loginapi() {
        mainprogress.setVisibility(View.VISIBLE);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JsonObject object = new JsonObject();
        object.addProperty("email", email.getText().toString());
        object.addProperty("password", password.getText().toString());
        if(is_mobile)
        {
            object.addProperty("mobile", email.getText().toString());
        }
        else
        {
            object.addProperty("mobile", "");

        }


        email.setEnabled(false);
        password.setEnabled(false);
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
        final Request request = new Request.Builder()
                .addHeader("custom-security", Utility.Custom_Security)
                .url(Apiconfig.url + Apiconfig.login)
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        mainprogress.setVisibility(View.GONE);
                        Log.d("responsefailure", e.toString());
                        email.setEnabled(true);
                        password.setEnabled(true);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) throws
                    IOException {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            mainprogress.setVisibility(View.GONE);
                            JSONObject object = new JSONObject(response.body().string());
                            if (object.getString("success").equalsIgnoreCase("1") && object.getString("is_registered").equalsIgnoreCase("1")) {
                                Utility.user_db(Login.this, object.toString());
                                if(is_mobile) {
                                    if (Utility.isValidMobile(email.getText().toString())) {
                                        email.setEnabled(false);
                                        otp_code = String.format("%04d", random.nextInt(1000));
                                        String message = otp_code + " is your OTP for Quikzon login. OTP is valid for 10 minutes. Please do not share it with any one.";
                                        SendSms.send_sms(email.getText().toString(), message);
                                        password_wrapper.setVisibility(View.VISIBLE);
                                        password.setHint("OTP");
                                        password.setEnabled(true);
                                        Btnsendotp.setVisibility(View.GONE);
                                        continue_btn.setVisibility(View.GONE);
                                        Btnlogins.setVisibility(View.VISIBLE);
                                    }
                                }
                                else
                                {
                                    Utility.set_login(Login.this, true);
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }

                            } else if (object.getString("success").equalsIgnoreCase("0") && object.getString("is_registered").equalsIgnoreCase("0")) {

                                Intent i = new Intent(Login.this, Register.class);
                                if (is_mobile) {
                                    i.putExtra("mobile", email.getText().toString());
                                    i.putExtra("email", "");
                                } else {
                                    i.putExtra("mobile", "");
                                    i.putExtra("email", email.getText().toString());
                                }
                                startActivity(i);
                            } else {
                                email.setEnabled(true);
                                password.setEnabled(true);
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
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
