package com.smd.chatapp.DataLayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class ChatDAO implements IChatDAO{
    private Context context;

    public ChatDAO(Context context) {
        this.context = context;
    }

    @Override
    public void save(Hashtable<String,String> msg) {
        MessageDbHelper helper=new MessageDbHelper(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues vals=new ContentValues();
        Enumeration<String>keys=msg.keys();
        while(keys.hasMoreElements()){
            String key= keys.nextElement();
            vals.put(key,msg.get(key));
        }
        db.insert("chat",null,vals);
    }

    @Override
    public void save(ArrayList<Hashtable<String,String>> msgs) {
        for (Hashtable<String,String>msg:msgs)
            save(msg);
    }

    @Override
    public ArrayList<Hashtable<String, String>> load(String keey) {
        ArrayList<Hashtable<String, String>> msgs = new ArrayList<Hashtable<String, String>>();
        if(keey!=null) {
            MessageDbHelper helper = new MessageDbHelper(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            String[] args = new String[1];
            args[0] = keey;
            Cursor cursor = db.rawQuery("SELECT * FROM chat WHERE chatname = ?", args);
            while (cursor.moveToNext()) {
                Hashtable<String, String> msg = new Hashtable<String, String>();
                String[] columns = cursor.getColumnNames();
                for (String col : columns) {
                    int i = cursor.getColumnIndex(col);
                    if (i >= 0)
                        msg.put(col.toLowerCase(), cursor.getString(i));
                }
                msgs.add(msg);
            }
            cursor.close();
        }
        return msgs;
    }

    @Override
    public ArrayList<Hashtable<String, String>> loadChatList() {
        MessageDbHelper helper=new MessageDbHelper(context);
        SQLiteDatabase db=helper.getReadableDatabase();
        ArrayList<Hashtable<String,String>>chats=new ArrayList<Hashtable<String, String>>();
        String sql="SELECT *\n" +
                "FROM chat t1\n" +
                "WHERE t1.ROWID = (\n" +
                "  SELECT MAX(t2.ROWID)\n" +
                "  FROM chat t2\n" +
                "  WHERE t1.ChatName = t2.ChatName\n" +
                ")\n" +
                "GROUP BY chatname;\n";
        Cursor cursor=db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            Hashtable<String,String>item=new Hashtable<String,String>();
            String[]cols= cursor.getColumnNames();
            for(String col:cols){
                int i=cursor.getColumnIndex(col);
                if(i>=0)
                    item.put(col.toLowerCase(), cursor.getString(i));
            }
            chats.add(item);
        }
        cursor.close();
        return chats;
    }

    @Override
    public Boolean exists(String chatName) {
        MessageDbHelper helper=new MessageDbHelper(context);
        SQLiteDatabase db=helper.getReadableDatabase();
        ArrayList<Hashtable<String,String>>chats=new ArrayList<Hashtable<String, String>>();
        String sql="SELECT COUNT(*) as count\n" +
                "FROM chat\n" +
                "WHERE chatname = ?";
        String[]args=new String[1];
        args[0]=chatName;
        Cursor cursor=db.rawQuery(sql,args);
        boolean chatNameExists = false;
        if (cursor.moveToFirst()) {
            int i =cursor.getColumnIndex("count");
            if(i>=0){
                int count = cursor.getInt(i);
                if (count > 0)
                    chatNameExists = true;
            }
        }
        cursor.close();
        return chatNameExists;
    }
}
