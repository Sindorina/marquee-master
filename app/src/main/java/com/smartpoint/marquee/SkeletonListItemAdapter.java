package com.smartpoint.marquee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.rmiri.skeleton.SkeletonGroup;

public class SkeletonListItemAdapter extends BaseAdapter {

    private List<String> objects;
    private Context context;
    private LayoutInflater layoutInflater;

    public SkeletonListItemAdapter(Context context,List<String> objects) {
        this.context = context;
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects == null ? 0 : objects.size();
    }

    @Override
    public String getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.skeleton_list_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(String object, ViewHolder holder) {
        //TODO implement
        holder.textViewTitle.setText(object);
        holder.imageView.setImageResource(R.mipmap.bg);
        holder.skeletonGroup.postDelayed(() -> holder.skeletonGroup.finishAnimation(),500);
    }

    protected class ViewHolder {
        private TextView textViewTitle;
        private ImageView imageView;
        private SkeletonGroup skeletonGroup;
        public ViewHolder(View view) {
            textViewTitle = (TextView) view.findViewById(R.id.textView_title);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            skeletonGroup = view.findViewById(R.id.skeletonGroup);
        }
    }
}
