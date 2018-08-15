package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

/**
 * Created by Administrator on 2018/6/14
 * 邮箱 18780569202@163.com
 */
public class FontActivity extends BaseActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_font;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, FontActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
