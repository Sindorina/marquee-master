package com.smartpoint.marquee.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class XfVoiceActivity extends BaseActivity{
    private TextView result;
    private AIUIAgent mAIUIAgent = null;
    private int mAIUIState = AIUIConstant.STATE_IDLE;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, XfVoiceActivity.class);
        activity.startActivity(intent);
    }
    public static final String TAG ="XfVoiceActivity";
    @Override
    public int getContentViewId() {
        return R.layout.activity_baidu_voice_made;
    }

    @Override
    public void beforeInitView() {
        requestPermission();
        //创建AIUIAgent
        mAIUIAgent = AIUIAgent.createAgent(this,getAIUIParams(),mAIUIListener);
    }

    @Override
    public void initView() {
        result = findViewByIdNoCast(R.id.result);
        findViewByIdNoCast(R.id.centerImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先发送唤醒消息，改变AIUI内部状态，只有唤醒状态才能接收语音输入
                if( AIUIConstant.STATE_WORKING != mAIUIState ){
                    AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
                    mAIUIAgent.sendMessage(wakeupMsg);
                }
                // 打开AIUI内部录音机，开始录音
                String params = "sample_rate=16000,data_type=audio";
                AIUIMessage writeMsg = new AIUIMessage( AIUIConstant.CMD_START_RECORD, 0, 0, params, null );
                mAIUIAgent.sendMessage(writeMsg);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
    private AIUIListener mAIUIListener = new AIUIListener() {

        @Override
        public void onEvent(AIUIEvent event) {
            Log.i( TAG,  "on event: " + event.eventType );

            switch (event.eventType) {
                case AIUIConstant.EVENT_CONNECTED_TO_SERVER:
                    showTip("已连接服务器");
                    break;

                case AIUIConstant.EVENT_SERVER_DISCONNECTED:
                    showTip("与服务器断连");
                    break;

                case AIUIConstant.EVENT_WAKEUP:
                    showTip( "进入识别状态" );
                    break;

                case AIUIConstant.EVENT_RESULT: {
                    try {
                        JSONObject bizParamJson = new JSONObject(event.info);
                        JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
                        JSONObject params = data.getJSONObject("params");
                        JSONObject content = data.getJSONArray("content").getJSONObject(0);

                        if (content.has("cnt_id")) {
                            String cnt_id = content.getString("cnt_id");
                            String cntStr = new String(event.data.getByteArray(cnt_id), "utf-8");

                            // 获取该路会话的id，将其提供给支持人员，有助于问题排查
                            // 也可以从Json结果中看到
                            String sid = event.data.getString("sid");
                            String tag = event.data.getString("tag");

                            showTip("tag=" + tag);

                            // 获取从数据发送完到获取结果的耗时，单位：ms
                            // 也可以通过键名"bos_rslt"获取从开始发送数据到获取结果的耗时
                            long eosRsltTime = event.data.getLong("eos_rslt", -1);

                            if (TextUtils.isEmpty(cntStr)) {
                                return;
                            }
                            JSONObject cntJson = new JSONObject(cntStr);
                            String sub = params.optString("sub");
                            if ("nlp".equals(sub)) {
                                // 解析得到语义结果
                                String resultStr = cntJson.optString("intent");
                                Log.i(TAG, resultStr);
                            }
                            if (cntJson != null) {
                                if (cntJson.has("text") && cntJson.getJSONObject("text") != null) {
                                    JSONArray ws = cntJson.getJSONObject("text").getJSONArray("ws");
                                    for (int i = 0; i < ws.length(); i++) {
                                        JSONObject wObj = ws.getJSONObject(i);
                                        if (wObj.has("cw") && wObj.getJSONArray("cw").length() > 0) {
                                            JSONObject cwObj = wObj.getJSONArray("cw").getJSONObject(0);
                                            if (cwObj.has("w") && !cwObj.getString("w").equals("")) {
                                                String kw = cwObj.getString("w");
                                                if (!kw.equals("我") && !kw.equals("要") && !kw.equals("想") && !kw.equals("找") && !kw.equals("呢") && !kw.equals("知道") && !kw.equals("哪儿") &&
                                                        !kw.equals("哪里") && !kw.equals("在") && !kw.equals("去") && !kw.equals("带") && !kw.equals("怎么") && !kw.equals("几楼") && !kw.equals("什么") &&
                                                        !kw.equals("地方") && !kw.equals("你") && !kw.equals("能") && !kw.equals("吗")) {
                                                    keyWords.add(kw);
                                                }
                                            }
                                        }
                                    }
                                }
                                if (cntJson.has("intent") && cntJson.getJSONObject("intent") != null) {
                                    JSONObject intent = cntJson.getJSONObject("intent");
                                    if (intent.has("text") && !intent.getString("text").equals("")) {
                                        wholeSentence = intent.getString("text");
                                        Log.e(TAG,"wholeSentence-->"+wholeSentence);
                                    }
                                }
                            }

                            if (keyWords.size() > 0 && !wholeSentence.equals("")) {
                                String keyWord = "";
                                for (int k = 0; k < keyWords.size(); k++) {
                                    keyWord += keyWords.get(k) + "|";
                                }
                                keyWord = keyWord.substring(0, keyWord.length() - 1);
                                result.setText("分词-->"+keyWord+"  整句话-->"+wholeSentence);
                                Log.e(TAG,"keyWord-->"+keyWord);
                                wholeSentence = "";
                                keyWords.clear();
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();

                    }

                } break;

                case AIUIConstant.EVENT_ERROR: {

                } break;

                case AIUIConstant.EVENT_VAD: {
                    if (AIUIConstant.VAD_BOS == event.arg1) {
                        showTip("找到vad_bos");
                    } else if (AIUIConstant.VAD_EOS == event.arg1) {
                        showTip("找到vad_eos");
                    } else {
                        showTip("" + event.arg2);
                    }
                } break;

                case AIUIConstant.EVENT_START_RECORD: {
                    showTip("已开始录音");
                } break;

                case AIUIConstant.EVENT_STOP_RECORD: {
                    showTip("已停止录音");

                } break;

                case AIUIConstant.EVENT_STATE: {	// 状态事件
                    mAIUIState = event.arg1;

                    if (AIUIConstant.STATE_IDLE == mAIUIState) {
                        // 闲置状态，AIUI未开启
                        showTip("STATE_IDLE");
                    } else if (AIUIConstant.STATE_READY == mAIUIState) {
                        // AIUI已就绪，等待唤醒
                        showTip("STATE_READY");
                    } else if (AIUIConstant.STATE_WORKING == mAIUIState) {
                        // AIUI工作中，可进行交互
                        showTip("STATE_WORKING");
                    }
                } break;

                case AIUIConstant.EVENT_CMD_RETURN: {
                    if (AIUIConstant.CMD_SYNC == event.arg1) {	// 数据同步的返回
                        int dtype = event.data.getInt("sync_dtype", -1);
                        int retCode = event.arg2;

                        switch (dtype) {
                            case AIUIConstant.SYNC_DATA_SCHEMA: {
                                if (AIUIConstant.SUCCESS == retCode) {
                                    // 上传成功，记录上传会话的sid，以用于查询数据打包状态
                                    // 注：上传成功并不表示数据打包成功，打包成功与否应以同步状态查询结果为准，数据只有打包成功后才能正常使用
                                } else {
                                }
                            } break;
                        }
                    } else if (AIUIConstant.CMD_QUERY_SYNC_STATUS == event.arg1) {	// 数据同步状态查询的返回
                        // 获取同步类型
                        int syncType = event.data.getInt("sync_dtype", -1);
                        if (AIUIConstant.SYNC_DATA_QUERY == syncType) {
                            // 若是同步数据查询，则获取查询结果，结果中error字段为0则表示上传数据打包成功，否则为错误码
                            String result = event.data.getString("result");

                            showTip(result);
                        }
                    }
                } break;

                default:
                    break;
            }
        }

    };
    private void showTip(String res){

    }
    private String getAIUIParams() {
        String params = "";
        AssetManager assetManager = getResources().getAssets();
        try {
            InputStream ins = assetManager.open( "cfg/aiui_phone.cfg" );
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return params;
    }
    private String wholeSentence = "";
    private List<String> keyWords = new ArrayList<>();
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
                    Toast.makeText(XfVoiceActivity.this,"权限未开启,请重启app",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(XfVoiceActivity.this,"权限未开启,请手动开启权限并重启app",Toast.LENGTH_SHORT).show();
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
