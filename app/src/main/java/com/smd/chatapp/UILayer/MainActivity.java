package com.smd.chatapp.UILayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smd.chatapp.BusinessLayer.ChatListItem;
import com.smd.chatapp.DataLayer.ChatDAO;
import com.smd.chatapp.DataLayer.ChatFirebaseDAO;
import com.smd.chatapp.DataLayer.ChatListCallback;
import com.smd.chatapp.DataLayer.IChatDAO;
import com.smd.chatapp.DataLayer.IChatFirebaseDAO;
import com.smd.chatapp.R;
import com.smd.chatapp.SessionManager;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ContactsFragment.PassContactToActivity {
    ArrayList<ChatListItem> chatListItems;
    IChatDAO dao;
    IChatFirebaseDAO fDao;
    ChatListViewAdapter chatListViewAdapter;
    LinearLayoutManager linearLayoutManager;
    SessionManager sessionManager;
    RecyclerView view;
    Button newChatBtn;
    String userPhoneNumber;
    final int READ_CONTACT_PERMISSION_CODE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager=new SessionManager(getApplicationContext());
        if(!sessionManager.isLoggedIn()){
            sessionManager.checkLogin();
            finish();
        }
        userPhoneNumber=sessionManager.getUserDetails().get(SessionManager.KEY_PHONE);
        setContentView(R.layout.activity_main);
        fDao=new ChatFirebaseDAO(this);
        //dao=new ChatDAO(this);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        newChatBtn=(Button)findViewById(R.id.createchatbtn);
        view = (RecyclerView)findViewById(R.id.chatlistview);
        view.setLayoutManager(linearLayoutManager);
        view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        fDao.loadChatListForUser(userPhoneNumber, new ChatListCallback() {
            @Override
            public void onChatListReceived(ArrayList<ChatListItem> chatList) {
                if(chatList!=null) {
                    chatListItems = chatList;
                    chatListViewAdapter = new ChatListViewAdapter(MainActivity.this, chatListItems, userPhoneNumber, new ChatListViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(ChatListItem item) {
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            intent.putExtra("chatname", item.getChatName());
                            intent.putExtra("receiver_number", item.getChatName());
                            intent.putExtra(SessionManager.KEY_PHONE, userPhoneNumber);
                            startActivity(intent);
                        }
                    });
                    view.setAdapter(chatListViewAdapter); // set the Adapter to RecyclerView
                }
            }
        });

        //loadChatList();
        newChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},READ_CONTACT_PERMISSION_CODE);
                    }
                    else
                        launchContactsFragment();

                }
            }
        });
    }

    void launchContactsFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ContactsFragment contactsFragment = new ContactsFragment();
        fragmentTransaction.replace(R.id.fragment_container, contactsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        fDao.loadChatListForUser(userPhoneNumber, new ChatListCallback() {
            @Override
            public void onChatListReceived(ArrayList<ChatListItem> chatList) {
                if(chatList!=null) {
                    chatListItems = chatList;
                    chatListViewAdapter = new ChatListViewAdapter(MainActivity.this, chatListItems, userPhoneNumber, new ChatListViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(ChatListItem item) {
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            intent.putExtra("chatname", item.getChatName());
                            intent.putExtra("receiver_number", item.getChatName());
                            intent.putExtra(SessionManager.KEY_PHONE, userPhoneNumber);
                            startActivity(intent);
                        }
                    });
                    view.setAdapter(chatListViewAdapter); // set the Adapter to RecyclerView
                }
            }
        });
    }

    public void loadChatList(){
        chatListItems= ChatListItem.loadList(dao);
        if(chatListItems.size()>0)
            chatListViewAdapter.reloadAdapter(chatListItems);
    }

    @Override
    public void getContact(Map<String, String> contact) {
        Intent intent=new Intent(MainActivity.this,ChatActivity.class);
        intent.putExtra("chatname",contact.get("name"));
        intent.putExtra("receiver_number",contact.get("number"));
        intent.putExtra(SessionManager.KEY_PHONE,userPhoneNumber);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==READ_CONTACT_PERMISSION_CODE&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            launchContactsFragment();
        else
            Toast.makeText(MainActivity.this,"Permission denied, cannot use this feature.",Toast.LENGTH_LONG).show();
    }
}