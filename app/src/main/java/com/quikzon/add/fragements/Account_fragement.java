package com.quikzon.add.fragements;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quikzon.add.R;
import com.quikzon.add.activities.Login;
import com.quikzon.add.activities.Myaccount;
import com.quikzon.add.adapters.Account_list_adapter;
import com.quikzon.add.adapters.VIew_all_adapter;
import com.quikzon.add.model.Account_details;
import com.quikzon.add.restapi.User_details;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Account_fragement extends Fragment implements View.OnClickListener {
    View view;
    FragmentTransaction fragmentTransaction;

    public Account_fragement() {

    }
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.user_email)
    TextView user_email;
    @BindView(R.id.Lledit)
    LinearLayout Lledit;
    @BindView(R.id.profile_img)
    ImageView profile_img;
    @BindView(R.id.Rvacc)
    RecyclerView Rvacc;
    ArrayList<Account_details> account_detailsarray = new ArrayList<>();
    Account_list_adapter account_list_adapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.accocunt_fragement, container, false);
        setRetainInstance(true);
        ButterKnife.bind(this, view);
        init(view);
        setdata();
        dataget();
        return view;
    }

    private void init(View view) {
        Lledit.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setdata() {
        if (!Utility.get_login(Objects.requireNonNull(getActivity()))) {
            user_name.setText(R.string.guest_name);
            user_email.setText(R.string.guest_email);

        } else {
            try {
                user_name.setText(Utility.single_detail(getActivity(), User_details.Name));
                user_email.setText(Utility.single(getActivity(), User_details.user_email));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void dataget() {
        Account_details account_details = new Account_details();
        account_details.setTitle(getString(R.string.my_ads));
        account_details.setDesc(getString(R.string.my_ads_desc));

        Account_details account_details2 = new Account_details();
        account_details2.setTitle(getString(R.string.profile));
        account_details2.setDesc(getString(R.string.profie_desc));

        Account_details account_details3 = new Account_details();
        account_details3.setTitle(getString(R.string.pass_set));
        account_details3.setDesc(getString(R.string.forgot));
        Account_details account_details4 = new Account_details();
        account_details4.setTitle(getString(R.string.logout));
        account_details4.setDesc(getString(R.string.swicth));
        account_detailsarray.add(account_details);
        account_detailsarray.add(account_details2);
        account_detailsarray.add(account_details3);
        account_detailsarray.add(account_details4);

        Rvacc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        account_list_adapter = new Account_list_adapter(getContext(), account_detailsarray);
        Rvacc.setAdapter(account_list_adapter);
    }

    private void start_fragment(Fragment fragment) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Lledit:
                if (Utility.get_login(getActivity())) {
                    Intent intent = new Intent(getActivity(), Myaccount.class);
                    startActivity(intent);
                } else {
                    Intent i = new Intent(getActivity(), Login.class);
                    startActivityForResult(i, Utility.login);
                }
                break;
        }
    }

    // INTERFACE FOR FRAGEMENT LAYOUT OPEN
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
