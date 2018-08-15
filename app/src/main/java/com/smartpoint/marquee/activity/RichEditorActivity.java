package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

/**
 * 富文本编辑
 * Created by Administrator on 2018/7/26
 * 邮箱 18780569202@163.com
 */
public class RichEditorActivity extends BaseActivity{
    private TextView textView;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, RichEditorActivity.class);
        activity.startActivity(intent);
    }
    @Override
    public int getContentViewId() {
        return R.layout.activity_rich_editor;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        textView = findViewByIdNoCast(R.id.textView);
        SpannableString spannableString = new SpannableString("今天天气不错");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(RichEditorActivity.this,"嗯 天气真不错",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.parseColor("#00FF00"));
                ds.setUnderlineText(true);
                ds.bgColor = Color.WHITE;
            }
        };
        spannableString.setSpan(clickableSpan,2,spannableString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
