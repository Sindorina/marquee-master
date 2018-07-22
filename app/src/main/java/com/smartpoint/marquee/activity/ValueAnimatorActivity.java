package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.smartpoint.adapter.FindAdapter;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.view.MyDrawView;
import com.smartpoint.view.TuyaView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/18
 * 邮箱 18780569202@163.com
 */
public class ValueAnimatorActivity extends BaseActivity {
    private RelativeLayout ll;
    private MyDrawView myDrawView;
    private List<String> colorList;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ValueAnimatorActivity.class);
        activity.startActivity(intent);
    }
    @Override
    public int getContentViewId() {
        return R.layout.activity_value_animator;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        ll = findViewByIdNoCast(R.id.ll);
        myDrawView = findViewByIdNoCast(R.id.myDrawView);
        findViewByIdNoCast(R.id.btn).setOnClickListener(this);
        findViewByIdNoCast(R.id.btn2).setOnClickListener(this);
    }

    @Override
    public void initData() {
        colorList = new ArrayList<>();
        colorList.add("红色");
        colorList.add("黑色");
        colorList.add("蓝色");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                myDrawView.clear();
                break;
            case R.id.btn2:
                openPopWindow(colorList);
                break;
        }
    }
    private void initTuYaView(){
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        TuyaView tuyaView = new TuyaView(this,screenWidth,screenHeight);
        ll.addView(tuyaView);
        tuyaView.requestFocus();
        tuyaView.selectPaintSize(5);
    }
    private void initMyDrawView(){
        MyDrawView myDrawView = new MyDrawView(this);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth,screenHeight);
        myDrawView.setLayoutParams(params);
        ll.addView(myDrawView);
        myDrawView.requestFocus();
    }
    //打开弹出框
    private void openPopWindow(List<String> search){
        View view = LayoutInflater.from(this).inflate(R.layout.popwindow_voice,null,false);
        final PopupWindow popupWindow = new PopupWindow(view,getScreenSize().x-20, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(ll, Gravity.CENTER,0,0);
        list = view.findViewById(R.id.list);
        adapter = new FindAdapter(search,R.layout.find_pop_item);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setOnItemClick(new FindAdapter.OnItemClick() {
            @Override
            public void clickItem(int position) {
                switch (position){
                    case 0:
                        myDrawView.setPaintStyle(Color.RED);
                        popupWindow.dismiss();
                        break;
                    case 1:
                        myDrawView.setPaintStyle(Color.BLACK);
                        popupWindow.dismiss();
                        break;
                    case 2:
                        myDrawView.setPaintStyle(Color.BLUE);
                        popupWindow.dismiss();
                        break;
                }
            }
        });
    }
    //获取屏幕尺寸
    private Point getScreenSize(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Point point = new Point();
        point.x = displayMetrics.widthPixels;
        point.y = displayMetrics.heightPixels;
        return point;
    }
    private RecyclerView list;
    private FindAdapter adapter;
}
