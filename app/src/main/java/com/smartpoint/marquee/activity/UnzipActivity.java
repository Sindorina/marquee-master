package com.smartpoint.marquee.activity;

import android.Manifest;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.smartpoint.Decompress;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/8/14
 * 邮箱 18780569202@163.com
 */
public class UnzipActivity extends BaseActivity{
    private String zipPath;
    private TextView textView,textView2,textView3;
    private long totalFileLength;
    private long startTime,endTime;
    @Override
    public int getContentViewId() {
        return R.layout.activity_unzip;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        checkPermission();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
    private Timer unZipTimer = new Timer();
    private class UnzipTimerTask extends TimerTask {

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            new Decompress(UnzipActivity.this,new File(zipPath),unZipHandler).unzip();
            if (unZipTimer!=null){
                unZipTimer.cancel();
                unZipTimer = null;
            }
        }
    }
    //todo 新增解压判断handler
    private Handler unZipHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.obj!=null){
                long percent = (long) msg.obj;
                if (percent!=0){
                    textView2.setText("当前解压大小: "+percent);
                }
            }
            if (msg.what==90){
                textView2.setText("解压错误,请重启再试!");
            } else if (msg.what==100){
                endTime = System.currentTimeMillis();
                textView3.setText("解压完成,共耗时"+(endTime-startTime)/1000+"秒");
            }
            return false;
        }
    });

    /**
     * 获取zip文件解压缩后的大小
     * @param filePath 压缩包路径
     * @return zip文件解压缩后的大小
     */
    public static long getZipTrueSize(String filePath) {
        long size = 0;
        ZipFile f;
        try {
            f = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> en = f.entries();
            while (en.hasMoreElements()) {
                size += en.nextElement().getSize();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }
    // TODO: 2018/5/3 权限检测
    private void checkPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS)
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                            startUnzip();
                        } else if (permission.shouldShowRequestPermissionRationale) {

                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void startUnzip(){
        zipPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/unzip/688.zip";
        totalFileLength = getZipTrueSize(zipPath);
        textView.setText("总文件大小: "+totalFileLength);
        textView3.setText("解压中,请稍后...");
        unZipTimer.schedule(new UnzipTimerTask(),500);
    }
}
