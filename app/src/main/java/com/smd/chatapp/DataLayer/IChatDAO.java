package com.smd.chatapp.DataLayer;

import java.util.ArrayList;
import java.util.Hashtable;

public interface IChatDAO {
    void save(Hashtable<String, String> msg);
    void save(ArrayList<Hashtable<String, String>> msgs);
    ArrayList<Hashtable<String,String>> load(String keey);
    ArrayList<Hashtable<String,String>>loadChatList();
    Boolean exists(String chatName);
}
