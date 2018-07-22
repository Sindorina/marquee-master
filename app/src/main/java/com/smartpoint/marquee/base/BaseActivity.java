package com.smartpoint.marquee.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.smartpoint.AppManager;
import com.smartpoint.MyApplication;
import com.smartpoint.StatusBarHelper;


public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    protected MyApplication mApplication = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        //全屏隐藏状态栏
        Window window = getWindow();
        //定义全屏参数
        int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        StatusBarHelper.setStatusBarLightMode(this, true); //设置小米手机系统状态栏字体颜色
        mApplication = MyApplication.newInstance();
        mApplication.setShowActivity(this);
        int layoutId = getContentViewId();
        if (layoutId > 0) { //DiscoverSearchAdapter1
            setContentView(getContentViewId());
        }
        //如果存在actionBar，就隐藏
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        AppManager.getInstance().addActivity(this);
        beforeInitView();   //
        initView();
        initData();
    }

    public abstract int getContentViewId();//放layoutId

    public abstract void beforeInitView();//初始化View之前做的事

    public abstract void initView();//初始化View

    public abstract void initData();//初始化数据

    /**
     * 状态栏透明只有Android 4.4 以上才支持
     */
    public void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            window.setAttributes(layoutParams);
        }
    }

    /**
     * 不用强制转换的findviewbyid
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T findViewByIdNoCast(int id) {
        return (T) findViewById(id);
    }

    public void setOnClick(int... ids) {
        for (int id : ids)
            findViewById(id).setOnClickListener(this);

    }

    public void setOnClick(View... views) {
        for (View view : views)
            view.setOnClickListener(this);

    }

    /**
     * 将某个View设置为返回键
     *
     * @param view
     */
    protected void setToBack(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * 将某个View设置为返回键
     */
    protected void setToBack(int id) {
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * 本段代码用来处理如果输入法还显示的话就消失掉输入键盘
     */
    protected void dismissSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManage = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManage.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭软键盘
     *
     * @param context
     */
    public void hideSoftInput(Activity context) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public Activity getActivity(){
        return this;
    }

    /**
     * 显示键盘
     *
     * @param view
     */
    protected void showKeyboard(View view) {
        try {
            InputMethodManager inputMethodManage = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManage.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.setShowActivity(this);
        // 页面埋点，需要使用Activity的引用，以便代码能够统计到具体页面名
    }

    @Override
    protected void onDestroy() {
        AppManager.getInstance().remove(this);
        super.onDestroy();
    }


}