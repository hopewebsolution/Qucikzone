package com.quikzon.add.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.quikzon.add.Homeactivity;
import com.quikzon.add.R;
import com.quikzon.add.restapi.Apiconfig;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity implements View.OnClickListener {
   /* @BindView(R.id.mainprogress)
    ProgressBar mainprogress;*/
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confrimpass)
    EditText confrimpass;
    @BindView(R.id.Btnregister)
    Button Btnregisterl;
    @BindView(R.id.mainprogress)
    ProgressBar mainprogress;
    @BindView(R.id.bussiness)
    Spinner bussiness;
    @BindView(R.id.ll_type)
    LinearLayout ll_type;
    LinkedHashMap<String,String> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setTitle(R.string.register);
        ButterKnife.bind(this);
        init();
    }
    private void init() {
        if(getIntent()!=null)
        {
            mobile.setText(getIntent().getStringExtra("mobile"));
            email.setText(getIntent().getStringExtra("email"));
        }
        ll_type.setOnClickListener(this);
        Btnregisterl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Btnregister:
                values=new LinkedHashMap<>();
                values.put(Utility.type_name,name.getText().toString());
                values.put(Utility.type_email,email.getText().toString());
                values.put(Utility.type_mobile,mobile.getText().toString());
                values.put(Utility.type_password,password.getText().toString());
                values.put(Utility.type_confirm_password,confrimpass.getText().toString());
                values.put(Utility.type_match,password.getText().toString()+"match"+confrimpass.getText().toString());
                if(Utility.is_valid(this,values) && Utility.is_online(this))
                {
                    if (bussiness.getSelectedItemPosition() != 0)
                        Registerapi();
                    else
                        Utility.show_toast(this,getString(R.string.bussines_type));
                }
                break;
            case android.R.id.home:
                 onBackPressed();
                break;
            case R.id.ll_type:
                bussiness.performClick();
                break;
        }
    }

    @SuppressLint("NewApi")
    public void Registerapi() {
        mainprogress.setVisibility(View.VISIBLE);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JsonObject object = new JsonObject();
        object.addProperty("name", name.getText().toString());
        object.addProperty("email", email.getText().toString());
        object.addProperty("phone", mobile.getText().toString());
        object.addProperty("_sb_user_type", bussiness.getSelectedItem().toString());
        object.addProperty("password", password.getText().toString());
        email.setEnabled(false);
        password.setEnabled(false);
        mobile.setEnabled(false);
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestBody body = RequestBody.create(JSON, String.valueOf(object));
        final Request request = new Request.Builder()
                .addHeader("custom-security", Utility.Custom_Security)
                .url(Apiconfig.url+Apiconfig.register)
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        mainprogress.setVisibility(View.GONE);
                        email.setEnabled(true);
                        password.setEnabled(true);
                        mobile.setEnabled(true);
                        Log.d("responsefailure",e.toString());
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
                            JSONObject object=new JSONObject(response.body().string());
                            if(object.getBoolean("success"))
                            {
                                Utility.user_db(Register.this,object.toString());
                                Utility.set_login(Register.this,true);
                                Intent returnIntent = new Intent(Register.this,Homeactivity.class);
                                startActivity(returnIntent);
                                finishAffinity();
                            }
                            else {
                                email.setEnabled(true);
                                password.setEnabled(true);
                                mobile.setEnabled(true);
                                Utility.show_toast(Register.this,object.getString("message"));
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
}
