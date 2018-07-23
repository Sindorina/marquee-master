package com.smartpoint.marquee.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.smartpoint.marquee.R;
import com.smartpoint.vguang.Vbar;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/5/2
 * 邮箱 18780569202@163.com
 */
public class DecodeActivity extends AppCompatActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, DecodeActivity.class);
        activity.startActivity(intent);
    }

    Vbar b = new Vbar();
    boolean state = false;
    private TextView textView;
    Thread t;
    Timer timer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
        //checkPermission();
        timer = new Timer();
        VbarStartTask task = new VbarStartTask();
        timer.schedule(task,1000*15,1000*999999);
        textView = findViewById(R.id.textView);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state) {
                    t = new Thread() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            super.run();
                            final String str = b.getResultsingle();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (str != null) {
                                        boolean beepstate = b.vbarBeep((byte) 1);
                                        Log.e("ost","状态-->"+beepstate);
                                        System.out.println(str.trim());
                                        if (checkUrl(str.trim())){
                                            TencentWebViewActivity.start(DecodeActivity.this,str.trim());
                                        }else {
                                            textView.append(str.trim() + "\r\n");
                                        }
                                    }
                                }
                            });
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    };
                    t.start();
                }

            }
        });
    }

    //权限检查
    private void checkPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RESTART_PACKAGES,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.INSTALL_PACKAGES,
                Manifest.permission.DISABLE_KEYGUARD,
                Manifest.permission.READ_LOGS).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    Log.e("oss", "已授权");
                    Log.e("oss", "已授权");
                } else if (permission.shouldShowRequestPermissionRationale) {
                    return;
                } else {
                    return;
                }
            }
        });
    }

    private boolean checkUrl(String str) {
        String pattern = "^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }

    private void openWeb(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private class VbarStartTask extends TimerTask{

        @Override
        public void run() {
            if (timer!=null){
                timer.cancel();
                timer = null;
            }
            state = b.vbarOpen();
            b.vbarLight(true);
            b.vbarBeep((byte) 2);
            b.vbarAddSymbolType((byte) 1);
        }
    }
}
