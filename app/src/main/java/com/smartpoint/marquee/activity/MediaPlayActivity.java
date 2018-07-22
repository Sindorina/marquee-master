package com.smartpoint.marquee.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import com.smartpoint.marquee.R;
import com.smartpoint.util.LogUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Field;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MediaPlayActivity extends AppCompatActivity {
    private int width,height;
    private float startY,endY,startX,endX;
    private float limitedX = 10;//滑动限制超过10px才进行
    private int skipSize = 5000;//videoView快进或者后退的时间
    private String url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private String path;
    private VideoView videoView;
    private float volume = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        requestPermission();
        getSizeOfScreen();
        initVideoView();
    }
    //videoView初始化

    @SuppressLint("ClickableViewAccessibility")
    private void initVideoView(){
        videoView = findViewById(R.id.videoView);
        path = "android.resource://" + getPackageName() + "/" + R.raw.video;
        videoView.setVideoURI(Uri.parse(path));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(MediaPlayActivity.this,"播放完成",Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    startY = event.getY();
                    startX = event.getX();
                    LogUtils.logD("MediaPlayActivity","ACTION_DOWN"+skipSize);
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    endY = event.getY();
                    endX = event.getX();
                    float dY = endY - startY;
                    LogUtils.logD("MediaPlayActivity","是否在播放"+videoView.isPlaying());
                    if (videoView.isPlaying()){
                        setVolume(-dY/height,videoView);
                    }
                    float dX = endX - startX;
                    if (dX>limitedX){//大于10像素
                        if (videoView.isPlaying()){
                            int max = videoView.getDuration();
                            int cur = videoView.getCurrentPosition();
                            LogUtils.logD("MediaPlayActivity","总进度"+skipSize);
                            LogUtils.logD("MediaPlayActivity","当前进度"+cur);
                            if (dX>=0){//后退5s
                                if (cur-skipSize>0){
                                    videoView.seekTo(cur-skipSize);
                                    LogUtils.logD("MediaPlayActivity","跳跃进度"+(cur-skipSize));
                                }
                            }else {//快进5s
                                if ((cur+skipSize)<max){
                                    videoView.seekTo(cur+skipSize);
                                    LogUtils.logD("MediaPlayActivity","跳跃进度"+(cur+skipSize));
                                }
                            }
                        }
                    }
                }
                return true;
            }
        });
    }
    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void requestPermission(){
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Observer<Permission>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Permission permission) {
                if (permission.granted){
                }else if (permission.shouldShowRequestPermissionRationale){
                    Toast.makeText(MediaPlayActivity.this,"权限未开启,请重启app",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MediaPlayActivity.this,"权限未开启,请手动开启权限并重启app",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
    /**
     * 使用AudioManager控制音量
     * @param value
     * @param context
     * //    https://github.com/lucid-lynxz/BlogSamples/blob/master/VideoViewDemo/app/src/main/java/org/lynxz/videoviewdemo/MainActivity.java
     */
    private void setVoiceVolume(float value,Context context) {
        try {
            AudioManager audioManager=
                    (AudioManager)context.getSystemService(Service.AUDIO_SERVICE);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//(最大值是15)
//            int flag = value > 0 ? -1 : 1;
//            currentVolume += flag * 0.1 * maxVolume;
            currentVolume += value*maxVolume;
            // 对currentVolume进行限制
            if (currentVolume>maxVolume){
                currentVolume = maxVolume;
            }
            if (currentVolume<0){
                currentVolume = 0;
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
            LogUtils.logD("MediaPlayActivity","当前音量"+currentVolume);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @param dVolume 音量改变
     * @param object VideoView实例
     * */
    public void setVolume(float dVolume,Object object) {
        if ((volume+dVolume)>1){
            volume = 1;
        }else {
            volume += dVolume;
            if (volume < 0) {
                volume = 0;
            }
        }
        try {
            Class<?> forName = Class.forName("android.widget.VideoView");
            Field field = forName.getDeclaredField("mMediaPlayer");
            field.setAccessible(true);
            MediaPlayer mMediaPlayer = (MediaPlayer) field.get(object);
            mMediaPlayer.setVolume(volume, volume);
            Log.d("OST","当前音量-->"+volume);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取屏幕尺寸
     */
    private void getSizeOfScreen(){
        WindowManager manager = getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
    }
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MediaPlayActivity.class);
        activity.startActivity(intent);
    }
}
