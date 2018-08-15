package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.view.ProgressWebView;
import com.tencent.smtt.sdk.WebSettings;

/**
 * Created by Administrator on 2018/6/13
 * 邮箱 18780569202@163.com
 */
public class WebAppActivity extends BaseActivity {
    private ProgressWebView webView;
    private String url = "http://172.16.15.96:9999/jquery.html";

    @Override
    public int getContentViewId() {
        return R.layout.activity_tencent_webview;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        webView = findViewById(R.id.tencentWebView);
        initWeb();
        webView.loadUrl(url);
    }

    @Override
    public void initData() {

    }

    //初始化webView
    private void initWeb() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, WebAppActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
