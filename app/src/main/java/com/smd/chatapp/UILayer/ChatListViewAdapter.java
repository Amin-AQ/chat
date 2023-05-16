package com.smd.chatapp.UILayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smd.chatapp.BusinessLayer.ChatListItem;
import com.smd.chatapp.R;

import java.util.ArrayList;

public class ChatListViewAdapter extends RecyclerView.Adapter<ChatListViewAdapter.ChatListViewHolder> {

    Context context;
    ArrayList<ChatListItem> chatListItems;
    OnItemClickListener listener;
    String userNum;
    public ChatListViewAdapter(Context ctx, ArrayList<ChatListItem> items,String userN, OnItemClickListener onItemClickListener) {
        context=ctx;
        chatListItems=items;
        this.listener=onItemClickListener;
        this.userNum=userN;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist_item_layout,parent,false);
        return new ChatListViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.chatNameTxt.setText(chatListItems.get(position).getChatName());
        holder.timeTxt.setText(chatListItems.get(position).getMsgTime());
        if(chatListItems.get(position).getUser1Num().equals(userNum))
            holder.lastMsgTxt.setText("You: " + chatListItems.get(position).getFirstMsg());
        else
            holder.lastMsgTxt.setText(chatListItems.get(position).getUser1Num()+": " + chatListItems.get(position).getFirstMsg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onItemClick(chatListItems.get(position));
                }
            }
        });
    }

    public void refreshAdapter(ChatListItem i) {
        chatListItems.add(i);
        notifyItemInserted(chatListItems.size() - 1);
    }

    public void reloadAdapter( ArrayList<ChatListItem>lst){
        int s=chatListItems.size();
        chatListItems.clear();
        notifyItemRangeRemoved(0,s);
        chatListItems=lst;
        notifyItemRangeInserted(0,chatListItems.size());
    }

    @Override
    public int getItemCount() {
        return chatListItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder{

        TextView chatNameTxt, lastMsgTxt, timeTxt;

        public ChatListViewHolder(View itemView){

            super(itemView);
            chatNameTxt=(TextView)itemView.findViewById(R.id.chatnametext);
            lastMsgTxt=(TextView)itemView.findViewById(R.id.lastmsgtxt);
            timeTxt=(TextView)itemView.findViewById(R.id.timetxt);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ChatListItem item);
    }
}
