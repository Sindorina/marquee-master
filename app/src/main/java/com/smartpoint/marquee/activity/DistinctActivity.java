package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/5/21
 * 邮箱 18780569202@163.com
 */
public class DistinctActivity extends BaseActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, DistinctActivity.class);
        activity.startActivity(intent);
    }

    TextView textView,textView2,textView3,textView4;
    @Override
    public int getContentViewId() {
        return R.layout.activity_distinct;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        testDistinct();
        RxJavaDistinct();
    }

    @Override
    public void initData() {

    }

    /**
     * 通过set去重, 不打乱原有list的顺序
     *       list中相同的对象会被去重复
     *
     * @param <T>List<T> list
     * @return List<T>
     * */
    private  <T>List<T> distinctBySetOrder(List<T> list){
        Set<T> set = new HashSet<>();
        List<T> newList = new ArrayList<>();
        for (T t :list) {
            if(set.add(t)){
                newList.add(t);
            }
        }
        return newList;
    }

    /**
     * list去重测试
     */
    private void testDistinct(){
        List<String> list = new ArrayList<>();
        list.add("厕所");list.add("厕所");
        list.add("楼梯");list.add("护士站");
        list.add("厕所");
        textView.append("原数据-->");
        for (String s:list){
            textView.append(s);
        }
        List<String> list1 = distinctBySetOrder(list);
        textView2.append("去重后数据-->");
        for (String str:list1) {
           textView2.append(str);
        }
    }

    private void RxJavaDistinct(){
        List<String> list = new ArrayList<>();
        list.add("厕所");list.add("厕所");
        list.add("楼梯");list.add("护士站");
        list.add("厕所");
        textView3.append("原数据-->");
        for (String s:list){
            textView3.append(s);
        }
        textView4.append("去重后数据-->");
        Observable.fromArray(list.toArray()).distinct().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                textView4.append(o.toString());
            }
        });
    }
    @Override
    public void onClick(View v) {

    }
}
