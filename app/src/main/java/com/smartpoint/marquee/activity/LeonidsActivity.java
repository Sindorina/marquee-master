package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.plattysoft.leonids.ParticleSystem;
import com.skyfishjy.library.RippleBackground;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

/**
 * Created by Administrator on 2018/7/17
 * 邮箱 18780569202@163.com
 */
public class LeonidsActivity extends BaseActivity{
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LeonidsActivity.class);
        activity.startActivity(intent);
    }
    private Button btn1,btn2;
    @Override
    public int getContentViewId() {
        return R.layout.activity_leonids;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        btn1 = findViewByIdNoCast(R.id.btn1);
        btn2 = findViewByIdNoCast(R.id.btn2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ParticleSystem(LeonidsActivity.this, 1000, R.mipmap.star_pink, 3000)
                        .setSpeedModuleAndAngleRange(0.05f, 0.2f, 0, 360)
                        .setRotationSpeed(30)
                        .setAcceleration(0, 90)
                        .oneShot(btn1, 200);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ParticleSystem(LeonidsActivity.this, 1000, R.mipmap.flower, 10000)
                        .setSpeedModuleAndAngleRange(0.05f, 0.2f, 0, 90)
                        .setRotationSpeed(60)
                        .setAcceleration(0.00005f, 90)
                        .emit(0, -100, 30, 10000);
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
