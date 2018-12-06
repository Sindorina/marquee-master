package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.smartpoint.marquee.R;
import com.smartpoint.marquee.SkeletonListItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/3
 * 邮箱 18780569202@163.com
 */
public class SkeletonListViewActivity extends AppCompatActivity{
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SkeletonListViewActivity.class);
        activity.startActivity(intent);
    }
    ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skeleton_listview);
        listView =  findViewById(R.id.recyclerView);
        List<String> list = new ArrayList<>();
        for (int i = 0 ; i < 10 ;i++){
            list.add("测试标题"+i);
        }
        listView.setAdapter(new SkeletonListItemAdapter(this,list));
    }
}
