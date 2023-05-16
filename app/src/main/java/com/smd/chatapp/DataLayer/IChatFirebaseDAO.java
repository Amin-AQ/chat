package com.smd.chatapp.DataLayer;

import com.smd.chatapp.BusinessLayer.Message;

import java.util.ArrayList;

public interface IChatFirebaseDAO {
    void save(Message msg);
    void loadMessagesForChat(String chatId, MessagesCallback callback);
    void loadChatListForUser(String userNumber, ChatListCallback callback);
    ArrayList<Message>load();
}
