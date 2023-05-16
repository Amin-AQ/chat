package com.smd.chatapp.UILayer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smd.chatapp.ContactsUtil;
import com.smd.chatapp.R;

import java.util.Map;
import java.util.Set;

public class ContactsFragment extends Fragment {

        private RecyclerView recyclerView;
        private Set<Map<String, String>> contacts;
        private ContactsAdapter adapter;
        private PassContactToActivity dataPasser;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);

            recyclerView = view.findViewById(R.id.contacts_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(itemDecoration);

            contacts = ContactsUtil.getContacts(getContext());
            adapter = new ContactsAdapter(contacts, new ContactsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Map<String, String> contact) {
                    dataPasser.getContact(contact);
                    getActivity().getSupportFragmentManager().popBackStack();

                }
            });
            recyclerView.setAdapter(adapter);
            if(contacts.size()==0)
                Toast.makeText(getActivity(),"No contacts in your phone",Toast.LENGTH_SHORT).show();

            return view;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            dataPasser=(PassContactToActivity)context;
        }
/*        private void saveSelectedContacts() {
            // Get the saved selected contacts (if any)
            Set<String> mSelectedContacts = new HashSet<String>();
            Log.d("Here1",mSelectedContacts.toString());
            // Get the selected contacts from the adapter
            List<Map<String, String>> contacts = adapter.getContacts();
            for (int i = 0; i < contacts.size(); i++) {
                Map<String,String> contact = contacts.get(i);
                Boolean isChecked=contactsCheck.get(i);
                if (isChecked) {
                    // Check if the contact is not already saved
                    // Add the contact to the saved set
                    mSelectedContacts.add(contact.get("number"));
                }
            }
            Log.d("Here1",mSelectedContacts.toString());
            // Save the selected contacts to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            editor.putStringSet("selectedContacts", mSelectedContacts);
            Log.d("Hare", String.valueOf(editor.commit()));
        }*/
    public interface PassContactToActivity{
        public void getContact(Map<String,String>contact);
    }
}
