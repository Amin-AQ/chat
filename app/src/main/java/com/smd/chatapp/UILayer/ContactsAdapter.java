package com.smd.chatapp.UILayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smd.chatapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Map<String, String>> contacts;
    private OnItemClickListener onItemClickListener;
    public ContactsAdapter(Set<Map<String, String>> contacts, OnItemClickListener listner) {
        this.contacts = new ArrayList<Map<String, String>>(contacts);
        this.onItemClickListener=listner;
    }



    public List<Map<String, String>> getContacts() {
        return contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contacts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, String> contact = contacts.get(position);
        holder.contactNameTextView.setText(contact.get("name"));
        holder.contactNumberTextView.setText(contact.get("number"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactNameTextView, contactNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactNameTextView = itemView.findViewById(R.id.contact_name);
            contactNumberTextView = itemView.findViewById(R.id.contact_number);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Map<String,String> contact);
    }
}
