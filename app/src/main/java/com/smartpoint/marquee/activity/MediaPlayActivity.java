package com.smartpoint.marquee.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.smartpoint.entity.FileInfo;
import com.smartpoint.marquee.R;
import com.smartpoint.util.LogUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    private boolean canPlay = false;
    private ImageView imageView_thumb,play;
    private boolean hasVideo = false;
    private List<FileInfo> list;
    private String video_url;
    private boolean isoutside = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInitView();
        setContentView(R.layout.activity_media);
        requestPermission();
        getSizeOfScreen();
        initVideoView();
    }
    private void beforeInitView(){
        video_url = getIntent().getStringExtra("key");
    }
    //videoView初始化

    @SuppressLint("ClickableViewAccessibility")
    private void initVideoView(){
        imageView_thumb = findViewById(R.id.imageView_thumb);
        play = findViewById(R.id.play);
        videoView = findViewById(R.id.videoView);
        list = queryAllVideo(this);
        if (list.size()>0){
            path = list.get(0).getPath();
            hasVideo = true;
        }else {
            hasVideo = false;
            path = "android.resource://" + getPackageName() + "/" + R.raw.video;
        }
        if (!TextUtils.isEmpty(video_url)){
            path = video_url;
            hasVideo = true;
            isoutside = true;
        }else {
            isoutside = false;
        }
        videoView.setVideoURI(Uri.parse(path));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                canPlay = true;
                if (isoutside){
                    videoView.start();
                }
                LogUtils.logI("SSSSSSSSSSSSSSSSSSSSS","canPlay-->"+canPlay);
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
        if (hasVideo){
            imageView_thumb.setImageBitmap(getVideoThumbnail(list.get(0).getPath()));
        }
       if (isoutside){
           imageView_thumb.setVisibility(View.GONE);
           play.setVisibility(View.GONE);
       }else {
           play.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (canPlay){
                       LogUtils.logI("SSSSSSSSSSSSSSS","canPlay-->"+canPlay);
                       imageView_thumb.setVisibility(View.GONE);
                       play.setVisibility(View.GONE);
                       videoView.start();
                   }
               }
           });
       }
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
    public static void start(Activity activity,String url) {
        Intent intent = new Intent(activity, MediaPlayActivity.class);
        intent.putExtra("key",url);
        activity.startActivity(intent);
    }
    // 获取视频缩略图
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap b = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            b = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    public ArrayList<FileInfo> queryAllVideo(final Context context) {
        if (context == null) { //判断传入的参数的有效性
            return null;
        }
        ArrayList<FileInfo> videos = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            //查询数据库，参数分别为（路径，要查询的列名，条件语句，条件参数，排序）
            cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null ,null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    FileInfo video = new FileInfo();
                    video.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID))); //获取唯一id
                    video.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))); //文件路径
                    video.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))); //文件名
                    //...   还有很多属性可以设置
                    //可以通过下一行查看属性名，然后在Video.Media.里寻找对应常量名
                    //Log.i(TAG, "queryAllImage --- all column name --- " + cursor.getColumnName(cursor.getPosition()));

                    //获取缩略图（如果数据量大的话，会很耗时——需要考虑如何开辟子线程加载）
                    /*
                     * 可以访问android.provider.MediaStore.Video.Thumbnails查询图片缩略图
                     * Thumbnails下的getThumbnail方法可以获得图片缩略图，其中第三个参数类型还可以选择MINI_KIND
                     */
                    videos.add(video);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return videos;
    }
}
