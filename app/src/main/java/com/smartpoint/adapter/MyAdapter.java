package com.smartpoint.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartpoint.marquee.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<String> list = new ArrayList<>();

    public MyAdapter() {
    }

    public List<String> getList() {
        return list;
    }

    public void addData(List<String> list1) {
        list.addAll(list1);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String res = list.get(position);
        MyViewHolder holder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_item,parent,false);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(res)){
            holder.textView.setText(res);
        }else {
            holder.textView.setText("未知");
        }
        return convertView;
    }
    public class MyViewHolder{
        TextView textView;
        public MyViewHolder(View view) {
            textView = view.findViewById(R.id.textView);
        }
    }
}