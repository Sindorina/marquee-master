package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.widget.TextView;

import com.smartpoint.marquee.R;

import io.rmiri.skeleton.SkeletonGroup;

/**
 * Created by Administrator on 2018/12/3
 * 邮箱 18780569202@163.com
 */
public class SkeletonViewActivity extends AppCompatActivity{
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SkeletonViewActivity.class);
        activity.startActivity(intent);
    }
    SkeletonGroup skeletonGroup;
    AppCompatImageView imageView;
    TextView textView2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skeleton_view);
        skeletonGroup = findViewById(R.id.skeletonGroup);
        imageView = findViewById(R.id.image);
        textView2 = findViewById(R.id.textView2);
        skeletonGroup.setAutoPlay(true);
        skeletonGroup.setShowSkeleton(true);
        skeletonGroup.startAnimation();
        skeletonGroup.setSkeletonListener(new SkeletonGroup.SkeletonListener() {
            @Override
            public void onStartAnimation() {
                Log.e("SkeletonActivity","onStartAnimation");
            }

            @Override
            public void onFinishAnimation() {
                Log.e("SkeletonActivity","onFinishAnimation");
            }
        });
        skeletonGroup.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView2.setText("发生发生发发发十多个第三方个电饭锅电饭锅");
                imageView.setImageResource(R.mipmap.bg);
                skeletonGroup.finishAnimation();
            }
        },2000);
    }
}
