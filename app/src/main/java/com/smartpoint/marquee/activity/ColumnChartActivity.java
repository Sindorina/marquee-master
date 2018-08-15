package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Administrator on 2018/6/8
 * 邮箱 18780569202@163.com
 */
public class ColumnChartActivity extends BaseActivity {
    /*========== 数据相关 ==========*/
    private ColumnChartData mColumnChartData;    //柱状图数据
    private ColumnChartView chart;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ColumnChartActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_column_chart;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        chart = findViewById(R.id.chart);
        initColumnInfo();
    }

    @Override
    public void initData() {

    }

    /**
     * 初始化柱状图
     */
    private void initColumnInfo(){
        /*========== 柱状图数据填充 ==========*/
        List<Column> columnList = new ArrayList<>(); //柱子列表
        List<SubcolumnValue> subcolumnValueList;     //子柱列表（即一个柱子，因为一个柱子可分为多个子柱）
        for (int i = 0; i < 7; ++i) {
            subcolumnValueList = new ArrayList<>();
            subcolumnValueList.add(new SubcolumnValue((float) Math.random() * 50f, ChartUtils.pickColor()));
            Column column = new Column(subcolumnValueList);
            columnList.add(column);
            column.setHasLabels(true);//展示Y坐标上数值
        }
        //设置数据
        mColumnChartData = new ColumnChartData(columnList);

        /*===== 坐标轴相关设置 =====*/
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setTextSize(20);
        axisY.setTextSize(20);
        axisX.setTextColor(Color.BLACK);
        axisY.setTextColor(Color.BLACK);
        axisX.setName("Axis X");    //设置横轴名称
        axisY.setName("Axis Y");    //设置竖轴名称
        mColumnChartData.setAxisXBottom(axisX); //设置横轴
        mColumnChartData.setAxisYLeft(axisY);   //设置竖轴
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i=0;i<7;i++){
            axisValues.add(new AxisValue(i).setLabel(""+(i+1)));
        }
        axisX.setValues(axisValues);
        //以上所有设置的数据、坐标配置都已存放到mColumnChartData中，接下来给mColumnChartView设置这些配置
        chart.setColumnChartData(mColumnChartData);
    }

    @Override
    public void onClick(View v) {

    }
}
