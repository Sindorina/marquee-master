package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.smartpoint.marquee.AdminReceiver;
import com.smartpoint.marquee.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/5/8
 * 邮箱 18780569202@163.com
 */
public class ScreenLockActivity extends AppCompatActivity implements View.OnClickListener{
    DevicePolicyManager policyManager;
    ComponentName componentName;
    PowerManager.WakeLock wakeLock;
    Button active,unactive;
    int hour,minute,second;//时分秒
    Timer timer;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ScreenLockActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_lock);
        active = findViewById(R.id.active);
        unactive = findViewById(R.id.unactive);
        active.setOnClickListener(this);
        unactive.setOnClickListener(this);
        boolean isSh = isArriveTime(11,04,00);
        timer = new Timer();
        Date date = new Date(System.currentTimeMillis()+1000*60);
        timer.scheduleAtFixedRate(new MyTimerTask(),date,1000*60);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            lightTest(-1.0f);
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void init(){
        //获取设备管理服务
        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        //AdminReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(this, AdminReceiver.class);
    }
    /**
     * 激活设备管理权限
     * 成功执行激活时，DeviceAdminReceiver中的 onEnabled 会响应
     */
    private void activeManage() {
        // 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        //权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);

        //描述(additional explanation)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "------ 其他描述 ------");

        startActivityForResult(intent, 0);
    }
    /**
     * 禁用设备管理权限
     * 成功执行禁用时，DeviceAdminReceiver中的 onDisabled 会响应
     */
    private void unActiveManage() {
        Log.d("ScreenLockActivity","------ unActiveManage ------");
        boolean active = policyManager.isAdminActive(componentName);
        if (active) {
            policyManager.removeActiveAdmin(componentName);
        }
    }
    //屏幕待机
    private void lock(){
//        if (wakeLock!=null){
//            wakeLock.release();
//            wakeLock = null;
//            Toast.makeText(this,"屏幕待机",Toast.LENGTH_SHORT).show();
//        }
//        try {
//            Class mPowerManager = Class.forName("PowerManager");
//            Object obj = mPowerManager.getConstructor().newInstance();
//            Method methods = mPowerManager.getDeclaredMethod("goToSleep",long.class);
//            methods.setAccessible(true);
//            methods.invoke(obj,System.currentTimeMillis());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

    }
    //点亮屏幕
    private void release(){
        // 电源管理
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"TAG");
        wakeLock.acquire();
        Toast.makeText(this,"点亮屏幕",Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wakeLock.setReferenceCounted(false);
                wakeLock.release();
            }
        }, 1000 * 10);
//        Class mPowerManager = null;
//        try {
//            mPowerManager = Class.forName("PowerManager");
//            Object obj = mPowerManager.getConstructor().newInstance();
//            Method methods = mPowerManager.getDeclaredMethod("wakeUp",long.class);
//            methods.setAccessible(true);
//            methods.invoke(obj,System.currentTimeMillis());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.unactive://sleep
                lock();
                break;
            case R.id.active://weak up
                release();
                break;
        }
    }
    private Handler handler = new Handler();
    /**
     * 屏幕亮度测试
     */
    private void lightTest(float brightness) {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) ;
        }
        window.setAttributes(lp);
    }
    private boolean isArriveTime(int hour1,int minute1,int second1){
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
        Log.e("ost","hour-->"+hour);
        Log.e("ost","minute-->"+minute);
        Log.e("ost","second-->"+second);
        if (hour==hour1&&minute==minute1&&second==second1){
            return true;
        }else {
            return false;
        }
    }
    private class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            handler1.sendEmptyMessage(20);
        }
    }
    private Handler handler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what==20)
                lightTest(0.0000000000001f);
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
            timer = null;
        }
    }
}
