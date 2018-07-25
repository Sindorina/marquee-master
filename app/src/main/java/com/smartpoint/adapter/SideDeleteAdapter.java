package com.smartpoint.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartpoint.marquee.R;

import java.util.List;

/**
 * Created by gjz on 9/4/16.
 */
public class SideDeleteAdapter extends RecyclerView.Adapter<SideDeleteAdapter.ContactsViewHolder> {
    private List<String> contacts;
    private int layoutId;
    public SideDeleteAdapter(List<String> contacts, int layoutId) {
        this.contacts = contacts;
        this.layoutId = layoutId;
    }

    public List<String> getContacts() {
        return contacts;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, final int position) {
        String item = contacts.get(position);
        holder.textView.setText(item);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            textView =  itemView.findViewById(R.id.textView);
        }
    }
}