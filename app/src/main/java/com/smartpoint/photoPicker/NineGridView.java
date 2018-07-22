package com.smartpoint.photoPicker;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.smartpoint.GlideImageLoader;
import com.smartpoint.MyApplication;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPreview;

/**
 * Created by kailun on 2017/8/17.
 */

public class NineGridView extends NineGridLayout {
    private GlideImageLoader loader = new GlideImageLoader();
    protected final int MAX_W_H_RATIO = 3;
    //SD卡路径
    private String mSdCardPath = Environment.getExternalStorageDirectory() + "";

    public NineGridView(Context context) {
        super(context);
    }

    public NineGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(final ImageView imageView, String url, final int parentWidth) {
        if (url.startsWith(mSdCardPath)) {
            loader.displayImage(MyApplication.newInstance(),"file://" + url,imageView);
        } else {
            loader.displayImage(MyApplication.newInstance(),"file://" + url,imageView);
        }

        setOneImageLayoutParams(imageView, parentWidth,dp2px(getContext(), 200));//这里是只显示一张图片的情况
        return false;
    }

    @Override
    protected void displayImage(ImageView imageView, String url) {
        if (url.startsWith(mSdCardPath)) {
            loader.displayImage(MyApplication.newInstance(),"file://" + url,imageView);
        } else {
            loader.displayImage(MyApplication.newInstance(),"file://" + url,imageView);
        }
    }

    @Override
    protected void onClickImage(int i, String url, List<String> urlList) {
        ArrayList mImageList = new ArrayList();
        for (String urlStr : urlList) {
            if (url.startsWith(mSdCardPath)) {
                mImageList.add(urlStr);
            } else {
                mImageList.add(getImageUniversalUrl(urlStr));
            }

        }
        PhotoPreview.builder()
                .setPhotos(mImageList)
                .setCurrentItem(i)
                .start((Activity) getContext());
    }
    /**
     * 获取图片路径全路径
     *
     * @param url
     * @return
     */
    public static String getImageUniversalUrl(String url) {
        if (url.startsWith("http")){
            return url;
        }
        return url;
    }
    /**
     * px转换成dp
     */
    private int px2dp(Context context,float pxValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }
    /**
     * dp转换成px
     */
    private int dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
}
