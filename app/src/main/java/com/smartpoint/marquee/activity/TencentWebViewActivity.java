package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.view.ProgressWebView;
import com.tencent.smtt.sdk.WebSettings;

public class TencentWebViewActivity extends BaseActivity {
    private static String webUrl ;
    private ProgressWebView webView;
    private TextView title;
    private String testUrl1 = "https://signposs1.oss-cn-shenzhen.aliyuncs.com/demo/test/index.html";
    private String testURL2 = "https://project.signp.cn/entrance/main?s=LgyoCS83s+lmEK1O/FGkcHj/HzQrrxsznWkNZNqf2r4=&f=7V3uqkwei7Z0WQADK/e50MCTY+vQt/gcwnN9hbV0IMI=&tpl=21.5";
    private String testUrl = "https://ifs.cloudindoormap.com/?floorid=BCD5A746-CFE0-448F-A7C8-08646A5B96F6&mark=AREA-20170918-14:17:57:23-6262&pointmove=undefined&from=singlemessage";
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, TencentWebViewActivity.class);
        activity.startActivity(intent);
    }
    public static void start(Activity activity,String url) {
        webUrl = url;
        Intent intent = new Intent(activity, TencentWebViewActivity.class);
        activity.startActivity(intent);
    }

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
        //webView.addJavascriptInterface(new AndroidJavaScript(), "test");
        //webView.loadUrl(testUrl1);
        if (TextUtils.isEmpty(webUrl)){
            webView.addJavascriptInterface(new AndroidJavaScript(),"AndroidJavaScript");
            webView.loadUrl("file:///android_asset/js2.html");
        }else {
            webView.loadUrl(webUrl);
        }
        findViewByIdNoCast(R.id.back).setOnClickListener(this);
        title = findViewByIdNoCast(R.id.title);
        title.setText("网页浏览");
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    public class AndroidJavaScript{
        @JavascriptInterface
        public void hello(String msg){
            Log.e("TencentWebViewActivity","数据-->"+msg);
            Log.e("TencentWebViewActivity","js调用Android方法");
        }
        @JavascriptInterface
        public void test(String msg,String tag){
            Log.e("TencentWebViewActivity","数据-->"+msg);
            Log.e("TencentWebViewActivity","数据-->"+tag);
            Log.e("TencentWebViewActivity","js调用Android方法");
        }
    }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (webView.canGoBack()){
                webView.goBack();
            }
        }
        return true;
    }
}
