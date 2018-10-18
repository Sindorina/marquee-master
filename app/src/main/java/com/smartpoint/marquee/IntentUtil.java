package com.smartpoint.marquee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.smartpoint.Constant;


/**
 * Created by Administrator on 2018/10/17
 * 邮箱 18780569202@163.com
 */
public class IntentUtil {
    public static void openActivity(Activity activity,Class _class){
        Intent intent = new Intent(activity,_class);
        activity.startActivity(intent);
    }

    public static void openActivity(Activity activity, Class _class, Bundle bundle){
        Intent intent = new Intent(activity,_class);
        intent.putExtra(Constant.KEY1,bundle);
        activity.startActivity(intent);
    }

    public static void openActivityForResult(Activity activity,Class _class){
        Intent intent = new Intent(activity,_class);
        activity.startActivityForResult(intent,Constant.REQUEST_CODE);
    }

    public static void openActivityForResult(Activity activity, Class _class, Bundle bundle){
        Intent intent = new Intent(activity,_class);
        intent.putExtra(Constant.KEY1,bundle);
        activity.startActivityForResult(intent,Constant.REQUEST_CODE);
    }

}
