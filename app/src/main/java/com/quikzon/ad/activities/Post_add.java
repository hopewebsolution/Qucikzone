package com.quikzon.ad.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.quikzon.ad.R;
import com.quikzon.ad.adapters.Img_add_adpater;
import com.quikzon.ad.adapters.Post_add_adpater;
import com.quikzon.ad.fragements.Addpost_fragement;
import com.quikzon.ad.helper.NonScrollRecyclerview;
import com.quikzon.ad.model.Ad_post_view;
import com.quikzon.ad.model.Adimg_model;
import com.quikzon.ad.model.News_blog;
import com.quikzon.ad.model.Post_imgs;
import com.quikzon.ad.model.Post_values;
import com.quikzon.ad.restapi.Apiconfig;
import com.quikzon.ad.restapi.User_details;
import com.quikzon.ad.utility.PlaceJSONParser;
import com.quikzon.ad.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
    NonScrollRecyclerview Rvadddesc;
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
    AutoCompleteTextView address_edt;
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
    ParserTask parserTask;
    PlacesTask placesTask;
    SimpleAdapter adapter;
    String lat="",lng="";
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
        address_edt.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                lat="";
                lng="";
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        address_edt.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String value=((TextView) arg1).getText().toString();
                address_edt.setText(value);
                lat=String.valueOf(Utility.getLocationFromAddress(Post_add.this,value).getLatitude());
                lng=String.valueOf(Utility.getLocationFromAddress(Post_add.this,value).getLongitude());
            }
        });

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
                if (returnValue.size() > 0&&!Utility.is_empty(lat)&&!Utility.is_empty(lng)) {
                    postaddapi(view, returnValue);
                }
                else  if (TextUtils.isEmpty(lat)||TextUtils.isEmpty(lng))
                {
                    Utility.show_toast(Post_add.this, getString(R.string.invalid));

                }
                else {
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
        builder.addFormDataPart("lat", lat);
        builder.addFormDataPart("lng", lng);
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
    /**
     * A method to download json data from url
     */
    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";
            // Obtain browser key from https://code.google.com/apis/console
            String key= getString(R.string.api_key);
            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=true";

            // Building the parameters to the web service
            String parameters = input + "&" + types +"&"+sensor + "&key=" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {

                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {

                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
             adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);
            address_edt.setAdapter(adapter);

        }
    }
}
