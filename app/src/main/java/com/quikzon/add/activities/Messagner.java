package com.quikzon.add.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.quikzon.add.R;
import com.quikzon.add.adapters.ChatAdapter;
import com.quikzon.add.model.ChatMessage;
import com.quikzon.add.restapi.User_details;
import com.quikzon.add.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Messagner extends AppCompatActivity {

    @BindView(R.id.Rvacc)
    ListView Rvacc;
    @BindView(R.id.send_msg)
    ImageView send_msg;
    @BindView(R.id.edt_msg)
    EditText edt_msg;
    ChatAdapter chatAdapter;
    ArrayList<ChatMessage> message;
    String user_name="";
    Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagenger);
        ButterKnife.bind(this);
        inti();
        try {
            socket = IO.socket("http://hopewebsolution.com:3000");
            socket.connect();
            socket.emit("join", "kamal");
            if(socket.connected())
            {
                Toast.makeText(getApplicationContext(),"joined",Toast.LENGTH_LONG).show();
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
//        try {
//            Utility.join_chat(this, Utility.single(this, User_details.display_name));
//            user_name=Utility.single(this, User_details.display_name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        // message send action
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                if(!edt_msg.getText().toString().isEmpty()){
                   socket.emit("messagedetection",user_name,edt_msg.getText().toString());
                    edt_msg.setText("");
                    edt_msg.setHint("Message...");
                }


            }
        });

        //implementing socket listeners
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        //Toast.makeText(this, data, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                       // Toast.makeText(ChatBoxActivity.this, data, Toast.LENGTH_SHORT).show();

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
                        try {
                            //extract data from fired event

                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");

                            // make instance of message

                           // Message m = new Message(nickname, message);

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
    }

    private void inti() {
        message=new ArrayList<>();
        chatAdapter=new ChatAdapter(this,message);
        Rvacc.setAdapter(chatAdapter);
    }
}
