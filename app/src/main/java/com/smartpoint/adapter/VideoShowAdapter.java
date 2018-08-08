package com.smartpoint.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.smartpoint.entity.Contact;
import com.smartpoint.entity.FileInfo;
import com.smartpoint.marquee.R;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by gjz on 9/4/16.
 */
public class VideoShowAdapter extends RecyclerView.Adapter<VideoShowAdapter.ContactsViewHolder> {

    private List<FileInfo> fileInfos;
    private int layoutId;
    private MultiTransformation multi;
    public VideoShowAdapter(List<FileInfo> fileInfos, int layoutId) {
        this.fileInfos = fileInfos;
        this.layoutId = layoutId;
        multi = new MultiTransformation(
                new RoundedCornersTransformation(20,10,RoundedCornersTransformation.CornerType.ALL));
    }

    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, final int position) {
        FileInfo fileInfo = fileInfos.get(position);
        if (!TextUtils.isEmpty(fileInfo.getAlbum())){
            holder.textView.setText(fileInfo.getAlbum());
        }else {
            holder.textView.setText("未知");
        }
        if (!TextUtils.isEmpty(fileInfo.getPath())){
            Glide.with(holder.imageView.getContext()).load(fileInfo.getPath())
                    .apply(bitmapTransform(multi))
                    .into(holder.imageView);
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
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
        return fileInfos.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView,play;
        public LinearLayout linearLayout;
        public ContactsViewHolder(View itemView) {
            super(itemView);
            textView =  itemView.findViewById(R.id.textView);
            imageView =  itemView.findViewById(R.id.imageView);
            play =  itemView.findViewById(R.id.play);
            linearLayout =  itemView.findViewById(R.id.linearLayout);
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