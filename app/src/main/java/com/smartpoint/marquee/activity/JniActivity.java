package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.JniUtils;
import com.smartpoint.marquee.R;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * Created by Administrator on 2018/5/3
 * 邮箱 18780569202@163.com
 */
public class JniActivity extends AppCompatActivity {
    private Button reUploadBtn,setWifiBtn;
    private TextView tv,versionTV;
    private WebView mapView;
    private String changeStr = "changeWords";
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, JniActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);
        init();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_jni, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1://版本号初始化无网络
                setVisable(false,true,true,true);
                return true;
            case R.id.item2://版本号初始化有网络
                setVisable(false,false,true,true);
                return true;
            case R.id.item3://提示信息重新下载
                setVisable(true,false,true,false);
                return true;
            case R.id.item4://提示信息
                setVisable(false,false,true,false);
                tv.setText("本地数据不完整");
                return true;
            case R.id.item5://jni
                setVisable(false,false,true,false);
                tv.setText(JniUtils.stringFromJNI());
                return true;
            case R.id.item6://文字设置规范
                setVisable(false,false,true,false);
                tv.setText(String.format(getString(R.string.change_replace),changeStr));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private void init(){
        reUploadBtn = findViewById(R.id.reUploadBtn);
        setWifiBtn = findViewById(R.id.setWifiBtn);
        tv = findViewById(R.id.tv);
        versionTV = findViewById(R.id.versionTV);
        mapView = findViewById(R.id.mapView);
    }
    //设置显示与隐藏
    private void setVisable(boolean isReUploadBtn,boolean isSetWifiBtn,boolean isTv,boolean isVersionTV){
        if (isReUploadBtn){
            reUploadBtn.setVisibility(View.VISIBLE);
        }else {
            reUploadBtn.setVisibility(View.GONE);
        }
        if (isSetWifiBtn){
            setWifiBtn.setVisibility(View.VISIBLE);
        }else {
            setWifiBtn.setVisibility(View.GONE);
        }
        if (isTv){
            tv.setVisibility(View.VISIBLE);
        }else {
            tv.setVisibility(View.GONE);
        }
        if (isVersionTV){
            versionTV.setVisibility(View.VISIBLE);
        }else {
            versionTV.setVisibility(View.GONE);
        }
    }

}
