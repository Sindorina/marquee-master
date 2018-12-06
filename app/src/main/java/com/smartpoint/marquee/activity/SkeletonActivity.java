package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.smartpoint.marquee.R;

/**
 * Created by Administrator on 2018/12/3
 * 邮箱 18780569202@163.com
 */
public class SkeletonActivity extends AppCompatActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SkeletonActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skeleton);
        findViewById(R.id.btn1).setOnClickListener( v -> SkeletonViewActivity.start(SkeletonActivity.this));
        findViewById(R.id.btn2).setOnClickListener( v -> SkeletonListViewActivity.start(SkeletonActivity.this));
    }
}
