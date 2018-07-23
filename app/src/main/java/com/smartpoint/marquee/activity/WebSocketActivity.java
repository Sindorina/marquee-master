package com.smartpoint.marquee.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.util.LogUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/7/4
 * 邮箱 18780569202@163.com
 *
 */
public class WebSocketActivity extends BaseActivity{
    private WebView webView;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, WebSocketActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_websocket;
    }

    @Override
    public void beforeInitView() {
        requestPermission();
    }

    @Override
    public void initView() {
        openWebSocket();
        webView = findViewById(R.id.webView);
        initWeb();
        //webView.loadUrl("http://172.16.15.95:5000");
       // webView.loadUrl("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/TODirection/html/index.html");
        webView.loadUrl("http://127.0.0.1:8080/index.html");
    }

    @Override
    public void initData() {

    }

    /**
     * 开启websocket
     */
    public void openWebSocket(){
        AsyncHttpServer server = new AsyncHttpServer();
        final List<WebSocket> _sockets = new ArrayList<>();
        server.get("/", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                String uri = request.getPath();
                LogUtils.logE("WebSocketActivity","uri--->"+uri);
                try {
                    if (uri!=null&&!TextUtils.isEmpty(uri.trim())){
                        FileInputStream is = new FileInputStream(Environment.getExternalStorageDirectory() + "/TODirection/html/" + uri);
                        response.sendStream(is,is.available());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //response.send("Hello!!!");
            }
        });
        // listen on port 5000
        server.listen(8080);
        // browsing http://localhost:5000 will return Hello!!!
    }

    @Override
    public void onClick(View v) {

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
    private void requestPermission(){
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Observer<Permission>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Permission permission) {
                if (permission.granted){
                }else if (permission.shouldShowRequestPermissionRationale){
                    Toast.makeText(WebSocketActivity.this,"权限未开启,请重启app",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(WebSocketActivity.this,"权限未开启,请手动开启权限并重启app",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
