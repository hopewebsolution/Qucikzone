package com.quikzon.add.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.gson.Gson;
import com.quikzon.add.R;
import com.quikzon.add.adapters.Profile_adapter;
import com.quikzon.add.helper.NonScrollListview;
import com.quikzon.add.model.Account_model;
import com.quikzon.add.model.Ad_post_view;
import com.quikzon.add.restapi.Apiconfig;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

public class Myaccount extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.Lvlist)
    NonScrollListview Lvlist;
    @BindView(R.id.Btnupdate)
    Button Btnupdate;
    @BindView(R.id.Ivprofile)
    ImageView Ivprofile;
    @BindView(R.id.Ivback)
    ImageView Ivback;
    ArrayList<Account_model> view = new ArrayList<>();
    ArrayList<String> returnValue = new ArrayList<>();
    boolean is_all_set = false;
    Dialog register_progress;
    String all_form_data="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        init();
    }
    private void init() {
        try {
            Lvlist.setAdapter(new Profile_adapter(this, Utility.get_account_detail(this)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Ivprofile.setOnClickListener(this);
        Ivback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Btnupdate:
                validation();
                break;
//            case R.id.Ivprofile:
//                Pix.start(this,1,1);
//                break;
            case R.id.Ivback:
                onBackPressed();
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, 1,8);
                }
                return;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            returnValue.clear();
            returnValue.addAll(data.getStringArrayListExtra(Pix.IMAGE_RESULTS));
            Log.d("return",returnValue.get(0));
            Ivprofile.setImageURI(Uri.parse(returnValue.get(0)));
        }
    }
    private void validation() {
        for (int i = 0; i < view.size(); i++) {
            if (TextUtils.isEmpty(view.get(i).getValue())) {
                Toast.makeText(getApplicationContext(), view.get(i).getKey() + " is required", Toast.LENGTH_LONG).show();
                is_all_set = false;
                break;
            } else {
                is_all_set = true;
            }
        }
        if (Utility.is_online(this) && is_all_set){
            Gson gson = new Gson();
            all_form_data = gson.toJson(view);
            Log.d("all_form_data",all_form_data);
        //    postaddapi();
        }
    }
    @SuppressLint("NewApi")
    public void postaddapi() {
        register_progress = Utility.show_progress(this);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("all_form_data",all_form_data);

        /*        builder.addFormDataPart("title", title_t);*/
        // Map is used to multipart the file using okhttp3.RequestBody
        // Multiple Images
        if (returnValue != null ) {
                File file = null;
                file = new File(returnValue.get(0));
                builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
        }
        MultipartBody requestBody = builder.build();
        final Request request = new Request.Builder()
                .addHeader("custom-security", Utility.Custom_Security)
                .url(Apiconfig.url+Apiconfig.post_add)
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("response",e.toString());
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) throws
                    IOException {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("response",response.body().toString());
                    }
                });
            }

        });
    }
}
