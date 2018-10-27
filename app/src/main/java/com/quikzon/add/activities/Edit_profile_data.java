package com.quikzon.add.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.quikzon.add.R;
import com.quikzon.add.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_profile_data extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.Tvcancel)
    TextView Tvcancel;
    @BindView(R.id.Tvprofileedit)
    TextView Tvprofileedit;
    @BindView(R.id.Tvsave)
    TextView Tvsave;
    @BindView(R.id.CIimg)
    CircleImageView CIimg;
    @BindView(R.id.Tvedit)
    TextView Tvedit;
    @BindView(R.id.Etfirstname)
    EditText Etfirstname;
    @BindView(R.id.Tvcityselect)
    TextView Tvcityselect;
    @BindView(R.id.Tvemail)
    TextView Tvemail;
    @BindView(R.id.Tvmobile)
    TextView Tvmobile;
    @BindView(R.id.Tvchangepass)
    TextView Tvchangepass;
    @BindView(R.id.Etcurrpass)
    EditText Etcurrpass;
    @BindView(R.id.Etnewpass)
    EditText Etnewpass;
    @BindView(R.id.Etconfpass)
    EditText Etconfpass;
    ArrayList<String> returnValue = new ArrayList<>();
    String currentpass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_data);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        init();
        setdata();
    }

    private void init() {
        Tvchangepass.setOnClickListener(this);
        Tvcityselect.setOnClickListener(this);
        Tvedit.setOnClickListener(this);
        Tvcancel.setOnClickListener(this);
        Tvsave.setOnClickListener(this);
        CIimg.setOnClickListener(this);
        Tvcityselect.setText(Utility.getcity(this));
    }
    private void setdata() {
        JSONObject object = null;
        try {
            object = new JSONObject(Utility.get_detail(this));
            JSONObject profile_details = object.getJSONObject("data");
        //    CIimg.setImageResource(Integer.parseInt(profile_details.getString("profile_img")));
            Etfirstname.setText(profile_details.getString("display_name"));
            Tvemail.setText(profile_details.getString("user_email"));
            Tvmobile.setText(profile_details.getString("phone"));
            currentpass = profile_details.getString("user_pass");
            Utility.Set_image(profile_details.getString("profile_img"),CIimg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Tvchangepass:
                Etcurrpass.setVisibility(View.VISIBLE);
                Etnewpass.setVisibility(View.VISIBLE);
                Etconfpass.setVisibility(View.VISIBLE);
                break;
            case R.id.Tvcityselect:
                startActivity(new Intent(this,City_selector.class));
                break;
            case R.id.Tvedit:
                Etfirstname.setEnabled(true);
                Tvcityselect.setEnabled(true);
                Tvchangepass.setEnabled(true);
                Etconfpass.setEnabled(true);
                Etnewpass.setEnabled(true);
                Etcurrpass.setEnabled(true);
                break;
            case R.id.Tvcancel:
                finish();
                break;
            case R.id.Tvsave:
                finish();
                break;
            case R.id.CIimg:
                Pix.start(this,1,1);
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
    protected void onResume() {
        super.onResume();
        Tvcityselect.setText(Utility.getcity(this));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            returnValue.clear();
            returnValue.addAll(data.getStringArrayListExtra(Pix.IMAGE_RESULTS));
            Log.d("return",returnValue.get(0));
            CIimg.setImageURI(Uri.parse(returnValue.get(0)));
        }
    }
}
