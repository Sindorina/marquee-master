package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.smartpoint.marquee.R;

/**
 * Created by Administrator on 2018/6/6
 * 邮箱 18780569202@163.com
 */
public class SurfaceViewActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    public static void start(Activity activity){
        Intent intent = new Intent(activity,SurfaceViewActivity.class);
        activity.startActivity(intent);
    }
    private SurfaceView surfaceView;
    private MediaPlayer mediaPlayer;
    private int position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);
        surfaceView = findViewById(R.id.surfaceView);
        mediaPlayer = new MediaPlayer();
        surfaceView.getHolder().addCallback(this);
    }
    @Override
    protected void onPause() {
        //先判断是否正在播放
        if (mediaPlayer.isPlaying()) {
            //如果正在播放我们就先保存这个播放位置
            position=mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
        }
        super.onPause();
    }
    //播放视频
    private void play() {
        try {
            //把视频画面输出到SurfaceView
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //设置需要播放的视频
            mediaPlayer.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
            mediaPlayer.prepare();
            //播放
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            Toast.makeText(this, "开始播放！", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
       new Thread(new Runnable() {
           @Override
           public void run() {
               play();
           }
       }).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
