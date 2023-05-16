package com.smd.chatapp.BusinessLayer;

import com.smd.chatapp.DataLayer.IChatDAO;
import com.smd.chatapp.DataLayer.IChatFirebaseDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Random;

public class Message implements Serializable {
    private  String chatName;
    private  String senderNumber; // user or remote
    private String receiverNumber;
    private  String message;
    private  String chatId;
    private  String dtStamp;

    public Message(String chatid, String name, String senderNum, String recvNum, String msg){
        chatId=chatid;
        receiverNumber=recvNum;
        chatName = name;
        senderNumber =senderNum;
        message=msg;
        dtStamp=java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    }

    public Message() {
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setDtStamp(String dtStamp) {
        this.dtStamp = dtStamp;
    }


    public String getChatName() {return chatName;}

    public String getSenderNumber() {
        return senderNumber;
    }

    public String getDtStamp() {
        return dtStamp;
    }

    public String getMessage() {
        return message;
    }

    public static String getRandomMessage(){
        Random rand=new Random();
        ArrayList<String> arr = new ArrayList<String>(
                Arrays.asList(
                        "I'm sorry, I didn't catch that. I was too busy thinking about tacos.",
                        "I'm nodding along, but really I'm just thinking about what I'm going to have for lunch.",
                        "I'm multitasking right now - pretending to listen to you and scrolling through Instagram at the same time.",
                        "Haha, that's so funny! Sorry, what did you say again?",
                        "I'm not ignoring you, I'm just easily distracted by shiny objects.",
                        "I'm not sure what you just said, but I'm going to reply with a smiley face anyway :)",
                        "Sorry, my mind was just wandering off to a tropical island. What were you saying again?",
                        "I'm pretending to listen to you, but really I'm just practicing my telekinesis on this pen.",
                        "I'm not sure if you noticed, but I was actually just staring at a piece of lint on my shirt instead of reading your message.",
                        "I'm not ignoring you, I'm just pretending to be a spy and using this conversation as a cover.",
                        "I'm sorry, my brain was just buffering. Can you repeat that?",
                        "I'm not ignoring you, I'm just practicing my mind-reading skills. You were thinking about pizza, right?",
                        "I'm not really listening, but keep talking anyway. Your voice is like music to my ears.",
                        "I'm trying to pay attention, but your message is just too darn long. Can you summarize it in a meme?",
                        "I'm not ignoring you, I'm just trying to figure out how to spell 'supercalifragilisticexpialidocious.'",
                        "Sorry, I got distracted by a butterfly. What were we talking about again?",
                        "I'm not ignoring you, I'm just pretending to be a secret agent and scanning this message for hidden codes.",
                        "I'm not sure if I'm listening to you or just hallucinating from hunger. Can we talk about food instead?",
                        "I'm not ignoring you, I'm just pretending to be a ninja and using stealth mode.",
                        "I'm not actually reading your message, I'm just admiring the cute little bubble that pops up when you type.",
                        "I'm not ignoring you, I'm just pretending to be a rock star and practicing my air guitar.",
                        "Sorry, I was just daydreaming about winning the lottery. What were you saying?",
                        "I'm not ignoring you, I'm just pretending to be a Jedi and using the Force to read your mind.",
                        "I'm not sure if I'm listening to you or just singing a song in my head. Can you repeat that?",
                        "I'm not ignoring you, I'm just pretending to be a pirate and searching",
                        "I'm sorry, were you saying something? I was too busy trying to figure out why you have such terrible taste in music.",
                        "I'm not sure if I'm listening to you or just trying to figure out how someone like you managed to get a job.",
                        "I'm pretending to care about what you're saying, but really I'm just counting down the seconds until this conversation is over.",
                        "I'm not sure if I'm listening to you or just trying to figure out how someone can be so wrong about everything.",
                        "I'm not ignoring you, I'm just trying to figure out how to politely tell you that your jokes are terrible.",
                        "I'm not sure if I'm listening to you or just wondering how someone like you can still be single.",
                        "I'm pretending to be interested, but really I'm just trying to figure out how you managed to dress yourself this morning.",
                        "I'm not ignoring you, I'm just trying to figure out how to respond without hurting your fragile ego.",
                        "I'm not sure if I'm listening to you or just trying to figure out how someone can be so clueless.",
                        "I'm not ignoring you, I'm just trying to figure out how to tell you that your opinions are objectively wrong.",
                        "I'm pretending to be engaged, but really I'm just trying to figure out how someone can be so oblivious to their own flaws.",
                        "I'm not sure if I'm listening to you or just trying to figure out how someone can be so annoying.",
                        "I'm not ignoring you, I'm just trying to figure out how many braincells u have."
                )
        );
        return arr.get(rand.nextInt(arr.size()));
    }

    public void save(IChatDAO dao, IChatFirebaseDAO fDao){
        if(dao!=null){
            Hashtable<String,String>msg=new Hashtable<String, String>();
            msg.put("chatname", chatName);
            msg.put("sendertype", senderNumber);
            msg.put("message",message);
            msg.put("dtstamp",dtStamp);
            //dao.save(msg);
            fDao.save(this);
        }
    }

   /* public static ArrayList<Message>load(IChatDAO d,IChatFirebaseDAO fd, String cname){
        ArrayList<Message>messages=new ArrayList<Message>();
        if(d!=null&&cname!=null){
            ArrayList<Hashtable<String,String>>msgs=d.load(cname);
            for(Hashtable<String,String>msg:msgs){
                Message m=new Message(, msg.get("chatname"), msg.get("sendertype"), msg.get("message"), msgTxt.getText().toString());
                m.setDtStamp(msg.get("dtstamp"));
                messages.add(m);
            }
        }
        return messages;
    }*/
}
