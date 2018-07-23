package com;

/**
 * Created by Administrator on 2018/7/20
 * 邮箱 18780569202@163.com
 */
public class JniUtils {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();

    public static native int intFromJNI();
}
