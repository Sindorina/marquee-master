package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.util.LogUtils;

/**
 * Created by Administrator on 2018/6/6
 * 邮箱 18780569202@163.com
 */
public class SurfaceViewActivity extends BaseActivity implements SurfaceHolder.Callback {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SurfaceViewActivity.class);
        activity.startActivity(intent);
    }
    private static final String TAG = "SurfaceViewActivity";
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private int position;
    private AssetManager assetManager;
    @Override
    public int getContentViewId() {
        return R.layout.activity_surface_view;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        surfaceView = findViewById(R.id.surfaceView);
        //mediaPlayer = new MediaPlayer();
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(this);
        play(0);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onPause() {
        //先判断是否正在播放
        if (mediaPlayer.isPlaying()) {
            //如果正在播放我们就先保存这个播放位置
            position = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
        }
        super.onPause();
    }

    //播放视频
    private void play(final int msec) {
        try {
            mediaPlayer = new MediaPlayer();
            //设置音频流类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //设置需要播放的视频
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("video.mp4");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.prepareAsync();
            //播放
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    mediaPlayer.seekTo(msec);
                }
            });
            Toast.makeText(this, "开始播放！", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtils.logE(TAG,"surfaceCreated");
        //把视频画面输出到SurfaceView
        mediaPlayer.setDisplay(surfaceView.getHolder());
        if (position > 0) {
            // 创建SurfaceHolder的时候，如果存在上次播放的位置，则按照上次播放位置进行播放
            play(position);
            position = 0;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtils.logE(TAG,"surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.logE(TAG,"surfaceDestroyed");
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            position = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
        }
    }

    @Override
    public void onClick(View v) {

    }
}
