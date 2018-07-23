package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartpoint.fingerRecog.CryptoObjectHelper;
import com.smartpoint.fingerRecog.MyAuthCallback;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.util.LogUtils;

import zwh.com.lib.RxFingerPrinter;

/**
 * Created by Administrator on 2018/7/19
 * 邮箱 18780569202@163.com
 */
public class FingerprintsRecogActivity extends BaseActivity {
    private static final String FLAG = "FingerprintsRecogActivity";
    public static final int MSG_AUTH_ERROR = 500;
    public static final int MSG_AUTH_HELP = 400;
    public static final int MSG_AUTH_SUCCESS = 200;
    public static final int MSG_AUTH_FAILED = 404;
    private FingerprintManagerCompat fingerprintManager = null;
    private KeyguardManager keyguardManager;
    private Button mCancelBtn,mStartBtn;
    private TextView textView;
    private RxFingerPrinter rxFingerPrinter;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, FingerprintsRecogActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.acitivity_fingerprints_recog;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void beforeInitView() {
        initFingerprintManager();
    }
    @Override
    public void initView() {
        mStartBtn = findViewByIdNoCast(R.id.btn1);
        mStartBtn.setOnClickListener(this);
        mCancelBtn = findViewByIdNoCast(R.id.btn2);
        mCancelBtn.setOnClickListener(this);
        textView = findViewByIdNoCast(R.id.textView_info);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initData() {
        findViewByIdNoCast(R.id.btn1).setOnClickListener(this);
        findViewByIdNoCast(R.id.btn2).setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1://开始识别
                if (!fingerprintManager.isHardwareDetected()) {
                    Toast.makeText(FingerprintsRecogActivity.this, "手机硬件不支持指纹识别", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!keyguardManager.isKeyguardSecure()) {
                    Toast.makeText(FingerprintsRecogActivity.this, "请开启屏幕锁", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    Toast.makeText(FingerprintsRecogActivity.this, "请录入指纹    ", Toast.LENGTH_SHORT).show();
                    if (fingerprintManager.isHardwareDetected()) {
                        Toast.makeText(FingerprintsRecogActivity.this, "手机硬件不支持指纹识别", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!keyguardManager.isKeyguardSecure()) {
                        Toast.makeText(FingerprintsRecogActivity.this, "请开启屏幕锁", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        Toast.makeText(FingerprintsRecogActivity.this, "请录入指纹    ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    CryptoObjectHelper cryptoObjectHelper = null;
                    try {
                        cryptoObjectHelper = new CryptoObjectHelper();
                        fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), 0,
                                cancellationSignal, callback, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn2://取消识别
                cancellationSignal.cancel();
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initFingerprintManager () {
        try {
            fingerprintManager = FingerprintManagerCompat.from(this);
            keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        } catch (Throwable e) {
            Log.e("ost", "FingerprintManager不存在!");
        }
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            LogUtils.logE(FLAG, "Msg.What-->" + msg.what);
            switch (msg.what) {
                //识别成功
                case MSG_AUTH_SUCCESS:
                    textView.setText("识别成功!");
                    mCancelBtn.setEnabled(false);
                    mStartBtn.setEnabled(true);
                    cancellationSignal = null;
                    break;
                //识别失败
                case MSG_AUTH_FAILED:
                    textView.setText("识别失败!");
                    mCancelBtn.setEnabled(false);
                    mStartBtn.setEnabled(true);
                    cancellationSignal = null;
                    break;
                //识别错误
                case MSG_AUTH_ERROR:
                    textView.setText("识别错误!");
                    break;
                //帮助
                case MSG_AUTH_HELP:
                    textView.setText("帮助!");
                    break;
            }
            return false;
        }
    });
    private MyAuthCallback callback = new MyAuthCallback(handler);
    private CancellationSignal cancellationSignal = new CancellationSignal();

}
