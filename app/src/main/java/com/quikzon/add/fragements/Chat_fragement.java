package com.quikzon.add.fragements;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quikzon.add.R;

import butterknife.ButterKnife;

public class Chat_fragement extends Fragment implements View.OnClickListener {
    View view;
    FragmentTransaction fragmentTransaction;
    public Chat_fragement() {

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.accocunt_fragement, container, false);
        setRetainInstance(true);
        ButterKnife.bind(this, view);
        init(view);
        setdata();
        return view;
    }
    private void init(View view) {

    }
    private void setdata() {

    }
    private void start_fragment(Fragment fragment)
    {

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
         //   case R.id.Btlogin:
           //     start_fragment(new Login());
             //   break;

        }
    }

    // INTERFACE FOR FRAGEMENT LAYOUT OPEN
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
