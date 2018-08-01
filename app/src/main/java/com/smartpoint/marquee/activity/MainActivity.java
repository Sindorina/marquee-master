package com.smartpoint.marquee.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smartpoint.GlideImageLoader;
import com.smartpoint.adapter.MyAdapter;
import com.smartpoint.adapter.RefreshAdapter;
import com.smartpoint.marquee.AEStool;
import com.smartpoint.marquee.MD5Util;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.util.ExampleUtil;
import com.smartpoint.util.LogUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {
    public static final String TAG = "MainActivity";
    public static final String METHOD_GET = "GET";
    public String BaseUri = "https://apiequipment.signp.cn";
    public String key = "56a8d122ec0d330d6d9f541b459e43e1";
    private TextView test,textView;
    private StringBuilder stringBuilder1 = new StringBuilder();
    View view;//底部弹出的dialog的显示View
    private boolean lightSwitch = false;
    private ListView listView;
    private MyAdapter adapter;
    private DrawerLayout drawerLayout;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    // 抽屉菜单对象
    private ActionBarDrawerToggle drawerbar;
    private SwitchButton switchButton ;
    private MediaPlayer mediaPlayer;
    private String[]bgms = new String[3];
    private int curBgm = 0;//当前bgm
    public static final String PUSH_KEY = "alias";
    private String id = "1104a89792ff7868e10";
    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void beforeInitView() {
        checkPermission();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/music/";
        bgms[0] = path+"bg1.mp3";bgms[1] = path+"bg2.mp3";bgms[2] = path+"bg3.mp3";
        boolean hasAlias = getSharedPreferences(PUSH_KEY,MODE_PRIVATE).getBoolean(PUSH_KEY,false);
        if (!hasAlias){
            setAlias();
        }
    }

    @Override
    public void initView() {
        getCpuInfo();
        switchButton = findViewByIdNoCast(R.id.switchBtn);
        test = findViewById(R.id.test);
        listView = findViewById(R.id.listView);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        adapter.addData(getListData());
        adapter.notifyDataSetChanged();
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    LogUtils.logE("MainActivity","打开");
                    playMusic(curBgm);
                }else {
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                    LogUtils.logE("MainActivity","关闭");
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://surfaceView
                        SurfaceViewActivity.start(MainActivity.this);
                        break;
                    case 1://字母快速指引
                        ContactActivity.start(MainActivity.this);
                        break;
                    case 2://折现图
                        LineChartsActivity.start(MainActivity.this);
                        break;
                    case 3://柱状图
                        ColumnChartActivity.start(MainActivity.this);
                        break;
                    case 4://webApp
                        WebAppActivity.start(MainActivity.this);
                        break;
                    case 5://字体
                        FontActivity.start(MainActivity.this);
                        break;
                    case 6://ping
                        PingTestActivity.start(MainActivity.this);
                    case 7://DECODE
                        Decode2Activity.start(MainActivity.this);
                        break;
                    case 8://webSocket
                        WebSocketActivity.start(MainActivity.this);
                        break;
                    case 9://waterWave
                        WaterWaveActivity.start(MainActivity.this);
                        break;
                    case 10://百度语音合成
                        BaiDuVoiceMadeActivity.start(MainActivity.this);
                        break;
                    case 11://粒子效果
                        LeonidsActivity.start(MainActivity.this);
                        break;
                    case 12://view 破坏效果
                        ExplosionAnimationActivity.start(MainActivity.this);
                        break;
                    case 13://画图
                        ValueAnimatorActivity.start(MainActivity.this);
                        break;
                }
            }
        });
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        test.setText(getInfo());
        Log.e("ost", "加密数据-->" + MD5Util.MD5("P1QRMJTWM8"));
        AEStool aeStool = new AEStool("RzyoJXeCuyBkVMRX");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //httpsTest("/panel");
            }
        }).start();

    }

    @Override
    public void initData() {
        initBanner();
        initRefresh();
        initPlayer();
        LogUtils.logE(TAG,"getRegistrationID-->"+JPushInterface.getRegistrationID(this));
        String appKey = ExampleUtil.getAppKey(getApplicationContext());
        if (null == appKey) appKey = "AppKey异常";
        LogUtils.logE(TAG,"appKey-->"+appKey);
    }

    private String getInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        String news1 = "春江潮水连海平，海上明月共潮生。 ";
        String news2 = "滟滟随波千万里，何处春江无月明！ ";
        String news3 = "江流宛转绕芳甸，月照花林皆似霰； ";
        String news4 = "空里流霜不觉飞，汀上白沙看不见。";
        String news5 = "江天一色无纤尘，皎皎空中孤月轮。" +
                "江畔何人初见月？江月何年初照人？" +
                "人生代代无穷已，江月年年只相似。" +
                "不知江月待何人，但见长江送流水。" +
                "白云一片去悠悠，青枫浦上不胜愁。" +
                "谁家今夜扁舟子？何处相思明月楼？" +
                "可怜楼上月徘徊，应照离人妆镜台。" +
                "玉户帘中卷不去，捣衣砧上拂还来。" +
                "此时相望不相闻，愿逐月华流照君。" +
                "鸿雁长飞光不度，鱼龙潜跃水成文。" +
                "昨夜闲潭梦落花，可怜春半不还家。" +
                "江水流春去欲尽，江潭落月复西斜。" +
                "斜月沉沉藏海雾，碣石潇湘无限路。" +
                "不知乘月几人归，落月摇情满江树。";
        stringBuilder.append(news1).append("        ")
                .append(news2).append("        ").append(news3).append("        ").append(news4).append("        ").append(news5);
        return stringBuilder.toString();
    }

    private void httpsTest(String functionName) {
        allowAllSSL();
        try {
            URL url = new URL(BaseUri + functionName);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod(METHOD_GET);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            int code = conn.getResponseCode();
            if (code == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String res = null;
                while ((res = reader.readLine()) != null) {
                    stringBuilder1.append(res);
                }
                reader.close();
                Message message = handler.obtainMessage();
                message.obj = stringBuilder1.toString();
                handler.sendMessage(message);
            } else {
                Log.e("MainActivity", "error");
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //信任所有证书
    private void allowAllSSL() {
        TrustManager[] trustManagers = new TrustManager[]{new HttpsTrustManager()};
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @SuppressLint("BadHostnameVerifier")
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }


    public class HttpsTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String result = (String) msg.obj;
            Log.e("MainActivity", "测试数据-->" + result);
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.web:
                WebViewActivity.start(this);
                return true;
            case R.id.vr:
                VRActivity.start(this);
                return true;
            case R.id.pickerView:
                PickerViewActivity.start(this);
                return true;
            case R.id.tencentWebView:
                TencentWebViewActivity.start(this);
                return true;
            case R.id.media:
                MediaPlayActivity.start(this);
                return true;
            case R.id.zxin:
                ZxinActivity.start(this);
                return true;
            case R.id.install:
                checkPermission();
                return true;
            case R.id.sqlite:
                SQLiteActivity.start(this);
                return true;
            case R.id.litePal:
                LitePalActivity.start(this);
                return true;
            case R.id.vbar:
                DecodeActivity.start(this);
                return true;
            case R.id.jni:
                JniActivity.start(this);
                return true;
            case R.id.reboot:
                try {
                    Log.v("ost", "root Runtime->reboot");
                    Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "reboot "});
                    proc.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            case R.id.show:
                if (lightSwitch) {
                    lightSwitch = false;
                    lightTest(-1.0f);
                } else {
                    lightSwitch = true;
                    lightTest(0.00000000000000000000000000001f);
                }
                return true;
            case R.id.screenLock:
                ScreenLockActivity.start(MainActivity.this);
                return true;
            case R.id.timeCount:
                CountDownTimerActivity.start(MainActivity.this);
                return true;
            case R.id.chrome:
                GoogleActivity.start(MainActivity.this);
                return true;
            case R.id.distinct:
                DistinctActivity.start(MainActivity.this);
                return true;
            case R.id.svg:
                SvgActivity.start(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * 安装
     *
     * @param context   接收外部传进来的context
     * @param mFilePath apk保存路径
     */
    public void install(Context context, String mFilePath) {
        // 核心是下面几句代码
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(mFilePath)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    // TODO: 2018/5/3 权限检测 
    private void checkPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                           // install(MainActivity.this, Environment.getExternalStorageDirectory().getAbsolutePath() + "/123.apk");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            return;
                        } else {
                            return;
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
     * 屏幕亮度测试
     */
    private void lightTest(float brightness) {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness);
        }
        window.setAttributes(lp);
    }

    /**
     * 获取侧边栏数据
     *
     * @return 侧边栏返回数据
     */
    private List<String> getListData() {
        List<String> list = new ArrayList<>();
        list.add("SurfaceView");
        list.add("字母快速指引");
        list.add("折现图");
        list.add("柱状图");
        list.add("JQuery");
        list.add("字体");
        list.add("PING");
        list.add("DECODE2");
        list.add("webSocket");
        list.add("waterWave");
        list.add("百度语音合成");
        list.add("粒子效果");
        list.add("view破坏效果");
        list.add("画图");
        return list;
    }

    private void addDrawerListenner() {
        drawerbar = new ActionBarDrawerToggle(this, drawerLayout, R.mipmap.ic_launcher, R.string.open, R.string.close) {
            //菜单打开
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            // 菜单关闭
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerbar);
    }
    //获取Android手机cpu信息
    private String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        Log.e("OST", "cpu型号-->" + cpuInfo[0]);
        Log.e("OST", "cpu频率-->" + cpuInfo[1]);
        return cpuInfo[0];
    }
    private void initBanner(){
        list.add("http://pic5.photophoto.cn/20071228/0034034901778224_b.jpg");
        list.add("http://pic21.photophoto.cn/20111106/0020032891433708_b.jpg");
        list.add("http://pic9.photophoto.cn/20081128/0033033999061521_b.jpg");
        list.add("http://imgsrc.baidu.com/imgad/pic/item/34fae6cd7b899e51fab3e9c048a7d933c8950d21.jpg");
        list.add("http://pic17.nipic.com/20111022/8575840_114126243000_2.jpg");
        list.add("http://imgsrc.baidu.com/image/c0%3Dpixel_huitu%2C0%2C0%2C294%2C40/sign=aa22fdf148166d222c7a1dd42f5b6c9b/5ab5c9ea15ce36d32ae0f90a31f33a87e950b120.jpg");
        list.add("http://pic11.photophoto.cn/20090415/0020032851022998_b.jpg");
        list.add("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=5e310a4ddb09b3deffb2ec2ba4d606f4/9d82d158ccbf6c81887581cdb63eb13533fa4050.jpg");
        list.add("http://pic32.photophoto.cn/20140817/0034034463193076_b.jpg");
        banner = findViewByIdNoCast(R.id.banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(list);
        banner.start();
    }
    private void initRefresh(){
        list = new ArrayList<>();
        recyclerView = findViewByIdNoCast(R.id.recyclerView);
        smartRefreshLayout = findViewByIdNoCast(R.id.smartRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new RefreshAdapter(list,R.layout.find_pop_item);
        recyclerView.setAdapter(adapter1);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                adapter1.getContacts().clear();
                setFuncInfo();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                adapter1.getContacts().clear();
                setFuncInfo();
            }
        });
        setFuncInfo();
        adapter1.setOnItemClick(new RefreshAdapter.OnItemClick() {
            @Override
            public void clickItem(int position) {
                switch (position){
                    case 0://webView
                        WebViewActivity.start(MainActivity.this);
                        break;
                    case 1://vr
                        VRActivity.start(MainActivity.this);
                        break;
                    case 2://选择器
                        PickerViewActivity.start(MainActivity.this);
                        break;
                    case 3://腾讯webview
                        TencentWebViewActivity.start(MainActivity.this);
                        break;
                    case 4://播放器
                        MediaPlayActivity.start(MainActivity.this);
                        break;
                    case 5://二维码
                        ZxinActivity.start(MainActivity.this);
                        break;
                    case 6://覆盖安装
                        openPopWindow("功能未开启!");
                        break;
                    case 7://数据库
                        SQLiteActivity.start(MainActivity.this);
                        break;
                    case 8://litePal数据库
                        LitePalActivity.start(MainActivity.this);
                        break;
                    case 9://新扫码枪
                        DecodeActivity.start(MainActivity.this);
                        break;
                    case 10://jni测试
                        JniActivity.start(MainActivity.this);
                        break;
                    case 11://重启
                        openPopWindow("功能未开启!");
                        break;
                    case 12://页面亮度展示
                        if (lightSwitch) {
                            lightSwitch = false;
                            lightTest(-1.0f);
                        } else {
                            lightSwitch = true;
                            lightTest(0.00000000000000000000000000001f);
                        }
                        break;
                    case 13://锁屏
                        ScreenLockActivity.start(MainActivity.this);
                        break;
                    case 14://倒计时
                        CountDownTimerActivity.start(MainActivity.this);
                        break;
                    case 15://字符拆分
                        GoogleActivity.start(MainActivity.this);
                        break;
                    case 16://去重
                        DistinctActivity.start(MainActivity.this);
                        break;
                    case 17://SVG
                        SvgActivity.start(MainActivity.this);
                        break;
                    case 18://指纹识别
                        FingerprintsRecogActivity.start(MainActivity.this);
                        break;
                    case 19://图片选择
                        ChoosePictureActivity.start(MainActivity.this);
                        break;
                    case 20://下载与毛玻璃圆角
                        DownloadActivity.start(MainActivity.this);
                        break;
                    case 21://下载与毛玻璃圆角
                        SideDeleteActivity.start(MainActivity.this);
                        break;
                    case 22://字体颜色文本
                        RichEditorActivity.start(MainActivity.this);
                        break;
                    case 23://讯飞语音
                        XfVoiceActivity.start(MainActivity.this);
                        break;
                    case 24://MapBox
                        MapBoxActivity.start(MainActivity.this);
                        break;
                }
            }
        });
    }
    private RefreshAdapter adapter1;
    private Banner banner;
    private List<String> list = new ArrayList<>();

    /**
     * 设置功能列表数据
     */
    private void setFuncInfo(){
        List<String> listInfo = new ArrayList<>();
        listInfo.add("webView");
        listInfo.add("vr全景");
        listInfo.add("选择器");
        listInfo.add("腾讯webview");
        listInfo.add("播放器");
        listInfo.add("二维码");
        listInfo.add("覆盖安装");
        listInfo.add("数据库");
        listInfo.add("litePal数据库");
        listInfo.add("新扫码枪");
        listInfo.add("jni测试");
        listInfo.add("重启");
        listInfo.add("页面亮度展示");
        listInfo.add("屏幕锁定");
        listInfo.add("倒计时");
        listInfo.add("字符拆分");
        listInfo.add("去重");
        listInfo.add("SVG");
        listInfo.add("指纹识别");
        listInfo.add("图片选择");
        listInfo.add("毛玻璃与下载");
        listInfo.add("侧滑删除");
        listInfo.add("字体颜色文本");
        listInfo.add("讯飞语音AIUI");
        listInfo.add("MapBox");
        adapter1.getContacts().addAll(listInfo);
        adapter1.notifyDataSetChanged();
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();
    }
    //打开弹出框
    private void openPopWindow(String info){
        View view = LayoutInflater.from(this).inflate(R.layout.no_content_show,null,false);
        PopupWindow popupWindow = new PopupWindow(view,400,200);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(drawerLayout, Gravity.CENTER,0,0);
        textView = view.findViewById(R.id.textView);
        textView.setText(info);
    }
    //播放音乐
    private void playMusic(int index){
        this.curBgm = index;
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(bgms[index]);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                openPopWindow("BGM播放错误");
                return false;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (curBgm==2){
                    playMusic(0);
                    curBgm = 0;
                }else {
                    playMusic(curBgm+1);
                    curBgm++;
                }
            }
        });
    }
    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias() {
        String alias = "liangkai";
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    SharedPreferences sp = getSharedPreferences("jpush",MODE_PRIVATE);
                    sp.edit().putBoolean(PUSH_KEY,true).apply();
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
            ExampleUtil.showToast(logs, getApplicationContext());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };
}
