package com.quikzon.add.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.quikzon.add.R;
import com.quikzon.add.adapters.Img_add_adpater;
import com.quikzon.add.adapters.Post_add_adpater;
import com.quikzon.add.fragements.Addpost_fragement;
import com.quikzon.add.model.Ad_post_view;
import com.quikzon.add.model.Adimg_model;
import com.quikzon.add.model.News_blog;
import com.quikzon.add.model.Post_imgs;
import com.quikzon.add.model.Post_values;
import com.quikzon.add.restapi.Apiconfig;
import com.quikzon.add.restapi.User_details;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Post_add extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.rel_main_header)
    RelativeLayout rel_main_header;
    @BindView(R.id.rel_extra_header)
    LinearLayout rel_extra_header;
    @BindView(R.id.left_icon)
    ImageView left_icon;
    @BindView(R.id.tite)
    EditText tite;
    @BindView(R.id.desc)
    EditText desc;
    /*    @BindView(R.id.center_icon)
        TextView center_icon;
        @BindView(R.id.right_icon)
        TextView right_icon;*/
    @BindView(R.id.Rvadddesc)
    RecyclerView Rvadddesc;
    @BindView(R.id.Tvmaincat)
    TextView Tvmaincat;
    @BindView(R.id.Tvsubcat)
    TextView Tvsubcat;
    @BindView(R.id.ll_catgorys)
    LinearLayout ll_catgorys;
    @BindView(R.id.btn_post)
    Button btn_post;
    @BindView(R.id.Ivimgsrc)
    ImageView Ivimgsrc;
    @BindView(R.id.Rvimgall)
    RecyclerView Rvimgall;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.address_edt)
    EditText address_edt;
    Post_add_adpater post_add_adapter;
    Img_add_adpater img_add_adapter;
    ArrayList<Ad_post_view> view = new ArrayList<>();
    ArrayList<Post_imgs> returnValue = new ArrayList<>();
    boolean is_all_set = false;
    Dialog register_progress;
    String all_form_data = "";
    String addId = "";
    ArrayList<Adimg_model> edit_img = new ArrayList<>();
    ArrayList<String> remaing_img = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_add);
        ButterKnife.bind(this);
        inti();
    }

    private void inti() {
        Rvimgall.setLayoutManager(new LinearLayoutManager(Post_add.this, LinearLayoutManager.HORIZONTAL, false));
        img_add_adapter = new Img_add_adpater(Post_add.this, Post_add.this, returnValue);
        Rvimgall.setAdapter(img_add_adapter);
        if (getIntent() != null) {
            addId = getIntent().getStringExtra("ad_id");
            tite.setText(getIntent().getStringExtra("title"));
            desc.setText(getIntent().getStringExtra("desc"));
            address_edt.setText(getIntent().getStringExtra("adress"));
            name.setText(getIntent().getStringExtra("name"));
            phone.setText(getIntent().getStringExtra("mobile"));
                edit_img = Utility.gson.fromJson(getIntent().getStringExtra("imgs"), new TypeToken<List<Adimg_model>>() {
                }.getType());
            if (edit_img != null) {

                for (int i = 0; i < edit_img.size(); i++) {
                    Post_imgs imgs = new Post_imgs();
                    imgs.setIs_system(false);
                    imgs.setUrl(edit_img.get(i).getFull());
                    imgs.setUri("");
                    returnValue.add(imgs);
                }
                img_add_adapter.notifyDataSetChanged();
            }
            if (!TextUtils.isEmpty(addId)) {
                btn_post.setText("Edit Post");
            }
        }
/*        center_icon.setText(R.string.post);
        right_icon.setText(R.string.resetall);*/
        Tvsubcat.setText(Addpost_fragement.sub_cat_name);
        Tvmaincat.setText(Addpost_fragement.parent_cat_name);
        ll_catgorys.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        if (TextUtils.isEmpty(addId)) {
            try {
                name.setText(Utility.single(Post_add.this, User_details.display_name));
                phone.setText(Utility.single(Post_add.this, User_details.phone));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //add dey view  in form
        if (Utility.is_online(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getview(Addpost_fragement.sub_cat_id);
            }
        }
        Ivimgsrc.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getview(String cat_id) {
        progress.setVisibility(View.VISIBLE);
        LinkedHashMap<String, String> view_ad = new LinkedHashMap<>();
        view_ad.put("cat_id", cat_id);
        view_ad.put("ad_id", addId);
        Request result = Utility.post(view_ad, Apiconfig.get_view);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        progress.setVisibility(View.GONE);
                        Log.d("error", e.toString());
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            progress.setVisibility(View.GONE);
                            assert response.body() != null;
                            JSONObject obj = new JSONObject(response.body().string());
                            view = Utility.gson.fromJson(obj.getJSONArray("data").toString(), new TypeToken<List<Ad_post_view>>() {
                            }.getType());
                            Rvadddesc.setLayoutManager(new LinearLayoutManager(Post_add.this, LinearLayoutManager.VERTICAL, false));
                            post_add_adapter = new Post_add_adpater(Post_add.this, Post_add.this, view);
                            Rvadddesc.setAdapter(post_add_adapter);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_catgorys:
                onBackPressed();
                break;
            case R.id.btn_post:
                validation();
                break;
            case R.id.Ivimgsrc:
                Pix.start(this, 1, 8);
                break;
        }
    }

    public void update(int pos, String values) {
        view.get(pos).setField_val(values);
    }

    private void validation() {
        for (int i = 0; i < view.size(); i++) {
            if (view.get(i).getIs_required().equalsIgnoreCase("true") && TextUtils.isEmpty(view.get(i).getField_val()) || view.get(i).getField_val().equalsIgnoreCase("Select Option")) {
                Utility.show_toast(Post_add.this, view.get(i).getTitle() + " is required");
                is_all_set = false;
                break;
            } else {
                is_all_set = true;
            }
        }
        if (Utility.is_online(this) && is_all_set) {
            if (!Utility.is_empty(tite.getText().toString()) && !Utility.is_empty(desc.getText().toString()) &&
                    !Utility.is_empty(name.getText().toString()) && !Utility.is_empty(phone.getText().toString()) && !Utility.is_empty(address_edt.getText().toString()))
                if (returnValue.size() > 0) {
                    postaddapi(view, returnValue);
                } else {
                    Utility.show_toast(Post_add.this, getString(R.string.min_add));
                }
            else
                Utility.show_toast(Post_add.this, getString(R.string.required));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(this, 1, 8);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            for (int i = 0; i < data.getStringArrayListExtra(Pix.IMAGE_RESULTS).size(); i++) {
                Post_imgs imgs = new Post_imgs();
                imgs.setIs_system(true);
                imgs.setUrl("");
                imgs.setUri(data.getStringArrayListExtra(Pix.IMAGE_RESULTS).get(i));
                returnValue.add(imgs);
            }

            img_add_adapter.notifyDataSetChanged();

        }
    }

    public void delete_img(int pos) {
        returnValue.remove(pos);
        img_add_adapter.notifyDataSetChanged();
    }

    @SuppressLint("NewApi")
    public void postaddapi(ArrayList<Ad_post_view> view, ArrayList<Post_imgs> imags) {
        progress.setVisibility(View.VISIBLE);

        JSONObject object = null;
        String userid = "";
        try {
            object = new JSONObject(Utility.get_detail(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject profile_details = object.getJSONObject("data");
            userid = profile_details.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();

        if (imags != null && imags.size() > 0) {
            for (int i = 0; i < imags.size(); i++) {
                if (imags.get(i).isIs_system()) {
                    File file = new File(imags.get(i).getUri());
                    try {
                        File now_file = new Compressor(this).compressToFile(file);
                        builder.addFormDataPart("file" + i, now_file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), now_file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (int k = 0; k < edit_img.size(); k++) {
                        if (edit_img.get(k).getFull().equalsIgnoreCase(imags.get(i).getUrl())) {
                            remaing_img.add(edit_img.get(k).getImg_id());
                            break;
                        }
                    }

                }

            }
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("ad_title", tite.getText().toString());
        builder.addFormDataPart("ad_description", desc.getText().toString());
        builder.addFormDataPart("ad_cats1", Addpost_fragement.sub_cat_id);
        builder.addFormDataPart("userid", userid);
        builder.addFormDataPart("ad_id", addId);
        builder.addFormDataPart("name", name.getText().toString());
        builder.addFormDataPart("mobile", phone.getText().toString());
        builder.addFormDataPart("address", address_edt.getText().toString());
        builder.addFormDataPart("remian", remaing_img.toString());
        builder.addFormDataPart("custom_fields", gson.toJson(view, new TypeToken<List<Post_values>>() {
        }.getType()));
        MultipartBody body = builder.build();
        final Request request = new Request.Builder()
                .addHeader("custom-security", Utility.Custom_Security)
                .url(Apiconfig.url + Apiconfig.post_add)
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.retryOnConnectionFailure();
        okHttpClient.connectTimeoutMillis();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        progress.setVisibility(View.GONE);
                        if (e.toString().equalsIgnoreCase("java.net.SocketTimeoutException: timeout")) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                        Log.d("response", e.toString());
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                progress.setVisibility(View.GONE);
                                JSONObject object = new JSONObject(response.body().string());
                                Log.d("post", new Gson().toJson(object));
                                Log.d("allpost", String.valueOf(object));
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                                if (object.getBoolean("success")) {
                                    onBackPressed();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

        });
    }

    public void postadimage(ArrayList<String> imgs) {
        progress.setVisibility(View.VISIBLE);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (imgs != null && imgs.size() > 0) {
            for (int i = 0; i < imgs.size(); i++) {
                File file = new File(imgs.get(i));
                builder.addFormDataPart("file" + i, file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
        }
        MultipartBody Body = builder.build();
        final Request request = new Request.Builder()
                .addHeader("custom-security", Utility.Custom_Security)
                .url(Apiconfig.url + Apiconfig.post_ad_image)
                .post(Body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        progress.setVisibility(View.GONE);
                        Log.d("response", e.toString());
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                progress.setVisibility(View.GONE);
                                Log.d("response", response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

        });
    }
}
