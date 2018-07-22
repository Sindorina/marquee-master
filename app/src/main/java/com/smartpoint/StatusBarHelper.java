package com.smartpoint;

import android.app.Activity;

/**
 * Created by Administrator on 2017/5/19.
 */

public class StatusBarHelper {

    /**
     * 获取手机厂商
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    public static void setStatusBarLightMode(Activity activity,boolean isFontColorDark){
        if (getDeviceBrand().contains("Xiaomi")){   //小米系统
            if (AndroidUtil.isLolliPopOrLater()){
                MIUIHelper.setStatusBarDarkMode(isFontColorDark,activity);
            }
        }
    }
}
