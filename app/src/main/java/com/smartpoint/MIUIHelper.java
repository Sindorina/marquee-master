package com.smartpoint;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.view.Window;

/**
 * Class to manage status and navigation bar tint effects when using KitKat
 * translucent system UI modes.
 *
 */
public class MIUIHelper {

    private static boolean sIsMiuiV6Above;

    static {
        try {
            Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            sIsMiuiV6Above = "V6".equals((String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name"));
            if (!sIsMiuiV6Above){
                sIsMiuiV6Above = "V7".equals((String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name"));
            }
            if (!sIsMiuiV6Above){
                sIsMiuiV6Above = "V8".equals((String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * set status bar darkmode
     * @param darkmode
     * @param activity
     */
    public static void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        if (sIsMiuiV6Above) {
            Class<? extends Window> clazz = activity.getWindow().getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
