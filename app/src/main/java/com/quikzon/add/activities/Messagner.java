package com.quikzon.add.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quikzon.add.R;
import com.quikzon.add.adapters.ChatAdapter;
import com.quikzon.add.model.ChatMessage;
import com.quikzon.add.model.Typing;
import com.quikzon.add.restapi.User_details;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Messagner extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.Rvacc)
    ListView Rvacc;
    @BindView(R.id.send_msg)
    ImageView send_msg;
    @BindView(R.id.edt_msg)
    EditText edt_msg;
    ChatAdapter chatAdapter;
    ArrayList<ChatMessage> message;
    @BindView(R.id.back)
     ImageView back;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.ad_title)
    TextView ad_title;
    String user_id = "";
    Socket socket;
    Gson gson;
    String author_id = "";
    String ad_id = "";
    String author_name = "";
    String ad_title_str= "";
    String room_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagenger);
        ButterKnife.bind(this);
        gson = new Gson();
        inti();

        //to connect with  node
        try {
            socket = Utility.join_chat(this, Utility.single(this, User_details.display_name));
            user_id = Utility.single(this, User_details.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        edt_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0)
                {
                    Typing typing=new Typing();
                    typing.setAd_id(ad_id);
                    typing.setUser_id(user_id);
                    socket.emit("typing", gson.toJson(typing));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                {
                    Typing typing=new Typing();
                    typing.setAd_id(ad_id);
                    typing.setUser_id(user_id);
                    socket.emit("stop_typing", gson.toJson(typing));
                }
            }
        });

        // message send action
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                if (!edt_msg.getText().toString().isEmpty()) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setBody(edt_msg.getText().toString());
                    chatMessage.setDate(current_date());
                    chatMessage.setSender(user_id);
                    chatMessage.setReceiver(author_id);
                    chatMessage.setAd_id(ad_id);
                    chatMessage.setRoom_id(user_id+author_id+ad_id);
                    chatMessage.setMine(true);
                    socket.emit("messagedetection", gson.toJson(chatMessage));
                    edt_msg.setText("");
                    edt_msg.setHint("Message...");


                }


            }
        });


        socket.on("userdisconnect", new Emitter.Listener() {
            @Override

            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                    }
                });
            }
        });

        socket.on("typing", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        Typing typing=null;
                        try {
                             typing = new Gson().fromJson(data.getString("typing_content"), Typing.class);
                            if(typing.getAd_id().equalsIgnoreCase(ad_id)&&typing.getUser_id().equalsIgnoreCase(user_id)) {
                                edt_msg.setText("Typing...");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        socket.on("stop_typing", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            Typing typing = new Gson().fromJson(data.getString("typing_content"), Typing.class);
                            if(typing.getAd_id().equalsIgnoreCase(ad_id)&&typing.getUser_id().equalsIgnoreCase(user_id)) {
                                edt_msg.setHint("Message...");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        ChatMessage chatMessage = null;
                        try {
                            chatMessage = new Gson().fromJson(data.getString("message"), ChatMessage.class);

                            if ((chatMessage.getSender().equalsIgnoreCase(Utility.single(Messagner.this, User_details.id))
                                    || chatMessage.getReceiver().equalsIgnoreCase(author_id))&&chatMessage.getAd_id().equalsIgnoreCase(ad_id))
                                 message.add(chatMessage);
                                chatAdapter.notifyDataSetChanged();
                                Rvacc.smoothScrollToPosition(message.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        Utility.save_chat(Messagner.this, user_id,author_id,ad_id, gson.toJson(message));
    }

    private void inti() {
        message = new ArrayList<>();

        if (getIntent() != null) {
            author_id = getIntent().getStringExtra("author_id");
            ad_id = getIntent().getStringExtra("ad_id");
            author_name = getIntent().getStringExtra("author_name");
            ad_title_str = getIntent().getStringExtra("ad_title");
            room_id=getIntent().getStringExtra("room_id");
        }

        try {
            message=Utility.get_chat(this,Utility.single(this,User_details.id),author_id,ad_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(message!=null) {
            chatAdapter = new ChatAdapter(this, message);
            Rvacc.setAdapter(chatAdapter);
        }
        user_name.setText(author_name);
        ad_title.setText(ad_title_str);
        back.setOnClickListener(this);

    }

    private String current_date() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                onBackPressed();
                break;
        }
    }
}
