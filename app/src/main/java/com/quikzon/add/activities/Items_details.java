package com.quikzon.add.activities;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quikzon.add.Homeactivity;
import com.quikzon.add.R;
import com.quikzon.add.adapters.Homeparentcategoires;
import com.quikzon.add.adapters.Item_description;
import com.quikzon.add.adapters.Sub_catgory;
import com.quikzon.add.adapters.VIew_all_adapter;
import com.quikzon.add.helper.EqualSpacingItemDecoration;
import com.quikzon.add.model.Chat_list;
import com.quikzon.add.model.Item_descritiption;
import com.quikzon.add.model.News_blog;
import com.quikzon.add.model.Product_attrubuts;
import com.quikzon.add.model.Typing;
import com.quikzon.add.model.item_des_key;
import com.quikzon.add.restapi.Apiconfig;
import com.quikzon.add.restapi.User_details;
import com.quikzon.add.utility.Utility;

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
    String author_type= "";
    String ad_title= "";
    String room_id="";
    Sub_catgory sub_catgory;
    ArrayList<Product_attrubuts> smiler_ads=new ArrayList<>();
    /*   TextView no_ads;*/
    Item_descritiption item_descritiption; //Model class
    Item_description item_description;    // Item adapter
    ArrayList<Item_descritiption> arrayList;
    ArrayList<item_des_key> list = new ArrayList<>();
    ;
    Gson gson;
    String all_imgs="";
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
        if (getIntent() != null) {
            ad_id = getIntent().getStringExtra("ad_id");
            singledetailsapi(ad_id, userid);
        }
        Llprofiledata.setOnClickListener(this);
        Llcallnow.setOnClickListener(this);
        layoutTop.setOnClickListener(this);
        Llchat.setOnClickListener(this);

    }

    private void singledetailsapi(String ad_id, String userid) {
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
                        Log.d("exce[", e.toString());
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
                            author_id=add_details.getString("ad_author_id");
                            Tvdescrip.setText(add_details.getString("ad_title"));
                            ad_title=add_details.getString("ad_title");
                            Tvdescr.setText(add_details.getString("ad_desc"));
                            Tvprice.setText(add_details.getString("ad_price"));
                            Tvcondition.setText(add_details.getString("ad_condition"));
                            Tvtimeloc.setText("By " + add_details.getString("poster_name") + " " + add_details.getString("ad_date"));
                            Utility.Set_image(add_details.getJSONArray("ad_images").getJSONObject(0).getString("full"), layoutTop);
                            all_imgs=add_details.getJSONArray("ad_images").toString();
                            username.setText(add_details.getString("poster_name"));
                            author_name=add_details.getString("poster_name");
                            author_type=add_details.getString("author_type");
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
                            sub_catgory = new Sub_catgory(Items_details.this,smiler_ads);
                            smiller_ads.addItemDecoration(new EqualSpacingItemDecoration(10));
                            smiller_ads.setAdapter(sub_catgory);
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
            case R.id.Llprofiledata:
                Intent intent = new Intent(this, Account_ad_details.class);
                intent.putExtra("userid", author_id);
                intent.putExtra("author_name", author_name);
                intent.putExtra("author_type", author_type);
                startActivity(intent);
                break;
            case R.id.Llcallnow:
                if (phone.equalsIgnoreCase("")) {
                    Toast.makeText(this, "User have no phone number", Toast.LENGTH_LONG).show();
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(phone));//change the number
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                }
                break;
            case R.id.layoutTop:
                intent = new Intent(this, Full_screen_img.class);
                intent.putExtra("imgs",all_imgs);
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
        Homeactivity homeactivity=new Homeactivity();
        ArrayList<Chat_list> user=Utility.get_chat_user(this);
        if(user!=null)
        {
            boolean is_already=false;
            for (int i = 0; i < user.size(); i++) {
                if (user.get(i).getAd_id().equalsIgnoreCase(ad_id) && user.get(i).getAuthor_id().equalsIgnoreCase(author_id)) {
                    is_already = true;
                    break;
                }

            }
            if(!is_already)
            {
                Chat_list chat_list=new Chat_list();
                chat_list.setAd_id(ad_id);
                chat_list.setAuthor_id(author_id);
                chat_list.setAuthor_name(author_name);
                chat_list.setAd_title(ad_title);
                chat_list.setRoom_id(String.valueOf(userid+author_id+ad_id));
                user.add(chat_list);
                Utility.set_chat_user(this,gson.toJson(user));

            }
            Intent intent=new Intent(this,Messagner.class);
            intent.putExtra("author_id",author_id);
            intent.putExtra("ad_id",ad_id);
            intent.putExtra("author_name",author_name);
            intent.putExtra("ad_title",ad_title);
            intent.putExtra("room_id",String.valueOf(userid+author_id+ad_id));
            startActivity(intent);


        }
        else
        {
            user=new ArrayList<>();
            Chat_list chat_list=new Chat_list();
            chat_list.setAd_id(ad_id);
            chat_list.setAuthor_id(author_id);
            chat_list.setAuthor_name(author_name);
            chat_list.setAd_title(ad_title);
            chat_list.setRoom_id(String.valueOf(userid+author_id+ad_id));
            user.add(chat_list);
            Utility.set_chat_user(this,gson.toJson(user));

            Intent intent=new Intent(this,Messagner.class);
            intent.putExtra("author_id",author_id);
            intent.putExtra("ad_id",ad_id);
            intent.putExtra("author_name",author_name);
            intent.putExtra("ad_title",ad_title);
            intent.putExtra("room_id",String.valueOf(userid+author_id+ad_id));
            startActivity(intent);
        }

    }

}