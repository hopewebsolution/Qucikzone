package com.quikzon.add.activities;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.security.NetworkSecurityPolicy;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.gson.JsonObject;
import com.quikzon.add.Homeactivity;
import com.quikzon.add.R;
import com.quikzon.add.restapi.Apiconfig;
import com.quikzon.add.utility.SendSms;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login_chk extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.firstnameWrapper)
    TextInputLayout firstnameWrapper;
    @BindView(R.id.password_wrapper)
    TextInputLayout password_wrapper;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.Ivback)
    ImageView Ivback;
    @BindView(R.id.Btnlogins)
    Button Btnlogins;
    @BindView(R.id.Btnsendotp)
    Button Btnsendotp;
    @BindView(R.id.mainprogress)
    ProgressBar mainprogress;
    @BindView(R.id.Chk)
    CheckBox Chk;
    String userdata ="";
    String otp_code;
    boolean is_mobile = false;
    private Random random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_chk);
        ButterKnife.bind(this);
        init();
    }
    private void init() {
        getSupportActionBar().hide();
        random = new Random();
        if (getIntent()!=null){
            userdata = getIntent().getStringExtra("data");
            email.setText(userdata);
            if (Utility.isValidMobile(email.getText().toString())) {
                password_wrapper.setVisibility(View.GONE);
                Btnlogins.setVisibility(View.GONE);
                Chk.setVisibility(View.VISIBLE);
                Chk.setOnClickListener(this);
                Btnsendotp.setVisibility(View.VISIBLE);
            } else if (Utility.isValidMail(email.getText().toString())) {

            }
            Btnlogins.setOnClickListener(this);
            Btnsendotp.setOnClickListener(this);
            Ivback.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
/*        boolean checked = ((CheckBox) v).isChecked();*/
        switch (v.getId()){
            case R.id.Btnlogins:
                if (is_mobile){
                    if (password.getText().toString().equalsIgnoreCase(otp_code)){
                        Utility.set_login(Login_chk.this,true);
                        Intent intent = new Intent(Login_chk.this,Homeactivity.class);
                        startActivity(intent);
                    }else{
                        Utility.show_toast(Login_chk.this,getString(R.string.wron_otp));
                    }
                }else{
                    if ((Utility.isValidMail(email.getText().toString()) || Utility.isValidMobile(email.getText().toString())) && !Utility.is_empty(password.getText().toString())) {
                        Loginapi();
                    }
                    else {
                        Log.d("not","Correct");
                        if (is_mobile)
                        Utility.show_toast(this, getString(R.string.wrong_detail_mobile));
                        else
                            Utility.show_toast(this, getString(R.string.wrong_detail));
                        /*
                        Toast.makeText(this,"not correct details",Toast.LENGTH_LONG).show();*/
                        /*
                        Utility.show_toast(Login_chk.this, getString(R.string.wrong_detail));*/
                    }
                    }
                break;
            case R.id.Chk:
                onCheckboxClicked(v);
                break;
            case R.id.Btnsendotp:
                if (Utility.isValidMobile(email.getText().toString())){
                    mainprogress.setVisibility(View.VISIBLE);
                    firstnameWrapper.setVisibility(View.GONE);
                    password_wrapper.setVisibility(View.VISIBLE);
                    Btnsendotp.setVisibility(View.GONE);
                    password_wrapper.setHint("Enter OTP");
                    otp_code = String.format("%04d", random.nextInt(1000));
                    String message = otp_code + " is your OTP for Quikzon login. OTP is valid for 10 minutes. Please do not share it with any one.";
                    SendSms.send_sms(email.getText().toString(), message);
                    is_mobile = true;
                    mainprogress.setVisibility(View.GONE);
                    Btnlogins.setVisibility(View.VISIBLE);
                    Chk.setVisibility(View.GONE);
                }else{
                    firstnameWrapper.startAnimation(Utility.animationeffect(this));
                    Toast.makeText(this,"Your mobile number is wrong",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.Ivback:
                onBackPressed();
                break;
        }
    }
    @SuppressLint("NewApi")
    public void Loginapi() {
        mainprogress.setVisibility(View.VISIBLE);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JsonObject object = new JsonObject();
        boolean digitsOnly = TextUtils.isDigitsOnly(email.getText().toString());
        if (digitsOnly){
            Log.d("only","mobile");
            object.addProperty("mobile", email.getText().toString());
            object.addProperty("password", password.getText().toString());
            /*object.addProperty("mobile","");*/
        }else{
            Log.d("only","email");
            object.addProperty("email", email.getText().toString());
            object.addProperty("password", password.getText().toString());
            object.addProperty("mobile","");
        }
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("all_data",String.valueOf(object));
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
                                Utility.user_db(Login_chk.this, object.toString());
                                    Utility.set_login(Login_chk.this, true);
                                    Intent returnIntent = new Intent(Login_chk.this, Homeactivity.class);
                                    startActivity(returnIntent);
                                    finishAffinity();
                                }else{
                                Toast.makeText(Login_chk.this, object.getString("message"), Toast.LENGTH_LONG).show();
                             }
                            } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    // setONchachange listner
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.Chk:
            if (checked){
                password_wrapper.setVisibility(View.VISIBLE);
                Btnlogins.setVisibility(View.VISIBLE);
                Btnsendotp.setVisibility(View.GONE);
            }else{
                password_wrapper.setVisibility(View.GONE);
                Btnsendotp.setVisibility(View.VISIBLE);
                Btnlogins.setVisibility(View.GONE);
            }
        }
    }
}
