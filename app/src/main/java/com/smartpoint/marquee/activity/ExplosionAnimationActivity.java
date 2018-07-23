package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartpoint.explosionfield.ExplosionField;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

/**
 * Created by Administrator on 2018/7/17
 * 邮箱 18780569202@163.com
 */
public class ExplosionAnimationActivity extends BaseActivity{
    private ImageView imageView;
    private ExplosionField explosionField;
    private Button btn;
    private int width,height;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ExplosionAnimationActivity.class);
        activity.startActivity(intent);
    }
    @Override
    public int getContentViewId() {
        return R.layout.activity_explosion_animation;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        btn = findViewByIdNoCast(R.id.btn);
        btn.setOnClickListener(this);
        imageView = findViewByIdNoCast(R.id.imageView);
        width = imageView.getWidth();
        height = imageView.getHeight();
        imageView.setOnClickListener(this);
        explosionField = ExplosionField.attach2Window(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView:
                explosionField.explode(imageView);
                break;
            case R.id.textView:
                imageView.setAlpha(1.0f);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                lp.width = width;
                lp.height = height;
                imageView.setLayoutParams(lp);
                break;
        }
    }
}
