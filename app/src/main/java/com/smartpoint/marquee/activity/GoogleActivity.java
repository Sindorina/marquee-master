package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

/**
 * Created by Administrator on 2018/5/15
 * 邮箱 18780569202@163.com
 */
public class GoogleActivity extends BaseActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, GoogleActivity.class);
        activity.startActivity(intent);
    }
    private EditText editText;
    private TextView textView;
    private Button btn;
    @Override
    public int getContentViewId() {
        return R.layout.activity_google;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = editText.getText().toString();
                if (!TextUtils.isEmpty(result)){
                    textView.setText(splitString(result));
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    private String splitString(String str){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<str.length();i++){
            char s = str.charAt(i);
            stringBuilder.append(s);
            stringBuilder.append("&&&&");
        }
        return stringBuilder.delete(stringBuilder.length()-4,stringBuilder.length()).toString();
    }

    @Override
    public void onClick(View v) {

    }
}
