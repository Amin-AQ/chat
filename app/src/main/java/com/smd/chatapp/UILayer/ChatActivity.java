package com.smd.chatapp.UILayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smd.chatapp.BusinessLayer.ChatListItem;
import com.smd.chatapp.DataLayer.ChatDAO;
import com.smd.chatapp.DataLayer.ChatFirebaseDAO;
import com.smd.chatapp.DataLayer.IChatDAO;
import com.smd.chatapp.BusinessLayer.Message;
import com.smd.chatapp.DataLayer.IChatFirebaseDAO;
import com.smd.chatapp.DataLayer.MessagesCallback;
import com.smd.chatapp.R;
import com.smd.chatapp.SessionManager;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    //private ChatViewModel chatViewModel;
    EditText msgTxt;
    TextView chatNameTxt;
    Button sendBtn;
    RecyclerView view;
    Intent intent;
    ChatViewAdapter chatViewAdapter;
    LinearLayoutManager linearLayoutManager;
    IChatDAO dao;
    IChatFirebaseDAO fDao;
    ArrayList<Message> messageList;
    String chatName, userPhoneNumber, receiverPhoneNumber, chatId;
    Handler handler=new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        messageList=null;
        dao=new ChatDAO(this);
        fDao=new ChatFirebaseDAO(this);
        intent=getIntent();
        chatName=intent.getStringExtra("chatname");
        userPhoneNumber=intent.getStringExtra(SessionManager.KEY_PHONE);
        receiverPhoneNumber=intent.getStringExtra("receiver_number");
        chatId= ChatListItem.generateChatId(userPhoneNumber,receiverPhoneNumber);
      // chatViewModel =new ViewModelProvider(this).get(ChatViewModel.class);
        view=(RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        view.setLayoutManager(linearLayoutManager);
        fDao.loadMessagesForChat(chatId, new MessagesCallback() {
            @Override
            public void onMessagesLoaded(ArrayList<Message> messages) {
                if(messages!=null) {
                    messageList = messages;
                    chatViewAdapter = new ChatViewAdapter(ChatActivity.this, messageList, userPhoneNumber);
                    view.setAdapter(chatViewAdapter); // set the Adapter to RecyclerView
                }
            }
        });
        msgTxt = (EditText)findViewById(R.id.msgtxt);
        sendBtn = (Button)findViewById(R.id.sendbtn);
        chatNameTxt=findViewById(R.id.chatnametext);
        chatNameTxt.setText(receiverPhoneNumber);
        //loadData();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(msgTxt.getText().toString().length()==0){
                    Toast.makeText(ChatActivity.this, "Enter a message ! ! !", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("Debug","Here");
                    Message m=new Message(chatId,receiverPhoneNumber,userPhoneNumber,receiverPhoneNumber,msgTxt.getText().toString());
                    messageList.add(m);
                    //chatViewAdapter.refreshAdapter(messageList.size()-1);
                    m.save(dao,fDao);
                    msgTxt.setText("");
                }
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        //loadData();
    }

/*    void loadData(){
        messageList=Message.load(dao,fDao,chatName);
        if (messageList.size() > 0) {
            chatViewAdapter.reloadAdapter(messageList);
        } else {
            messageList = new ArrayList<>();
            chatViewAdapter.setMessageList(messageList);
        }
    }*/

}