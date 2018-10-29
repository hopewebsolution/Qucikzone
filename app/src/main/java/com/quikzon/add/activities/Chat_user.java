package com.quikzon.add.activities;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quikzon.add.R;
import com.quikzon.add.adapters.Chat_user_adpater;
import com.quikzon.add.model.Chat_list;
import com.quikzon.add.utility.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Chat_user extends Fragment {

    @BindView(R.id.users)
    RecyclerView users;
    ArrayList<Chat_list> chats;
    Chat_user_adpater adpater;
    View view;
    public Chat_user() {

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_chat_user, container, false);
        ButterKnife.bind(this, view);
        initi(view);
        return view;
    }



    private void initi(View view) {
        chats=new ArrayList<>();
        chats=Utility.get_chat_user(getActivity());
        if(chats!=null) {
            adpater = new Chat_user_adpater(getActivity(), chats);
            users.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
            users.setAdapter(adpater);
        }
    }
}
