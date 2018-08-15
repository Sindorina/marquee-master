package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/5/11
 * 邮箱 18780569202@163.com
 */
public class CountDownTimerActivity extends BaseActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, CountDownTimerActivity.class);
        activity.startActivity(intent);
    }

    private static TextView textView;
    static long time;
    static long cunrrentTime;
    static long targetTime;
    static Timer timer;

    @Override
    public int getContentViewId() {
        return R.layout.activity_count_down_timer;
    }

    @Override
    public void beforeInitView() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void initView() {
        textView = findViewById(R.id.textView);
        cunrrentTime = System.currentTimeMillis()/1000;
        targetTime = getTime(getTargetTime())/1000;
        time = targetTime - cunrrentTime;
        if (time<0){
            Toast.makeText(this,"目标时间已过",Toast.LENGTH_SHORT).show();
            textView.setText("目标时间已过");
            return;
        }
        if (time == 0) {
            Toast.makeText(this, "时间转换错误", Toast.LENGTH_SHORT).show();
            textView.setText("时间转换错误");
            return;
        }
        timer = new Timer();
        timer.schedule(new MyTimerTask(),1000,1000);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(2);
        }
    }

    private final MyHandler handler = new MyHandler(this);

    // TODO: 2018/5/11 handler内存溢出警告
    private static class MyHandler extends Handler {
        private final WeakReference<CountDownTimerActivity> mActivity;

        public MyHandler(CountDownTimerActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                time--;
                if (time==0){
                    if (timer!=null){
                        timer.cancel();
                        timer=null;
                    }
                }else {
                    textView.setText(time + "");
                }
            }
        }
    }

    private long getTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
            timer=null;
        }
        if (handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
    }
    private String getTargetTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year+"-"+getStringDoubleSet(month)+"-"+getStringDoubleSet(day)+" 18:30";
    }
    private String getStringDoubleSet(int i){
        if (i<10){
            return "0"+i;
        }else {
            return ""+i;
        }
    }
}
