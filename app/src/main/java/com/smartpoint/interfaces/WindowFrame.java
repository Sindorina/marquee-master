package com.smartpoint.interfaces;

import android.os.Bundle;

public interface WindowFrame {

    /**
     * 获取布局资源Id
     * @return 布局资源Id
     */
    int getLayoutId();

    /**
     * 初始化View
     */
    void initView(Bundle saveInstanceState);

    /**
     * 设置View监听
     */
    void setListener();

    /**
     * 初始化数据
     * @param saveInstanceState 保存的状态信息
     */
    void initData(Bundle saveInstanceState);

    /**
     * 保存状态 在Activity onSaveInstanceState方法调用的时候调用
     * @param xBundle XBundle实例
     */
    void onSaveInstanceState(Bundle xBundle);


    void showToast(String text);

    void showToast(int txtId);
}
