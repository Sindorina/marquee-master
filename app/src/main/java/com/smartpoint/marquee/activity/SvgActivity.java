package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.pixplicity.sharp.Sharp;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

/**
 * Created by Administrator on 2018/6/5
 * 邮箱 18780569202@163.com
 */
public class SvgActivity extends BaseActivity {
    public static void start(Activity activity){
        Intent intent = new Intent(activity,SvgActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_svg;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        Sharp.loadAsset(getAssets(), "shopping.svg").into(findViewById(R.id.photoView));
//        AppCompatImageView imageView2 = findViewByIdNoCast(R.id.imageView2);
//        imageView2.setImageResource();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
