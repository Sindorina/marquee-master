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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.smartpoint.entity.FileInfo;
import com.smartpoint.marquee.BiliDanmukuParser;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.util.LogUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.util.IOUtils;
import master.flame.danmaku.danmaku.util.SystemClock;

public class MediaPlayActivity extends BaseActivity {
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
    private TextView sendText,sendPic;//发送
    private EditText editText;//弹幕输入
    @Override
    public int getContentViewId() {
        return R.layout.activity_media;
    }

    @Override
    public void beforeInitView() {
        video_url = getIntent().getStringExtra("key");
        requestPermission();
    }


    @Override
    public void initView() {
        getSizeOfScreen();
        initVideoView();
        editText = findViewByIdNoCast(R.id.editText);
        sendText = findViewByIdNoCast(R.id.sendText);
        sendPic = findViewByIdNoCast(R.id.sendPic);
        sendPic.setOnClickListener(this);
        sendText.setOnClickListener(this);
        initDanMuView();
    }

    @Override
    public void initData() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = editText.getText().toString();
                if (TextUtils.isEmpty(text)||TextUtils.isEmpty(text.trim())){
                    sendText.setBackgroundColor(Color.GRAY);
                    sendText.setEnabled(false);
                }else {
                    sendText.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                    sendText.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendText://发送弹幕
                addDanmaku(true,editText.getText().toString().trim());
                editText.setText("");
                break;
            case R.id.sendPic://发送弹幕带图片
                addDanmaKuShowTextAndImage(true,editText.getText().toString().trim());
                editText.setText("");
                break;
        }
    }
    /**
     * 添加一条文字弹幕
     * @param islive 是否是实时的
     */
    private void addDanmaku(boolean islive,String text) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        // for(int i=0;i<100;i++){
        // }
        danmaku.text = text;
        danmaku.padding = 5;
        danmaku.priority = 90;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = islive;
        danmaku.setTime(mDanmakuView.getCurrentTime() + 1200);
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = Color.WHITE;
        // danmaku.underlineColor = Color.GREEN;
        danmaku.borderColor = Color.GREEN;
        mDanmakuView.addDanmaku(danmaku);

    }

    /**
     * 添加一条图文弹幕
     * @param islive 是否是实时的
     */
    private void addDanmaKuShowTextAndImage(boolean islive,String result) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        Drawable drawable = getResources().getDrawable(R.mipmap.flower);
        drawable.setBounds(0, 0, 100, 100);
        SpannableStringBuilder spannable = createSpannable(drawable,result);
        danmaku.text = spannable;
        danmaku.padding = 5;
        danmaku.priority = 1;  // 一定会显示, 一般用于本机发送的弹幕
        danmaku.isLive = islive;
        danmaku.setTime(mDanmakuView.getCurrentTime() + 1200);
        danmaku.textSize = 25f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.RED;
        danmaku.textShadowColor = 0; // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
        danmaku.underlineColor = Color.GREEN;
        mDanmakuView.addDanmaku(danmaku);
    }

    /**
     * span 生成
     * @param drawable
     * @return
     */
    private SpannableStringBuilder createSpannable(Drawable drawable,String result) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(result);
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }
    private IDanmakuView mDanmakuView;

    private BaseDanmakuParser mParser;

    private DanmakuContext mContext;

    private BaseCacheStuffer.Proxy mCacheStufferAdapter = new BaseCacheStuffer.Proxy() {

        private Drawable mDrawable;

        @Override
        public void prepareDrawing(final BaseDanmaku danmaku, boolean fromWorkerThread) {
            if (danmaku.text instanceof Spanned) { // 根据你的条件检查是否需要需要更新弹幕
                // FIXME 这里只是简单启个线程来加载远程url图片，请使用你自己的异步线程池，最好加上你的缓存池
                new Thread() {

                    @Override
                    public void run() {
                        String url = "http://www.bilibili.com/favicon.ico";
                        InputStream inputStream = null;
                        Drawable drawable = mDrawable;
                        if(drawable == null) {
                            try {
                                URLConnection urlConnection = new URL(url).openConnection();
                                inputStream = urlConnection.getInputStream();
                                drawable = BitmapDrawable.createFromStream(inputStream, "bitmap");
                                mDrawable = drawable;
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                IOUtils.closeQuietly(inputStream);
                            }
                        }
                        if (drawable != null) {
                            drawable.setBounds(0, 0, 100, 100);
                            SpannableStringBuilder spannable = createSpannable(drawable,"默认");
                            danmaku.text = spannable;
                            if(mDanmakuView != null) {
                                mDanmakuView.invalidateDanmaku(danmaku, false);
                            }
                            return;
                        }
                    }
                }.start();
            }
        }

        @Override
        public void releaseResource(BaseDanmaku danmaku) {
            // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
        }
    };
    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }

    /**
     * 初始化弹幕View
     */
    private void initDanMuView(){
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuView = findViewById(R.id.sv_danmaku);
        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
                .setCacheStuffer(new SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
//        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40);
        if (mDanmakuView != null) {
            mParser = createParser(this.getResources().openRawResource(R.raw.comments));
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });
            mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {

                @Override
                public boolean onDanmakuClick(IDanmakus danmakus) {
                    Log.d("DFM", "onDanmakuClick: danmakus size:" + danmakus.size());
                    BaseDanmaku latest = danmakus.last();
                    if (null != latest) {
                        Log.d("DFM", "onDanmakuClick: text of latest danmaku:" + latest.text);
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onDanmakuLongClick(IDanmakus danmakus) {
                    return false;
                }

                @Override
                public boolean onViewClick(IDanmakuView view) {
                    return false;
                }
            });
            mDanmakuView.prepare(mParser, mContext);
            mDanmakuView.showFPS(false);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mDanmakuView != null && mDanmakuView.isPrepared()) {
            mDanmakuView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }
}
