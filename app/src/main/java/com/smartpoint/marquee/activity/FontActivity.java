package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.smartpoint.marquee.R;

/**
 * Created by Administrator on 2018/6/14
 * 邮箱 18780569202@163.com
 */
public class FontActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font);
    }
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, FontActivity.class);
        activity.startActivity(intent);
    }
}
