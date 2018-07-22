package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.smartpoint.marquee.R;

/**
 * Created by Administrator on 2018/6/5
 * 邮箱 18780569202@163.com
 */
public class SvgActivity extends AppCompatActivity {
    public static void start(Activity activity){
        Intent intent = new Intent(activity,SvgActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);
    }
}
