package com.smd.chatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.smd.chatapp.UILayer.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Context _context;

    int Private_mode=0;
    private static final String PREF_NAME="AndroidHivePref";
    public static final String KEY_PHONE="phone";
    private static final String IS_LOGIN = "islogin";
    public SessionManager(Context context){
        this._context=context;
        pref=_context.getSharedPreferences(PREF_NAME,Private_mode);
        editor= pref.edit();
    }

    public void createLoginSession(String phoneNum){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_PHONE,phoneNum);
        editor.commit();
    }

    public Boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN,false);
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        user.put(KEY_PHONE,pref.getString(KEY_PHONE,null));
        return user;
    }

    public void LogoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }


}
