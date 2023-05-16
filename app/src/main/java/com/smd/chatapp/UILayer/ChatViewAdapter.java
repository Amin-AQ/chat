package com.smd.chatapp.UILayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.smd.chatapp.BusinessLayer.Message;
import com.smd.chatapp.R;

import java.util.ArrayList;

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ChatViewHolder> {
    ArrayList<Message> messageList;
    String userPhoneNum;
    Context context;

    public ChatViewAdapter(Context context1, ArrayList<Message> msgs, String userPhoneNum){
        this.context=context1;
        this.messageList=msgs;
        this.userPhoneNum=userPhoneNum;
    }

    public void setMessageList(ArrayList<Message> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_layout, parent, false);
        return new ChatViewHolder(v);
    }

    public void refreshAdapter(int index) {
        notifyItemInserted(index);
    }

    public void reloadAdapter( ArrayList<Message>lst){
        if(messageList==null) {
            messageList = new ArrayList<Message>();
            notifyDataSetChanged();
        }
        int s =messageList.size();
        messageList.clear();
        notifyItemRangeRemoved(0,s);
        if(lst.size()>0) {
            messageList = lst;
            notifyItemRangeInserted(0, messageList.size());
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Log.d("Debug","In OnBindViewHolder");
        holder.messageText.setText(messageList.get(position).getMessage());

        if(messageList.get(position).getSenderNumber().equals(userPhoneNum)){
            holder.nameText.setText("You:");
            holder.timestamp.setText("Sent: "+messageList.get(position).getDtStamp());
            RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.cardView.setLayoutParams(layoutParams);
        }else{
            holder.nameText.setText(messageList.get(position).getSenderNumber()+":");
            holder.timestamp.setText("Received: "+messageList.get(position).getDtStamp());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView messageText;
        TextView nameText;
        TextView timestamp;
        public ChatViewHolder(View itemView){
            super(itemView);
            messageText=(TextView)itemView.findViewById(R.id.message);
            nameText=(TextView)itemView.findViewById(R.id.name);
            cardView=(CardView) itemView.findViewById(R.id.msgcard);
            timestamp=(TextView) itemView.findViewById(R.id.stamp);
        }

    }


}
