package com.smartpoint;

import android.app.Activity;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.uuzuche.lib_zxing.ZApplication;

import org.litepal.LitePal;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends ZApplication {
    private Activity mShowActivity = null;//当前显示activity
    public Activity getShowActivity() {
        return mShowActivity;
    }

    public void setShowActivity(Activity mShowActivity) {
        this.mShowActivity = mShowActivity;
    }
    @Override
    public void onCreate() {
        //SpeechUtility.createUtility(this, SpeechConstant.APPID +"="+ Constant.APP_KEY);
        super.onCreate();
        //初始化X5内核
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。

            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("@@","加载内核是否成功:"+b);
            }
        });
        mBaseApp = this;
        LitePal.initialize(this);//初始化litePal
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
    private volatile static MyApplication mBaseApp;
    public static MyApplication newInstance(){
        if(mBaseApp == null){
            synchronized (MyApplication.class){
                if(mBaseApp == null){
                    mBaseApp = new MyApplication();
                }
            }
        }
        return mBaseApp;
    }


}
