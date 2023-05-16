package com.smd.chatapp.DataLayer;

import com.smd.chatapp.BusinessLayer.Message;

import java.util.ArrayList;

public interface MessagesCallback {
    void onMessagesLoaded(ArrayList<Message>messages);
}
