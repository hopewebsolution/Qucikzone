package com.quikzon.add.utility;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quikzon.add.Homeactivity;
import com.quikzon.add.R;
import com.quikzon.add.model.Account_model;
import com.quikzon.add.model.ChatMessage;
import com.quikzon.add.model.Chat_list;
import com.quikzon.add.model.Group_chat;
import com.quikzon.add.model.Super_cat;
import com.quikzon.add.restapi.Apiconfig;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    static Context contexts;
    //db variables
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static Socket socket;
    private static SharedPreferences local_db;
    private static SharedPreferences.Editor tbl_user;
    public static Gson gson = new Gson();
    public static String Custom_Security = "hT$e?~nsemPxCE{-UrhT$e?~nsemPxCE{-Ur";
    private static String Purchase_code = "ba127b0b-d3b8-47ac-ab0f-edcb26e3ef09";
    // for registration user
    public static String type_name = "name";
    public static String type_email = "email";
    public static String type_mobile = "mobile";
    public static String type_password = "pass";
    public static String type_confirm_password = "con_pass";
    public static String type_match = "match";
    public static int login=1;
    // for user details
    public static String Profile_data="Profile_data";
    public Utility(Context context) {
        this.contexts = context;
    }
    //to call okkhtpp
    public static Request post(HashMap<String, String> keys, String api_name) {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FormBody.Builder body = new FormBody.Builder();
        for (Object key : keys.keySet()) {
            String value = (String) keys.get(key);
            body.add(key.toString(), value);
        }
        RequestBody parmetrs = body.build();
        Request request = new Request.Builder()
                .addHeader("custom-security", Custom_Security)
                .url(Apiconfig.url+api_name)
                .post(parmetrs)
                .build();
        return request;
    }
