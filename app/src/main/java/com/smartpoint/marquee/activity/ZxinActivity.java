package com.smartpoint.marquee.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.smartpoint.marquee.base.BaseActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ZxinActivity extends BaseActivity {
    private TextView textView;
    private static final int SCANNING_CODE = 1000;
    private String testUrl = "http://172.16.15.96:9999/jquery.html";
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ZxinActivity.class);
        activity.startActivity(intent);
    }
    ImageView iv;
    @Override
    public int getContentViewId() {
        return R.layout.activity_zxin;
    }

    @Override
    public void beforeInitView() {
        checkPermission();
    }

    @Override
    public void initView() {
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
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView.getText().toString().trim().startsWith("http")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ZxinActivity.this);
                    builder.setTitle("是否要跳转到目标网页").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TencentWebViewActivity.start(ZxinActivity.this,textView.getText().toString().trim());
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ZxinActivity.this,"已取消加载目标网页",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.create().show();
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    //生成二维码不带图片
    public void generate(View view) throws WriterException {
        Bitmap bitmap = BitmapUtil.createQRCode(testUrl,256);
        iv.setImageBitmap(bitmap);
    }
    //生成二维码带logo
    public void createImg(){
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.flower);
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
                textView.setText(content);
                if (content.startsWith("http")){
                    textView.setTextColor(Color.GREEN);
                    textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                    textView.getPaint().setAntiAlias(true);//抗锯齿
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

    @Override
    public void onClick(View v) {

    }
}
