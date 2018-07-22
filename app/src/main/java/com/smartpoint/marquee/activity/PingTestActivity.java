package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.smartpoint.marquee.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/6/21
 * 邮箱 18780569202@163.com
 */
public class PingTestActivity extends AppCompatActivity {
    private static final int NET_WORK_SUCCESS = 200;
    private static final int NET_WORK_FAIL = 100;
    TextView ip, pingTest, result;
    String address = "111.13.100.92";
    String address1 = "93.123.23.1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_test);
        result = findViewById(R.id.result);
        timer.schedule(new CheckNetWorkStateTimerTask(),0,1000*10);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, PingTestActivity.class);
        activity.startActivity(intent);
    }



    /**
     * 检查网络
     */
    public void checkNetWorkState(){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ping "+address1);
            InputStreamReader r = new InputStreamReader(process.getInputStream());
            LineNumberReader returnData = new LineNumberReader(r);
            String returnMsg="";
            String line = "";
            int count = 0;//网络连接次数
            int successCount = 0;//网络连接成功个数
            while (count<3) {
                line = returnData.readLine();
                Log.d("OST","返回内容-->"+line);
                if (TextUtils.isEmpty(line)){
                    res[count] = "";
                }else {
                    res[count] = line;
                }
                count++;
            }
            for (String str:res){
                if (TextUtils.isEmpty(str)||str.contains("100% loss")){
                    Log.d("OST","网络连接不顺畅");
                    handler.sendEmptyMessage(NET_WORK_FAIL);
                    break;
                }else {
                    successCount++;
                }
            }
            if (successCount==3){
                Log.d("OST","网络连接顺畅");
                handler.sendEmptyMessage(NET_WORK_SUCCESS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    String res[] = new String[3];
    Timer timer = new Timer();
    public class CheckNetWorkStateTimerTask extends TimerTask{

        @Override
        public void run() {
            checkNetWorkState();
        }
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case NET_WORK_SUCCESS://成功
                    result.setText("网络连接顺畅");
                    break;
                case NET_WORK_FAIL://失败
                    result.setText("网络连接不顺畅");
                    break;
            }
            return false;
        }
    });
}
