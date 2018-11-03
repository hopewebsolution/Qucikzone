package com.quikzon.ad.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.quikzon.ad.R;
import com.quikzon.ad.adapters.Homecategories;
import com.quikzon.ad.adapters.Homeparentcategoires;
import com.quikzon.ad.adapters.Sub_catgory_adpater;
import com.quikzon.ad.model.Products;
import com.quikzon.ad.model.Sub_catgory_model;
import com.quikzon.ad.model.Super_cat;
import com.quikzon.ad.model.Super_sub_Cat;
import com.quikzon.ad.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Sub_catgory extends AppCompatActivity {
    String position="";
    ListView lv;
    Sub_catgory_adpater sub_catgory_adpater;
    EditText serach;
    ArrayList<Super_sub_Cat> list=new ArrayList<>();
    TextView title_cat;
    ImageView img_banner;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_catgory);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        list = Utility.gson.fromJson(getIntent().getStringExtra("sub_catgory"), new TypeToken<List<Super_sub_Cat>>() {}.getType());
        lv=(ListView)findViewById(R.id.lv);
        title_cat=(TextView)findViewById(R.id.title_cat);
        img_banner=(ImageView) findViewById(R.id.img_banner);
        title_cat.setText(getIntent().getStringExtra("title"));
        if (!TextUtils.isEmpty(getIntent().getStringExtra("banner"))) {
            Utility.Set_image(getIntent().getStringExtra("banner"),img_banner);
        }
        sub_catgory_adpater=new Sub_catgory_adpater(getApplicationContext(),list);
        lv.setAdapter(sub_catgory_adpater);
        serach=(EditText)findViewById(R.id.edt_search);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Sub_catgory.this,Viewall.class);
                intent.putExtra("cat_id",list.get(i).getSub_cat_id());
                startActivity(intent);
                finish();//finishing activity
            }
        });

        serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sub_catgory_adpater.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
