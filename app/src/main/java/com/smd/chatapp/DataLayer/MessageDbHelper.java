package com.smd.chatapp.DataLayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDbHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION=1;
    public static final String DB_Name="ChatApp.db";
    public MessageDbHelper(Context context){
        super(context,DB_Name,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE chat (chatname TEXT , sendertype TEXT, message TEXT, dtstamp TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS chat");
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv){
        onUpgrade(sqLiteDatabase,oldv,newv);
    }
}
