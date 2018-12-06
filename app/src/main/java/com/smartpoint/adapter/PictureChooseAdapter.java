package com.smartpoint.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smartpoint.GlideImageLoader;
import com.smartpoint.MyApplication;
import com.smartpoint.marquee.R;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPreview;

/**
 * Created by gjz on 9/4/16.
 */
public class PictureChooseAdapter extends RecyclerView.Adapter<PictureChooseAdapter.FindViewHolder> {

    private ArrayList<String> contacts;
    private int layoutId;
    private GlideImageLoader loader;
    public PictureChooseAdapter(ArrayList<String> contacts, int layoutId) {
        this.contacts = contacts;
        this.layoutId = layoutId;
        loader = new GlideImageLoader();
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
    public void onBindViewHolder(final FindViewHolder holder, final int position) {
        String result = contacts.get(position);
        if (!TextUtils.isEmpty(result)){
            loader.displayImage(MyApplication.newInstance(),result,holder.imageView_pic);
        }
        holder.imageView_pic.setOnClickListener(v -> PhotoPreview.builder()
                .setPhotos(contacts)
                .setCurrentItem(position)
                .start((Activity) holder.imageView_pic.getContext()));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class FindViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView_pic;
        public FindViewHolder(View itemView) {
            super(itemView);
            imageView_pic =  itemView.findViewById(R.id.imageView_pic);

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