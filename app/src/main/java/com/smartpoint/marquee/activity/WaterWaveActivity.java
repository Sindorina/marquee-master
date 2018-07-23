package com.smartpoint.marquee.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.skyfishjy.library.RippleBackground;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

/**
 * Created by Administrator on 2018/7/16
 * 邮箱 18780569202@163.com
 */
public class WaterWaveActivity extends BaseActivity{
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, WaterWaveActivity.class);
        activity.startActivity(intent);
    }
    @Override
    public int getContentViewId() {
        return R.layout.activity_water_wave;
    }

    @Override
    public void beforeInitView() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {
        final RippleBackground rippleBackground= findViewByIdNoCast(R.id.content);
        ImageView imageView=findViewByIdNoCast(R.id.centerImage);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    rippleBackground.startRippleAnimation();
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    rippleBackground.stopRippleAnimation();
                }
                return true;
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
