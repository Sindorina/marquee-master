package com.smartpoint.marquee;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AdminReceiver extends DeviceAdminReceiver {
    public String TAG = "AdminReceiver";
    @Override  
    public DevicePolicyManager getManager(Context context) {  
        Log.d(TAG,"------" + "getManager" + "------");
        return super.getManager(context);  
    }  
    @Override  
    public ComponentName getWho(Context context) {  
        Log.d(TAG,"------" + "getWho" + "------");
        return super.getWho(context);  
    }  
      
    /** 
     * 禁用 
     */  
    @Override  
    public void onDisabled(Context context, Intent intent) {  
        Log.d(TAG,"------" + "onDisabled" + "------");
          
        Toast.makeText(context, "禁用设备管理", Toast.LENGTH_SHORT).show();  
          
        super.onDisabled(context, intent);  
    }  
    @Override  
    public CharSequence onDisableRequested(Context context, Intent intent) {  
        Log.d(TAG,"------" + "onDisableRequested" + "------");
        return super.onDisableRequested(context, intent);  
    }  
      
    /** 
     * 激活 
     */  
    @Override  
    public void onEnabled(Context context, Intent intent) {  
        Log.d(TAG,"------" + "onEnabled" + "------");
          
        Toast.makeText(context, "启动设备管理", Toast.LENGTH_SHORT).show();  
          
        super.onEnabled(context, intent);  
    }  
    @Override  
    public void onPasswordChanged(Context context, Intent intent) {  
        Log.d(TAG,"------" + "onPasswordChanged" + "------");
        super.onPasswordChanged(context, intent);  
    }  
    @Override  
    public void onPasswordFailed(Context context, Intent intent) {  
        Log.d(TAG,"------" + "onPasswordFailed" + "------");
        super.onPasswordFailed(context, intent);  
    }  
    @Override  
    public void onPasswordSucceeded(Context context, Intent intent) {  
        Log.d(TAG,"------" + "onPasswordSucceeded" + "------");
        super.onPasswordSucceeded(context, intent);  
    }  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        Log.d(TAG,"------" + "onReceive" + "------");
          
        super.onReceive(context, intent);  
    }  
    @Override  
    public IBinder peekService(Context myContext, Intent service) {  
        Log.d(TAG,"------" + "peekService" + "------");
        return super.peekService(myContext, service);  
    }  
      
}  