package com.smartpoint;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import com.smartpoint.faceRecongnize.FaceDB;
import com.tencent.smtt.sdk.QbSdk;
import com.uuzuche.lib_zxing.ZApplication;

import org.litepal.LitePal;

import cn.jpush.android.api.JPushInterface;


public class MyApplication extends ZApplication {
    private final String TAG = this.getClass().toString();
    public FaceDB mFaceDB;
    Uri mImage;
    private Activity mShowActivity = null;//当前显示activity
    public Activity getShowActivity() {
        return mShowActivity;
    }

    public void setShowActivity(Activity mShowActivity) {
        this.mShowActivity = mShowActivity;
    }
    @Override
    public void onCreate() {
        mFaceDB = new FaceDB(this.getExternalCacheDir().getPath());
        mImage = null;
        //SpeechUtility.createUtility(this, SpeechConstant.APPID +"="+ Constant.APP_KEY);
        super.onCreate();
        //初始化X5内核
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。

            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("@@","加载内核是否成功:"+b);
            }
        });
        mBaseApp = this;
        LitePal.initialize(this);//初始化litePal
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
    private volatile static MyApplication mBaseApp;
    public static MyApplication newInstance(){
        if(mBaseApp == null){
            synchronized (MyApplication.class){
                if(mBaseApp == null){
                    mBaseApp = new MyApplication();
                }
            }
        }
        return mBaseApp;
    }

    public void setCaptureImage(Uri uri) {
        mImage = uri;
    }

    public Uri getCaptureImage() {
        return mImage;
    }
    /**
     * @param path
     * @return
     */
    public static Bitmap decodeImage(String path) {
        Bitmap res;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = 1;
            op.inJustDecodeBounds = false;
            //op.inMutable = true;
            res = BitmapFactory.decodeFile(path, op);
            //rotate and scale.
            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }

            Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
            Log.d("com.arcsoft", "check target Image:" + temp.getWidth() + "X" + temp.getHeight());

            if (!temp.equals(res)) {
                res.recycle();
            }
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
