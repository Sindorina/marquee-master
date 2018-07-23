package com.smartpoint.marquee.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.skyfishjy.library.RippleBackground;
import com.smartpoint.adapter.FindAdapter;
import com.smartpoint.baiduVoice.MessageStatusRecogListener;
import com.smartpoint.baiduVoice.MyRecognizer;
import com.smartpoint.baiduVoice.StatusRecogListener;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/7/16
 * 邮箱 18780569202@163.com
 */
public class BaiDuVoiceMadeActivity extends BaseActivity{
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, BaiDuVoiceMadeActivity.class);
        activity.startActivity(intent);
    }
    SpeechSynthesizer mSpeechSynthesizer;
    private RelativeLayout parent;
    private RecyclerView list;
    private FindAdapter adapter;
    MyRecognizer myRecognizer;
    private EventManager asr;//
    private List<String> allData;
    @Override
    public int getContentViewId() {
        return R.layout.activity_baidu_voice_made;
    }

    @Override
    public void beforeInitView() {
        requestPermission();
        initBaiduVoice();
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        mSpeechSynthesizer.setContext(this); // this 是Context的之类，如Activity
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {
        parent = findViewByIdNoCast(R.id.parent);
        final RippleBackground rippleBackground= findViewByIdNoCast(R.id.content);
        ImageView imageView=findViewByIdNoCast(R.id.centerImage);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    rippleBackground.startRippleAnimation();
                    startRecord();
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    rippleBackground.stopRippleAnimation();
                    myRecognizer.stop();
                }
                return true;
            }
        });
    }

    @Override
    public void initData() {
        setAllData();
        //listener是SpeechSynthesizerListener 的实现类，需要实现您自己的业务逻辑。SDK合成后会对这个类的方法进行回调。
        mSpeechSynthesizer.setSpeechSynthesizerListener(listener);
        mSpeechSynthesizer.setAppId("11164859");
        mSpeechSynthesizer.setApiKey("KQqvRCdUhyE79spXtXEf1cuo","dcd5db8eadc7d59dce926b0f5febea39");
        mSpeechSynthesizer.auth(TtsMode.ONLINE);  // 纯在线
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0"); // 设置发声的人声音，在线生效
        mSpeechSynthesizer.initTts(TtsMode.ONLINE); // 初始化离在线混合模式，如果只需要在线合成功能，使用 TtsMode.ONLINE
    }

    @Override
    public void onClick(View v) {

    }
    SpeechSynthesizerListener listener = new SpeechSynthesizerListener() {
        @Override
        public void onSynthesizeStart(String s) {

        }

        @Override
        public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

        }

        @Override
        public void onSynthesizeFinish(String s) {

        }

        @Override
        public void onSpeechStart(String s) {

        }

        @Override
        public void onSpeechProgressChanged(String s, int i) {

        }

        @Override
        public void onSpeechFinish(String s) {

        }

        @Override
        public void onError(String s, SpeechError speechError) {

        }
    };
    //开始合成
    private void startMade(String result){
        mSpeechSynthesizer.speak(result);
    }
    //打开弹出框
    private void openPopWindow(List<String> search){
        View view = LayoutInflater.from(this).inflate(R.layout.popwindow_voice,null,false);
        PopupWindow popupWindow = new PopupWindow(view,getScreenSize().x-20, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
        list = view.findViewById(R.id.list);
        adapter = new FindAdapter(search,R.layout.find_pop_item);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setOnItemClick(new FindAdapter.OnItemClick() {
            @Override
            public void clickItem(int position) {
                startMade("正在为您规划到"+adapter.getContacts().get(position)+"的路线");
            }
        });
    }
    //获取屏幕尺寸
    private Point getScreenSize(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Point point = new Point();
        point.x = displayMetrics.widthPixels;
        point.y = displayMetrics.heightPixels;
        return point;
    }
    //所有数据
    private void setAllData(){
        allData = new ArrayList<>();
        allData.add("洗手间1");
        allData.add("洗手间2");
        allData.add("洗手间3");
        allData.add("电梯1");
        allData.add("电梯2");
        allData.add("电梯3");
        allData.add("电梯4");
        allData.add("护士站1");
        allData.add("护士站2");
        allData.add("护士站3");
        allData.add("开水间1");
        allData.add("开水间2");
        allData.add("开水间3");
        allData.add("内科1");
        allData.add("内科2");
        allData.add("内科3");

    }
    /**
     * 初始化百度语音
     */
    private void initBaiduVoice() {
        asr = EventManagerFactory.create(this, "asr"); // this是Activity或其它Context类
        //asr.registerListener(yourListener);
        StatusRecogListener listener = new MessageStatusRecogListener(resultHandler);
        myRecognizer = new MyRecognizer(this, listener);
        //loadOfflineEngine();
        HashMap<String,Object> map = new HashMap<>();
        map.put(SpeechConstant.DECODER, 2); // 0:在线 2.离在线融合(在线优先)
        map.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/TODirection/html/baidu_speech_grammar.bsg"); // 设置离线命令词文件路径
        myRecognizer.loadOfflineEngine(map);
    }

    /**
     * 百度语音结果消息接收
     */
    private Handler resultHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg2 == 1) {
                Log.e("MainActivity","最终结果-->"+msg.obj.toString());
                if (!TextUtils.isEmpty(msg.obj.toString())){
                    List<String> search = searchData(msg.obj.toString().replace("\n",""));
                    if (search.size()!=0){
                        startMade(getString(R.string.success));
                        openPopWindow(search);
                    }else {
                        startMade(getString(R.string.fail));
                    }
                }else {
                    startMade(getString(R.string.fail));
                }
            }else {
                Log.e("MainActivity","提示消息-->"+msg.obj.toString());
            }
            return false;
        }
    });
    //开始录音
    private void startRecord() {
        //{"decoder":2,"accept-audio-volume":false}
        //String json = "{\"vad\":touch,\"accept-audio-data\":false,\"disable-punctuation\":false,\"accept-audio-volume\":true,\"pid\":15361}";
        String json = "{\"vad\":touch,\"decoder\":2,\"accept-audio-volume\":false}";//"vad":"touch"
        //asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
//        String json2 = "{\"decoder\":2,\"accept-audio-volume\":false,\"pid\":1837}";
//        asr.send(SpeechConstant.ASR_START, json2, null, 0, 0);
        Map<String,Object> map = new HashMap<>();
        map.put("vad","touch");
        map.put("decoder",2);
        map.put("accept-audio-volume",false);
        myRecognizer.start(map);
    }

    /**
     * 查询符合条件的地点
     * @return
     */
    private List<String> searchData(String target){
        List<String> searchList = new ArrayList<>();
        for (String s:allData){
            if (s.contains(target)){
                searchList.add(s);
            }
        }
        return searchList;
    }
    //6.0动态权限获取
    private void requestPermission(){
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        };
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(permissions).subscribe(new Observer<Permission>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Permission permission) {
                if (permission.granted){
                }else if (permission.shouldShowRequestPermissionRationale){
                    Toast.makeText(BaiDuVoiceMadeActivity.this,"权限未开启,请重启app",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(BaiDuVoiceMadeActivity.this,"权限未开启,请手动开启权限并重启app",Toast.LENGTH_SHORT).show();
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
}
