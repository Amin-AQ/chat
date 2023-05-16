package com.smd.chatapp.DataLayer;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smd.chatapp.BusinessLayer.ChatListItem;
import com.smd.chatapp.BusinessLayer.Message;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatFirebaseDAO implements IChatFirebaseDAO {
    FirebaseDatabase db;
    DatabaseReference ref;
    Context context;
    ArrayList<Hashtable<String,Object>> data;
    ValueEventListener messagesListner;

    public ChatFirebaseDAO(Context context) {
        this.context = context;
        db=FirebaseDatabase.getInstance();
        ref= db.getReference();
    }

    @Override
    public void save(Message msg) {
        DatabaseReference chatRef = ref.child(msg.getChatId());
        String key = chatRef.push().getKey(); // Generate a unique ID for the message
        if (key != null) {
            DatabaseReference msgRef = chatRef.child(key);
            msgRef.setValue(msg);
            chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Retrieve the updated message list
                    List<Message> updatedMessages = new ArrayList<>();
                    for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                        Message message = messageSnapshot.getValue(Message.class);
                        updatedMessages.add(message);
                    }
                    // Perform further operations with updatedMessages if needed
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                    Log.d("FirebaseCancelSave", error.toString());
                }
            });
        }
    }

    @Override
    public ArrayList<Message> load() {
        ArrayList<Message>msgs=new ArrayList<Message>();
        Task<DataSnapshot> dataSnapshotTask= ref.get();
        try{
            Tasks.await(dataSnapshotTask);
            DataSnapshot dataSnapshot=dataSnapshotTask.getResult();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Message item=snapshot.getValue(Message.class);
                    msgs.add(item);

            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
        return msgs;
    }

    @Override
    public void loadMessagesForChat(String chatId, MessagesCallback callback) {
        DatabaseReference chatRef = ref.child(chatId);
        Task<DataSnapshot> dataSnapshotTask = chatRef.get();
        messagesListner= chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Message> msgs = new ArrayList<Message>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Message msg=dataSnapshot.getValue(Message.class);
                    if(msg!=null)
                        msgs.add(msg);
                }
                callback.onMessagesLoaded(msgs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context,"Failed to retreive messages.",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void loadChatListForUser(String userNumber, ChatListCallback callback) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<ChatListItem> chatList = new ArrayList<>();
                    int chatCount = (int) dataSnapshot.getChildrenCount();
                    AtomicInteger processedCount = new AtomicInteger(0);

                    for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                        DatabaseReference chatMessagesRef = chatSnapshot.getRef();
                        Query query = chatMessagesRef.limitToLast(1);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                        Message message = messageSnapshot.getValue(Message.class);
                                        if (message != null) {
                                            // Check if the user ID matches the sender or receiver
                                            if (message.getSenderNumber().equals(userNumber)) {
                                                ChatListItem item=new ChatListItem(message.getSenderNumber(),message.getReceiverNumber(),message.getReceiverNumber(),message.getMessage(),message.getDtStamp());
                                                chatList.add(item);
                                            }else if(message.getReceiverNumber().equals(userNumber)){
                                                ChatListItem item=new ChatListItem(message.getSenderNumber(),message.getReceiverNumber(),message.getSenderNumber(),message.getMessage(),message.getDtStamp());
                                                chatList.add(item);
                                            }
                                        }
                                    }
                                }

                                int count = processedCount.incrementAndGet();
                                if (count == chatCount) {
                                    // All chats processed, invoke the callback function
                                    callback.onChatListReceived(chatList);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                                int count = processedCount.incrementAndGet();
                                if (count == chatCount) {
                                    // All chats processed, invoke the callback function
                                    callback.onChatListReceived(chatList);
                                }
                            }
                        });
                    }
                } else {
                    // No chats found, invoke the callback function with empty list
                    callback.onChatListReceived(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                callback.onChatListReceived(new ArrayList<>());
            }
        });
    }
}
