package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.smartpoint.marquee.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/21
 * 邮箱 18780569202@163.com
 */
public class DistinctActivity extends AppCompatActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, DistinctActivity.class);
        activity.startActivity(intent);
    }

    TextView textView,textView2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distinct);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        testDistinct();
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
}
