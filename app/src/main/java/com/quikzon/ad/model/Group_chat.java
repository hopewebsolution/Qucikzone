package com.quikzon.ad.model;

import java.util.ArrayList;

public class Group_chat {
    String room_id;
    ArrayList<ChatMessage> messages=new ArrayList<>();

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }
}