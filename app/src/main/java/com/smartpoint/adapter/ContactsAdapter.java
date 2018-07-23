package com.smartpoint.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartpoint.entity.Contact;
import com.smartpoint.marquee.R;

import java.util.List;

/**
 * Created by gjz on 9/4/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private List<Contact> contacts;
    private int layoutId;

    public ContactsAdapter(List<Contact> contacts, int layoutId) {
        this.contacts = contacts;
        this.layoutId = layoutId;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, final int position) {
        Contact contact = contacts.get(position);
        if (position == 0 || !contacts.get(position-1).getIndex().equals(contact.getIndex())) {
            holder.tvIndex.setVisibility(View.VISIBLE);
            holder.tvIndex.setText(contact.getIndex());
        } else {
            holder.tvIndex.setVisibility(View.GONE);
        }
        holder.tvName.setText(contact.getName());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick!=null){
                    onItemClick.clickItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIndex;
        public ImageView ivAvatar;
        public TextView tvName;
        RelativeLayout relativeLayout;
        public ContactsViewHolder(View itemView) {
            super(itemView);
            tvIndex =  itemView.findViewById(R.id.tv_index);
            ivAvatar =  itemView.findViewById(R.id.iv_avatar);
            tvName =  itemView.findViewById(R.id.tv_name);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void clickItem(int position);
    }
}