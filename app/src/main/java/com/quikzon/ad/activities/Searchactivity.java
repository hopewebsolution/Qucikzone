package com.quikzon.ad.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.quikzon.ad.R;
import com.quikzon.ad.adapters.VIew_all_adapter;
import com.quikzon.ad.model.Product_attrubuts;
import com.quikzon.ad.restapi.Apiconfig;
import com.quikzon.ad.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Searchactivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.Ivback)
    ImageView Ivback;
    @BindView(R.id.Ivvoice)
    ImageView Ivvoice;
    @BindView(R.id.Tvsearch)
    EditText Tvsearch;
    private static final int REQUEST_CODE = 1234;
    String Query = "";
    ArrayList<Product_attrubuts> product_attrubuts = new ArrayList<>();
    VIew_all_adapter vIew_all_adapter;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchactivity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Ivback.setOnClickListener(this);
        Ivvoice.setOnClickListener(this);
        Tvsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    callsearchApi(Tvsearch.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Ivback:
                onBackPressed();
                break;
            case R.id.Ivvoice:
                voiceSearch();
                break;
        }
    }

    public void voiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
// Populate the wordsList with the String values the recognition engine thought it heard
            final ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!matches.isEmpty()) {
                Query = matches.get(0);
                Log.d("stirng", Query);
                Tvsearch.setText(Query);
                callsearchApi(Query);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void callsearchApi(String query) {
        LinkedHashMap<String, String> login_detail = new LinkedHashMap<>();
        login_detail.put("search_string", query);
        login_detail.put("city_name", Utility.getcity(this));
        Request result = Utility.post(login_detail, Apiconfig.add_post);
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(result).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }
            @Override
            public void onResponse(@NonNull Call call, final @NonNull Response response) throws
                    IOException {

                runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Log.d("string",new Gson().toJson(response));
                                JSONObject obj = new JSONObject(response.body().string());
                                String data = obj.getJSONObject("data").getJSONArray("ads").toString();
                                Log.d("atrer ", String.valueOf(obj));
                                Intent intent = new Intent(Searchactivity.this,Viewall.class);
                                intent.putExtra("sub_Cat",data);
                                startActivity(intent);
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
