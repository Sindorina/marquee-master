package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.smartpoint.marquee.R;
import com.zph.glpanorama.GLPanorama;

public class VRActivity extends AppCompatActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, VRActivity.class);
        activity.startActivity(intent);
    }
    private VrPanoramaView mVrPanoramaView;
    private VrPanoramaView.Options paNormalOptions;
    private GLPanorama mGLPanorama;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr);
        //初始化google Vr
        initVrPaNormalView();
        //初始化OpenGL
        //initOpenGL();
    }
    private void initOpenGL(){
        mGLPanorama = findViewById(R.id.mGLPanorama);
        mGLPanorama.setGLPanorama(R.drawable.andes);
    }
    //初始化VR图片
    private void initVrPaNormalView() {
        mVrPanoramaView =  findViewById(R.id.mVrPanoramaView);
        paNormalOptions = new VrPanoramaView.Options();
        paNormalOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
        //隐藏全屏模式按钮
        //mVrPanoramaView.setFullscreenButtonEnabled (false);
        //设置隐藏最左边信息的按钮
        mVrPanoramaView.setInfoButtonEnabled(false);
        //设置隐藏立体模型的按钮
        //mVrPanoramaView.setStereoModeButtonEnabled(false);
        //开启手触模式
        mVrPanoramaView.setTouchTrackingEnabled(true);
        //设置监听
        mVrPanoramaView.setEventListener(new ActivityEventListener());
       //加载本地的图片源
        mVrPanoramaView.loadImageFromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sj), paNormalOptions);
        //设置网络图片源
        //panoWidgetView.loadImageFromByteArray();

    }
    private class ActivityEventListener extends VrPanoramaEventListener {
        @Override public void onLoadSuccess() {
            //图片加载成功
            Log.d("VRActivity","onLoadSuccess");
        }
        @Override public void onLoadError(String errorMessage) {
            //图片加载失败
            Log.d("VRActivity","onLoadError");
        }
        @Override public void onClick() {
            super.onClick();
            //当我们点击了VrPanoramaView 时候触发
            Log.d("VRActivity","onClick");
        }
        @Override public void onDisplayModeChanged(int newDisplayMode) {
            super.onDisplayModeChanged(newDisplayMode);
            Log.d("VRActivity","onDisplayModeChanged");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mVrPanoramaView.resumeRendering();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVrPanoramaView.pauseRendering();
    }

    @Override
    protected void onDestroy() {
        mVrPanoramaView.shutdown();
        super.onDestroy();
    }
}
