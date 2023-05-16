package com.smd.chatapp.BusinessLayer;

import com.smd.chatapp.DataLayer.IChatDAO;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class ChatListItem implements Serializable {
    private String chatId;
    private String user1Num, user2Num;  // format
    private String chatName;
    private String firstMsg;
    private String senderType;
    private String msgTime;

    public ChatListItem(String user1Num, String user2Num, String chatName, String firstMsg, String msgTime) {
        this.user1Num = user1Num; // format +92 followed by 10 digits   e.g. +923346119000
        this.user2Num = user2Num; // format +92 followed by 10 digits   e.g. +923346119000
        this.chatId = generateChatId(user1Num,user2Num);
        this.chatName = chatName;
        this.firstMsg = firstMsg;
        this.msgTime = msgTime;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getFirstMsg() {
        return firstMsg;
    }

    public void setFirstMsg(String firstMsg) {
        this.firstMsg = firstMsg;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUser1Num() {
        return user1Num;
    }

    public void setUser1Num(String user1Num) {
        this.user1Num = user1Num;
    }

    public String getUser2Num() {
        return user2Num;
    }

    public void setUser2Num(String user2Num) {
        this.user2Num = user2Num;
    }

    public static ArrayList<ChatListItem>loadList(IChatDAO d){
        ArrayList<ChatListItem>items=new ArrayList<ChatListItem>();
        if(d!=null){
            ArrayList<Hashtable<String,String>>chats=d.loadChatList();
            for(Hashtable<String,String>c:chats){
               // ChatListItem cli=new ChatListItem(c.get("chatname"),c.get("message"),c.get("sendertype"), c.get("dtstamp"));
                //items.add(cli);
            }
        }
        return items;
    }
    public static String generateChatId(String user1Num, String user2Num) {
        String[] sortedNumbers = {user1Num, user2Num};
        Arrays.sort(sortedNumbers);

        String concatenatedNumbers = sortedNumbers[0] + sortedNumbers[1];
        String chatId = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(concatenatedNumbers.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }

            chatId = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return chatId;
    }
}
