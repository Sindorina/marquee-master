package com.smartpoint.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartpoint.marquee.R;

import java.util.List;

/**
 * Created by gjz on 9/4/16.
 */
public class AutoViewAdapter extends RecyclerView.Adapter<AutoViewAdapter.FindViewHolder> {

    private List<String> contacts;
    private int layoutId;

    public AutoViewAdapter(List<String> contacts, int layoutId) {
        this.contacts = contacts;
        this.layoutId = layoutId;
    }


    public List<String> getContacts() {
        return contacts;
    }

    @Override
    public FindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new FindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FindViewHolder holder, final int position) {
        holder.imageView.setImageResource(R.mipmap.bg);
    }

    @Override
    public int getItemCount() {
        return contacts == null ? 20 : contacts.size();
    }

    class FindViewHolder extends RecyclerView.ViewHolder {
        public AppCompatImageView imageView;

        public FindViewHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.imageView_auto);

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