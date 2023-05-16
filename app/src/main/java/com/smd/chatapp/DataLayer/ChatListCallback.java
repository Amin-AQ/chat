package com.smd.chatapp.DataLayer;

import com.smd.chatapp.BusinessLayer.ChatListItem;

import java.util.ArrayList;

public interface ChatListCallback {
    void onChatListReceived(ArrayList<ChatListItem>chatListItems);
}
