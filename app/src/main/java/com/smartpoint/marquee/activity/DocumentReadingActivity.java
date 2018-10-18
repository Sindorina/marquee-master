package com.smartpoint.marquee.activity;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.smartpoint.entity.MainContent;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.util.LogUtils;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/10/17
 * 邮箱 18780569202@163.com
 */
public class DocumentReadingActivity extends BaseActivity {
    TextView document;
    String[] texs = null;
    int length = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_document_reading;
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        document = findViewById(R.id.document);
        document.setOnClickListener(this);
        Connector.getDatabase();
    }

    @SuppressLint("CheckResult")
    @Override
    public void initData() {
        final List<MainContent> list = DataSupport.findAll(MainContent.class);
        LogUtils.logE("ss", "list为空-->" + (list.size() == 0));
        if (list.size()==0) {
            LogUtils.logE("SS", "本地读取");
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> e) throws Exception {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/MicroMsg/Download/qzgs.txt";
                    FileInputStream inputStream = new FileInputStream(path);
                    byte[] bytes = new byte[800];
                    int len = 0;
                    int count = 0;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((len = inputStream.read(bytes)) != -1) {
                        String s = new String(bytes, 0, len, "GBK");
                        stringBuilder.append(s).append("&&&&");
                    }
                    inputStream.close();
                    String whole = stringBuilder.toString();
                    e.onNext(whole.substring(0,whole.length()-4));
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            int c = DataSupport.deleteAll(MainContent.class);
                            int wholeLength = s.length();
                            LogUtils.logE("ss", "wholeLength->" + wholeLength);
                            texs = s.split("&&&&");
                            length = texs.length;
                            LogUtils.logE("SS","数组长度-->"+length);
                            List<MainContent> list1 = new ArrayList<>();
                            int dx = wholeLength/10;
                            int begin = 0;
                            int end = dx;
                            LogUtils.logE("SS","分区长度-->"+dx);
                            for (int i = 1; i <= 10; i++) {
                                MainContent mainContent1 = new MainContent();
                                mainContent1.setDocument(s.substring(begin, end));
                                list1.add(mainContent1);
                                LogUtils.logE("SS","begin-->"+begin);
                                LogUtils.logE("SS","end-->"+end);
                                begin += dx;
                                if(i == 9){
                                    end = wholeLength;
                                }else {
                                    end += dx;
                                }

                            }
                            DataSupport.saveAll(list1);
                            document.setText(texs[0]);
                        }
                    });
        } else {
            LogUtils.logE("SS", "数据库");
            StringBuilder stringBuilder = new StringBuilder();
            for (MainContent mainContent:list){
                stringBuilder.append(mainContent.getDocument());
            }
            String whole = stringBuilder.toString();
            LogUtils.logE("ss","whole.length()-->"+whole.length());
            texs = whole.split("&&&&");
            length = texs.length;
            LogUtils.logE("ss","长度-->"+length);
            document.setText(texs[0]);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.document:
                LogUtils.logE("SS", "length-->" + length);
                current++;
                if (current < length - 1) {
                    document.setText(texs[current]);
                }
                break;
        }
    }

    int current = 0;
}