/*    public static Request get(String api_name){
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Request request = new Request.Builder()
                .url(Apiconfig.url+api_name)
                .get()
                .build();
        Log.d("urtl",Apiconfig.url+api_name);
        return request;
    }*/
    // to show  full  screen activity
    public static void full_screen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    //to show progress
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Dialog show_progress(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_progress);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        return dialog;
    }
    //to dismis dilog
    public static void dismiss_dilog(Activity activity, Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    //to show toast
    public static void show_toast(Activity activity, String message) {
        View view=activity.getCurrentFocus();
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //to check text is empty or not
    public static boolean is_empty(String value) {
        return TextUtils.isEmpty(value);
    }

    //to check is  network is available or not
    public static boolean is_online(Activity activity) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            //we are connected to a network
            connected = true;
        else
            connected = false;
        return connected;
    }

    //to set image in image view from url

    public static void Set_image(String url,ImageView img)
    {
        Picasso.get().load(url).error(R.drawable.automobile).into(img);
    }
    public static void save_home(Activity activity,String values) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.home_data), Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString(activity.getString(R.string.data), values);
        tbl_user.apply();
    }

    //to get user info
    public static String get_home(Activity activity) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.home_data), Context.MODE_PRIVATE);
        return local_db.getString(activity.getString(R.string.data), "");
    }



    // to save user remeber detail

    public static void clear_home(Activity activity) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.home_data), Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.clear();
        tbl_user.apply();
    }
    // for animation shaker
    public static Animation animationeffect(Activity activity){
        Animation shake;
        return  shake = AnimationUtils.loadAnimation(activity, R.anim.shake);
    }/*
    //for chk validation
    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
        android.util.Patterns.PHONE.matcher(input).matches();
    }*/
    public static boolean isValidMobile(String phone) {
        String regEx = "^[0-9]{10,10}$";
        return phone.matches(regEx);
    }
    public static boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    // display location
    public static void displayLocationSettingsRequest(final Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
    // current city name
    public static String get_city(Activity activity,Double lat,Double lng)
    {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            return addresses.get(0).getLocality();

        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    //
    public static String getcity(Activity activity) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.city), Context.MODE_PRIVATE);
        return local_db.getString("city", "");
    }

    public static String getcity_id(Activity activity) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.city), Context.MODE_PRIVATE);
        return local_db.getString("city_id", "");
    }

    public static  void setcity(Activity activity,String value,String value2) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.city), Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString("city", value);
        tbl_user.putString("city_id", value2);
        tbl_user.commit();
    }
    //to check to value is  equal
    public static boolean is_equal(String value1, String value2) {
        boolean is_valid = false;
        if (value1.equals(value2)) {
            is_valid = true;
        }
        return is_valid;
    }
    // to check user validations
    public static boolean is_valid(Activity activity, LinkedHashMap<String, String> values) {
        boolean is_validate = false;
        for (Object key : values.keySet()) {
            String value = (String) values.get(key);
            String type = key.toString();
            if (type.equalsIgnoreCase("name")) {
                if (Utility.is_empty(value)) {
                    show_toast(activity,activity.getString(R.string.empty_name));
                    is_validate = false;
                    break;
                } else {
                    is_validate = true;
                }
            }else if (type.equalsIgnoreCase("email")) {
                if (!Utility.isValidMail(value)) {
                    show_toast(activity,activity.getString(R.string.email_valid));
                    is_validate = false;
                    break;
                } else {
                    is_validate = true;
                }
            } else if (type.equalsIgnoreCase("mobile")) {
                if (Utility.is_empty(value)) {
                    show_toast(activity,activity.getString(R.string.valid_mobile));
                    is_validate = false;
                    break;
                } else if (!Utility.isValidMobile(value)){
                    show_toast(activity,activity.getString(R.string.valida_mobile));
                    break;
                }else{
                    is_validate = true;
                }
            } else if (type.equalsIgnoreCase("pass")) {
                if (Utility.is_empty(value)) {
                    show_toast(activity,activity.getString(R.string.pass_valid));
                    is_validate = false;
                    break;
                } else {
                    is_validate = true;
                }
            } else if (type.equalsIgnoreCase("con_pass")) {
                if (Utility.is_empty(value)) {
                    show_toast(activity,activity.getString(R.string.con_pass_valid));
                    is_validate = false;
                    break;
                } else {
                    is_validate = true;
                }
            } else if (type.equalsIgnoreCase("match")) {
                StringTokenizer stringTokenizer = new StringTokenizer(value, "match");
                String pass = stringTokenizer.nextToken();
                String confirm = stringTokenizer.nextToken();
                if (Utility.is_empty(value) || !Utility.is_equal(pass, confirm)) {
                    show_toast(activity,activity.getString(R.string.not_match));
                    is_validate = false;
                    break;
                } else {
                    is_validate = true;
                }
            }
        }
        return is_validate;
    }
    // to save user  detail
    public static void user_db(Activity activity, String user_info) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.user_db), Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString(Profile_data,user_info);
        tbl_user.apply();
    }


    public static String single_detail(Activity activity, String key) throws JSONException {
        String value = "";
        JSONObject object = new JSONObject(Utility.get_detail(activity));
        JSONArray profile = object.getJSONObject("data").getJSONArray("profile_data");
        ArrayList<Account_model> user_detail = Utility.gson.fromJson(profile.toString(), new TypeToken<List<Account_model>>() {
        }.getType());
        for (int i = 0; i < user_detail.size(); i++) {
            if (user_detail.get(i).getKey().equalsIgnoreCase(key)) {
                value = user_detail.get(i).getValue();
                break;
            }
        }
        return value;
    }


    public static String single(Activity activity, String key) throws JSONException {
        JSONObject object = new JSONObject(Utility.get_detail(activity));
        return object.getJSONObject("data").getString(key);
    }

    public static ArrayList<Account_model> get_account_detail(Activity activity) throws JSONException {
        JSONObject object = new JSONObject(Utility.get_detail(activity));
        JSONArray profile = object.getJSONObject("data").getJSONArray("profile_data");
        return Utility.gson.fromJson(profile.toString(), new TypeToken<List<Account_model>>() {
        }.getType());
    }

    //to get user info details
    public static String get_detail(Activity activity){
        local_db = activity.getSharedPreferences(activity.getString(R.string.user_db), Context.MODE_PRIVATE);
        String user_detail = local_db.getString(Profile_data, "");
        return user_detail;
    }
    //to clear user  db
    public static void clear_db(Activity activity) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.user_db), Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.clear();
        tbl_user.apply();
    }

    //to set user is login
    public static void set_login(Activity activity, boolean value) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.login_db), Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putBoolean(activity.getString(R.string.is_login), value);
        tbl_user.apply();
    }

    // to check is  user login  or not
    public static boolean get_login(Activity activity) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.login_db), Context.MODE_PRIVATE);
        return local_db.getBoolean(activity.getString(R.string.is_login), false);
    }
    // to call logout
    public  static void Logout(Activity activity){
        set_login(activity,false);
        clear_db(activity);
        activity.startActivity(new Intent(activity, Homeactivity.class));
        activity.finishAffinity();
    }
    //to set user is login
    public static void set_chat_user(Activity activity, String chats) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.chat_user), Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        tbl_user.putString("chat_list",chats);
        tbl_user.apply();
    }

    public static boolean get_chat_user_by_room(Activity activity,String room_id) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.chat_user), Context.MODE_PRIVATE);
       ArrayList<Chat_list> lists= Utility.gson.fromJson(local_db.getString("chat_list",""), new TypeToken<List<Chat_list>>() {
        }.getType());
        if (lists != null) {
            for (Chat_list chat_list : lists) {
                if (chat_list.getRoom_id().equalsIgnoreCase(room_id)) {
                    return true;
                }
            }
        }
       return false;
    }




    public static ArrayList<Chat_list> get_chat_user(Activity activity) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.chat_user), Context.MODE_PRIVATE);
        return Utility.gson.fromJson(local_db.getString("chat_list",""), new TypeToken<List<Chat_list>>() {
        }.getType());
    }



    public static Socket join_chat(Activity activity,String name)
    {
        //connect you socket client to the server
        try {
            socket = IO.socket("http://hopewebsolution.com:3000");
            socket.connect();
            socket.emit("join", name);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return socket;
    }

    public static void save_chat(Activity activity, String sen,String auhot,String ad_id, String chat) {
        local_db = activity.getSharedPreferences(activity.getString(R.string.chat_user), Context.MODE_PRIVATE);
        tbl_user = local_db.edit();
        ArrayList<Group_chat> all_chats = Utility.gson.fromJson(local_db.getString("chat", ""), new TypeToken<List<Group_chat>>() {
        }.getType());

        if(all_chats!=null) {
            boolean is_chat = false;
            int pos = 0;
            for (Group_chat chatMessage : all_chats) {
                if (chatMessage!=null&&chatMessage.getSender_id().equalsIgnoreCase(sen)&&chatMessage.getAuthor_id().equalsIgnoreCase(auhot)&&chatMessage.getAd_id().equalsIgnoreCase(ad_id)) {
                    is_chat = true;
                    break;
                }
                pos++;
            }
            Group_chat group_chat=new Group_chat();
            group_chat.setAd_id(ad_id);
            group_chat.setSender_id(sen);
            group_chat.setAuthor_id(auhot);
            group_chat.setChats(chat);
            if (is_chat) {
                all_chats.set(pos, group_chat);
            } else {

                all_chats.add(group_chat);

            }
        }
        else {
            Group_chat group_chat=new Group_chat();
            group_chat.setAd_id(ad_id);
            group_chat.setSender_id(sen);
            group_chat.setAuthor_id(auhot);
            group_chat.setChats(chat);
            all_chats=new ArrayList<>();
            all_chats.add(group_chat);
        }
        tbl_user.putString("chat", Utility.gson.toJson(all_chats));
        tbl_user.apply();
    }

    public static ArrayList<ChatMessage> get_chat(Activity activity,String sen,String aut, String ad_id) {
        ArrayList<ChatMessage> current_ad_chat = new ArrayList<>();
        local_db = activity.getSharedPreferences(activity.getString(R.string.chat_user), Context.MODE_PRIVATE);
        ArrayList<Group_chat> all_chats = Utility.gson.fromJson(local_db.getString("chat", ""), new TypeToken<List<Group_chat>>() {
        }.getType());
        if(all_chats!=null) {
            int i=0;
            for (Group_chat chatMessage : all_chats) {
                if (chatMessage!=null&&chatMessage.getSender_id().equalsIgnoreCase(sen)&&chatMessage.getAuthor_id().equalsIgnoreCase(aut)&&chatMessage.getAd_id().equalsIgnoreCase(ad_id)) {
                    current_ad_chat= Utility.gson.fromJson(chatMessage.getChats(), new TypeToken<List<ChatMessage>>() {}.getType());
                    break;
                }
            }
        }
        return current_ad_chat;
    }



}
