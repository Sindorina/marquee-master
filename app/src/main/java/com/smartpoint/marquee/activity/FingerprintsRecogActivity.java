package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.smartpoint.fingerRecog.CryptoObjectHelper;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

import zwh.com.lib.RxFingerPrinter;

/**
 * Created by Administrator on 2018/7/19
 * 邮箱 18780569202@163.com
 */
public class FingerprintsRecogActivity extends BaseActivity {
    public static final int MSG_AUTH_ERROR = 500;
    public static final int MSG_AUTH_HELP = 400;
    public static final int MSG_AUTH_SUCCESS = 200;
    public static final int MSG_AUTH_FAILED = 404;
    private FingerprintManagerCompat fingerprintManager = null;
    private KeyguardManager keyguardManager;
    private RxFingerPrinter rxFingerPrinter;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, FingerprintsRecogActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.acitivity_fingerprints_recog;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        findViewByIdNoCast(R.id.btn1).setOnClickListener(this);
        findViewByIdNoCast(R.id.btn2).setOnClickListener(this);
    }

    @Override
    public void initData() {
        //rxFingerPrinter = new RxFingerPrinter(this); // where this is an Activity instance
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1://开始识别
                if (fingerprintManager.isHardwareDetected()){
                    Toast.makeText(FingerprintsRecogActivity.this,"手机硬件不支持指纹识别",Toast.LENGTH_SHORT).show();
                   return;
                }
                if (!keyguardManager.isKeyguardSecure()){
                    Toast.makeText(FingerprintsRecogActivity.this,"请开启屏幕锁",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fingerprintManager.hasEnrolledFingerprints()){
                    Toast.makeText(FingerprintsRecogActivity.this,"请录入指纹    ",Toast.LENGTH_SHORT).show();
                    return;
                }
                CryptoObjectHelper cryptoObjectHelper = null;
                try {
                    cryptoObjectHelper = new CryptoObjectHelper();
//                    fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), 0,
//                            cancellationSignal, myAuthCallback, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                rxFingerPrinter.dispose();
//                rxFingerPrinter.begin().subscribe(observer);//RxfingerPrinter会自动在onPause()时暂停指纹监听，onResume()时恢复指纹监听)
//                rxFingerPrinter.addDispose(observer);//由RxfingerPrinter管理(会在onDestroy()生命周期时自动解除订阅)，已可以不调用该方法，自己解除订阅
                break;
            case R.id.btn2://取消识别
                break;
        }
    }

        @RequiresApi(api = Build.VERSION_CODES.M)
    public void initFingerprintManager() {
        try {
            fingerprintManager = FingerprintManagerCompat.from(this);
            keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            //fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        } catch (Throwable e) {
            Log.e("ost","FingerprintManager不存在!");
        }
    }
// 可以在oncreat方法中执行
//    DisposableObserver<Boolean> observer = new DisposableObserver<Boolean>() {
//
//        @Override
//        protected void onStart() {
//
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            //处理错误信息
//            if (e instanceof FPerException) {
//                Toast.makeText(FingerprintsRecogActivity.this, ((FPerException) e).getDisplayMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onComplete() {
//
//        }
//
//        @Override
//        public void onNext(Boolean aBoolean) {
//            if (aBoolean) {
//                Log.e("ost","指纹验证成功");
//                //指纹验证成功
//            } else {
//                Log.e("ost","指纹验证失败");
//                //指纹验证失败
//            }
//        }
//    };
}
