package com.quikzon.ad.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quikzon.ad.R;
import com.quikzon.ad.adapters.Item_description;
import com.quikzon.ad.adapters.Sub_catgory_detail;
import com.quikzon.ad.helper.EqualSpacingItemDecoration;
import com.quikzon.ad.model.Chat_list;
import com.quikzon.ad.model.Item_descritiption;
import com.quikzon.ad.model.Product_attrubuts;
import com.quikzon.ad.model.item_des_key;
import com.quikzon.ad.restapi.Apiconfig;
import com.quikzon.ad.restapi.User_details;
import com.quikzon.ad.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Items_details extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.Listview)
    ListView listView;
    @BindView(R.id.Tvdescrip)
    TextView Tvdescrip;
    @BindView(R.id.Tvtimeloc)
    TextView Tvtimeloc;
    @BindView(R.id.Tvprice)
    TextView Tvprice;
    @BindView(R.id.Tvdescr)
    TextView Tvdescr;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.userdesc)
    TextView userdesc;
    @BindView(R.id.Imguser)
    CircleImageView Imguser;
    @BindView(R.id.progressbars)
    ProgressBar progressbars;
    @BindView(R.id.layoutTop)
    ImageView layoutTop;
    @BindView(R.id.Llprofiledata)
    LinearLayout Llprofiledata;
    @BindView(R.id.Llcallnow)
    LinearLayout Llcallnow;
    @BindView(R.id.Llchat)
    LinearLayout Llchat;
    @BindView(R.id.botttam)
    LinearLayout botttam;
    @BindView(R.id.screoll)
    ScrollView screoll;
    @BindView(R.id.smiller_ads)
    RecyclerView smiller_ads;
    @BindView(R.id.Tvcondition)
    TextView Tvcondition;
    @BindView(R.id.ll_is_fetured)
    LinearLayout ll_is_fetured;
    String userid = "";
    String ad_id = "";
    String phone = "";
    String author_name = "";
    String author_id = "";
    String author_type = "";
    String ad_title = "";
    String room_id = "";
    String author_tocken = "";
    Sub_catgory_detail sub_catgory;
    ArrayList<Product_attrubuts> smiler_ads = new ArrayList<>();
    /*   TextView no_ads;*/
    Item_descritiption item_descritiption; //Model class
    Item_description item_description;    // Item adapter
    ArrayList<Item_descritiption> arrayList;
    ArrayList<item_des_key> list = new ArrayList<>();
    ;
    Gson gson;
    String all_imgs = "";
    FragmentTransaction fragmentTransaction;
    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_details);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        gson = new Gson();
        if (Utility.get_login(this)) {
            try {
                userid = Utility.single(this, User_details.id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (getIntent() != null) {
            ad_id = getIntent().getStringExtra("ad_id");
            singledetailsapi(ad_id, userid);
        }
        Llprofiledata.setOnClickListener(this);
        Llcallnow.setOnClickListener(this);
        layoutTop.setOnClickListener(this);
        Llchat.setOnClickListener(this);

    }

    private void singledetailsapi(String ad_id, final String userid) {
        progressbars.setVisibility(View.VISIBLE);
        LinkedHashMap<String, String> login_detail = new LinkedHashMap<>();
        login_detail.put("userid", userid);
        login_detail.put("ad_id", ad_id);
        Request result = Utility.post(login_detail, Apiconfig.addetails);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /*        no_ads.setVisibility(View.GONE);*/
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) throws
                    IOException {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    public void run() {
                        try {
                            progressbars.setVisibility(View.GONE);
                            screoll.setVisibility(View.VISIBLE);
                            botttam.setVisibility(View.VISIBLE);
                            /*       no_ads.setVisibility(View.GONE);*/
                            JSONObject obj = new JSONObject(response.body().string());
                            JSONObject add_details = obj.getJSONObject("data");
                            author_id = add_details.getString("ad_author_id");
                            Tvdescrip.setText(add_details.getString("ad_title"));
                            ad_title = add_details.getString("ad_title");
                            author_tocken = add_details.getString("auth_tocken");
                            Tvdescr.setText(add_details.getString("ad_desc"));
                            Tvprice.setText(add_details.getString("ad_price"));
                            Tvcondition.setText(add_details.getString("ad_condition"));
                            Tvtimeloc.setText("By " + add_details.getString("poster_name") + " " + add_details.getString("ad_date"));
                            Utility.Set_image(add_details.getJSONArray("ad_images").getJSONObject(0).getString("full"), layoutTop);
                            all_imgs = add_details.getJSONArray("ad_images").toString();
                            username.setText(add_details.getString("poster_name"));
                            author_name = add_details.getString("poster_name");
                            author_type = add_details.getString("author_type");
                            phone = add_details.getString("phone");
                            ll_is_fetured.setVisibility(View.GONE);
                            if (add_details.getBoolean("is_feature")) {
                                ll_is_fetured.setVisibility(View.VISIBLE);
                            }

                            userdesc.setText("Contact with " + add_details.getString("poster_name") + " chat");
                            list = Utility.gson.fromJson(add_details.getJSONArray("fieldsData").toString(), new TypeToken<List<item_des_key>>() {
                            }.getType());
                            if (list.size() > 0) {
                                listView.setAdapter(new Item_description(Items_details.this, list));
                            }
                            smiler_ads = Utility.gson.fromJson(obj.getJSONObject("data").getJSONArray("related_ads").toString(), new TypeToken<List<Product_attrubuts>>() {
                            }.getType());
                            smiller_ads.setLayoutManager(new LinearLayoutManager(Items_details.this, LinearLayoutManager.HORIZONTAL, false));
                            sub_catgory = new Sub_catgory_detail(Items_details.this, smiler_ads);
                            smiller_ads.addItemDecoration(new EqualSpacingItemDecoration(10));
                            smiller_ads.setAdapter(sub_catgory);
                            botttam.setVisibility(View.VISIBLE);
                            if (userid.equalsIgnoreCase(author_id)) {
                                botttam.setVisibility(View.GONE);
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

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Llprofiledata:
                Intent intent = new Intent(this, Account_ad_details.class);
                intent.putExtra("userid", author_id);
                intent.putExtra("author_name", author_name);
                intent.putExtra("author_type", author_type);
                startActivity(intent);
                break;
            case R.id.Llcallnow:
                if (Utility.get_login(this) && !phone.equalsIgnoreCase("")) {
                     intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                } else if (!Utility.get_login(this)) {
                    Intent i = new Intent(this, Login.class);
                    startActivityForResult(i, Utility.login);
                } else {
                    Toast.makeText(getApplicationContext(), "Phone Number Not Found !", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.layoutTop:
                intent = new Intent(this, Full_screen_img.class);
                intent.putExtra("imgs", all_imgs);
                startActivity(intent);
                break;
            case R.id.Llchat:
                if (Utility.get_login(this)) {
                    ad_user(ad_id, author_id, author_name, ad_title);
                } else {
                    Intent i = new Intent(this, Login.class);
                    startActivityForResult(i, Utility.login);
                }
                break;
        }
    }

    private void ad_user(String ad_id, final String author_id, String author_name, String ad_title) {
        ArrayList<Chat_list> user = Utility.get_chat_user(this);
        if (user != null) {
            boolean is_already = false;
            for (int i = 0; i < user.size(); i++) {
                if (user.get(i).getAd_id().equalsIgnoreCase(ad_id) && user.get(i).getAuthor_id().equalsIgnoreCase(author_id)) {
                    is_already = true;
                    break;
                }

            }
            if (!is_already) {
                Chat_list chat_list = new Chat_list();
                chat_list.setAd_id(ad_id);
                chat_list.setAuthor_id(author_id);
                chat_list.setAuthor_name(author_name);
                chat_list.setAd_title(ad_title);
                chat_list.setRoom_id(String.valueOf(userid + author_id + ad_id));
                user.add(chat_list);
                Utility.set_chat_user(this, gson.toJson(user));
                send_push_to_admin(author_id, chat_list);
            }
            else
            {
                Intent intent = new Intent(Items_details.this, Messagner.class);
                intent.putExtra("author_id", author_id);
                intent.putExtra("ad_id", ad_id);
                intent.putExtra("author_name", author_name);
                intent.putExtra("ad_title", ad_title);
                intent.putExtra("room_id", room_id);
                startActivity(intent);
            }


        } else {
            user = new ArrayList<>();
            Chat_list chat_list = new Chat_list();
            chat_list.setAd_id(ad_id);
            chat_list.setAuthor_id(author_id);
            chat_list.setAuthor_name(author_name);
            chat_list.setAd_title(ad_title);
            chat_list.setRoom_id(String.valueOf(userid + author_id + ad_id));
            user.add(chat_list);
            Utility.set_chat_user(this, gson.toJson(user));

            send_push_to_admin(author_id, chat_list);
        }

    }

    public void send_push_to_admin(final String author_id, final Chat_list list) {

        try {
            list.setAuthor_name(Utility.single(this, User_details.display_name));
            list.setAuthor_id(list.getAuthor_id());
            list.setAd_title(list.getAd_title());
            list.setRoom_id(list.getRoom_id());
            list.setAd_id(list.getAd_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressbars.setVisibility(View.VISIBLE);
        LinkedHashMap<String, String> login_detail = new LinkedHashMap<>();
        login_detail.put("author_id", author_id);
        login_detail.put("author_tocken", author_tocken);
        login_detail.put("group_data", gson.toJson(list));
        Log.d("datatat", gson.toJson(list));
        Request result = Utility.post(login_detail, Apiconfig.push_notification);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressbars.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) throws
                    IOException {
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressbars.setVisibility(View.GONE);
                        Intent intent = new Intent(Items_details.this, Messagner.class);
                        intent.putExtra("author_id", author_id);
                        intent.putExtra("ad_id", ad_id);
                        intent.putExtra("author_name", author_name);
                        intent.putExtra("ad_title", ad_title);
                        intent.putExtra("room_id", room_id);
                        startActivity(intent);
                    }
                });
            }
        });
    }

}