package com.smartpoint.interfaces;


import com.smartpoint.config.EmptyConfig;

/**
 * viewModel回调到view层
 * Created by Administrator on 2017/5/18.
 */
public interface OnViewModelCallback<T> {

    /**
     * 给view设置数据
     * @param value
     */
    public  void setValues(T value);

    //文字提示
    public void onReminder(EmptyConfig mEmptyConfig);

}
