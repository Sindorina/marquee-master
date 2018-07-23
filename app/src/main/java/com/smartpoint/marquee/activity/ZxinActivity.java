package com.smartpoint.marquee.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.smartpoint.marquee.BitmapUtil;
import com.smartpoint.marquee.R;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ZxinActivity extends AppCompatActivity {
    private TextView textView;
    private static final int SCANNING_CODE = 1000;
    private String testUrl = "http://172.16.15.96:9999/jquery.html";
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ZxinActivity.class);
        activity.startActivity(intent);
    }
    ImageView iv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_zxin);
        iv = findViewById(R.id.iv);
        textView = findViewById(R.id.textView);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createImg();
            }
        });
        findViewById(R.id.parseCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parse();
            }
        });
    }
    //生成二维码不带图片
    public void generate(View view) throws WriterException {
        Bitmap bitmap = BitmapUtil.createQRCode(testUrl,256);
        iv.setImageBitmap(bitmap);
    }
    //生成二维码带logo
    public void createImg(){
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap bitmap = null;
        bitmap = BitmapUtil.createQRImage(testUrl,256,logoBitmap);
        iv.setImageBitmap(bitmap);
    }
    //解析二维码
    public void parse(){
        canningMethod();
    }
    //点击跳转到扫描界面
    public void canningMethod() {
        Intent intent = new Intent(ZxinActivity.this,
                CaptureActivity.class);
        startActivityForResult(intent, SCANNING_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == SCANNING_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(CodeUtils.RESULT_STRING);
                textView.setText("扫描结果： " + content);
                if (content.startsWith("http")){
                    TencentWebViewActivity.start(ZxinActivity.this,content);
                }
            }
        }
    }
    private void checkPermission(){
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.CAMERA).subscribe(new Observer<Permission>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Permission permission) {
                if (permission.granted){
                }else if (permission.shouldShowRequestPermissionRationale){
                    Toast.makeText(ZxinActivity.this,"权限未开启,请重启app",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Toast.makeText(ZxinActivity.this,"权限未开启,请手动开启权限并重启app",Toast.LENGTH_SHORT).show();
                    return;
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
