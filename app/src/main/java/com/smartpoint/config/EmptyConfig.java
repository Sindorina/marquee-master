package com.smartpoint.config;

/**
 * 空view提示配置
 * Created by kailun on 2017/8/29.
 */
public class EmptyConfig {
    //提示文字
    private String mTeminderText;
    //提示图标
    private int mTeminderDrawableId = 0;
    //是否显示按键
    private boolean showButtom = false;
    //按键显示文字
    private String mButtomText;

    public static EmptyConfig createNewInstance(String mTeminderText,int mTeminderDrawableId){
        EmptyConfig mEmptyConfig = new EmptyConfig();
        mEmptyConfig.setTeminderText(mTeminderText);
        mEmptyConfig.setTeminderDrawableId(mTeminderDrawableId);
        return mEmptyConfig;
    }

    public static EmptyConfig createNewInstance(String mTeminderText,int mTeminderDrawableId,boolean showButtom,String mButtomText){
        EmptyConfig mEmptyConfig = new EmptyConfig();
        mEmptyConfig.setTeminderText(mTeminderText);
        mEmptyConfig.setTeminderDrawableId(mTeminderDrawableId);
        mEmptyConfig.setButtomText(mButtomText);
        mEmptyConfig.setShowButtom(showButtom);
        return mEmptyConfig;
    }

    public String getTeminderText() {
        return mTeminderText;
    }

    public void setTeminderText(String mTeminderText) {
        this.mTeminderText = mTeminderText;
    }

    public int getTeminderDrawableId() {
        return mTeminderDrawableId;
    }

    public void setTeminderDrawableId(int mTeminderDrawableId) {
        this.mTeminderDrawableId = mTeminderDrawableId;
    }

    public boolean isShowButtom() {
        return showButtom;
    }

    public void setShowButtom(boolean showButtom) {
        this.showButtom = showButtom;
    }

    public String getButtomText() {
        return mButtomText;
    }

    public void setButtomText(String mButtomText) {
        this.mButtomText = mButtomText;
    }
}
