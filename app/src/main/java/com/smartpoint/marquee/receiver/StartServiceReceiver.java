package com.smartpoint.marquee.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;

public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 监听wifi的打开与关闭，与wifi的连接无关
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            if (wifiState == WifiManager.WIFI_STATE_DISABLED) {//wifi关闭
                Log.d("netstatus", "wifi已关闭");
            } else if (wifiState == WifiManager.WIFI_STATE_ENABLED) {//wifi开启
                Log.d("netstatus", "wifi已开启");
            } else if (wifiState == WifiManager.WIFI_STATE_ENABLING) {//wifi开启中
                Log.d("netstatus", "wifi开启中");
            } else if (wifiState == WifiManager.WIFI_STATE_DISABLING) {//wifi关闭中
                Log.d("netstatus", "wifi关闭中");
            }
        }
        // 监听wifi的连接状态即是否连上了一个有效无线路由
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (parcelableExtra != null) {
                Log.d("netstatus", "wifi parcelableExtra不为空");
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {//已连接网络
                    Log.d("netstatus", "wifi 已连接网络");
                    if (networkInfo.isAvailable()) {//并且网络可用
                        Log.d("netstatus", "wifi 已连接网络，并且可用");
                    } else {//并且网络不可用
                        Log.d("netstatus", "wifi 已连接网络，但不可用");
                    }
                } else {//网络未连接
                    Log.d("netstatus", "wifi 未连接网络");
                }
            } else {
                Log.d("netstatus", "wifi parcelableExtra为空");
            }
        }
        // 监听网络连接，总网络判断，即包括wifi和移动网络的监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            //连上的网络类型判断：wifi还是移动网络
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.d("netstatus", "总网络 连接的是wifi网络");
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                Log.d("netstatus", "总网络 连接的是移动网络");
            }
            //具体连接状态判断
            checkNetworkStatus(networkInfo);
        }
    }

    private void checkNetworkStatus(NetworkInfo networkInfo) {
        if (networkInfo != null) {
            Log.d("netstatus", "总网络 info非空");
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {//已连接网络
                Log.d("netstatus", "总网络 已连接网络");
                if (networkInfo.isAvailable()) {//并且网络可用
                    Log.d("netstatus", "总网络 已连接网络，并且可用");
                } else {//并且网络不可用
                    Log.d("netstatus", "总网络 已连接网络，但不可用");
                }
            } else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {//网络未连接
                Log.d("netstatus", "总网络 未连接网络");
            }
        } else {
            Log.d("netstatus", "总网络 info为空");
        }
    }
}