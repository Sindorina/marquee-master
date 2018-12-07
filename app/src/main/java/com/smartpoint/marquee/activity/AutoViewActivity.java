package com.smartpoint.marquee.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.smartpoint.adapter.AutoViewAdapter;
import com.smartpoint.marquee.R;
import com.smartpoint.util.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/12/6
 * 邮箱 18780569202@163.com
 */
public class AutoViewActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    String TAG = "AutoViewActivity";
    private AutoViewAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_view);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayout.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
        adapter = new AutoViewAdapter(null,R.layout.auto_view_item);
        recyclerView.setAdapter(adapter);
        viewTimer = new Timer(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    x = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    LogUtils.logE(TAG,"lastItemPosition-->"+ x);
                    LogUtils.logE(TAG,"firstItemPosition-->"+ firstItemPosition);
                }

            }
        });
        viewTimer.schedule(new ViewTimerTask(),1000,2000);
    }
    Timer viewTimer;
    int x = 0;
    private class ViewTimerTask extends TimerTask{

        @Override
        public void run() {
            AutoViewActivity.this.runOnUiThread(() -> {
                if (x < 20){
                    recyclerView.smoothScrollToPosition(x);
                    x++;
                }else {
                    if (viewTimer != null){
                        viewTimer.cancel();
                        viewTimer = null;
                    }
                }
//                LogUtils.logE(TAG,"recycle宽度-->"+ recyclerView.computeHorizontalScrollRange());
//                dx += 10;
//                LogUtils.logE(TAG,"移动的宽度-->"+ dx);
//                recyclerView.smoothScrollBy(dx,0);
            });
        }
    }
}
