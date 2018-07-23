package com.smartpoint.util;

import android.util.Log;

import com.smartpoint.marquee.BuildConfig;

/**
 * Created by Administrator on 2018/6/13
 * 邮箱 18780569202@163.com
 */
public class LogUtils {
    public static void logI(String TAG,String result){
        if (BuildConfig.DEBUG){
            Log.i(TAG,result);
        }
    }
    public static void logD(String TAG,String result){
        if (BuildConfig.DEBUG){
            Log.d(TAG,result);
        }
    }
    public static void logE(String TAG,String result){
        if (BuildConfig.DEBUG){
            Log.e(TAG,result);
        }
    }
}
