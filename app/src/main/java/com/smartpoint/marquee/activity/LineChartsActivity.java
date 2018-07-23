package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.smartpoint.marquee.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2018/6/8
 * 邮箱 18780569202@163.com
 */
public class LineChartsActivity extends AppCompatActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LineChartsActivity.class);
        activity.startActivity(intent);
    }
    private LineChartView chart;
    private LineChartData data;
    private  long MaxY=0;//y轴最大值
    private List<Float> listY = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_charts);
        chart = findViewById(R.id.chart);
        setListY();
        generateData();
    }
    //初始化数据
    private void generateData() {
        MaxY = 0;
        List<Line> lines = new ArrayList<Line>();//折现集合（可以有多根折线）
        List<PointValue> values = new ArrayList<PointValue>();//每根折线上的点的集合（每个点包括xy坐标）
        for (int j = 0; j< listY.size(); ++j) {
            values.add(new PointValue(j, listY.get(j)));//添加Y轴数据
            MaxY = (long) Math.max(MaxY,listY.get(j));//取出Y轴最大数据

        }
        Line line = new Line(values);
        line.setColor(Color.parseColor("#FF6600"));//设置折线的颜色
        line.setShape(ValueShape.CIRCLE);//节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setPointRadius(3);//设置节点半径
        line.setPointColor(Color.RED);//设置节点颜色
        lines.add(line);
        data = new LineChartData(lines);
        Axis axisY = new Axis().setHasLines(true);
        axisY.setName("");
        axisY.setTextSize(20);
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < 12; i ++) {
            axisValues.add(new AxisValue(i).setLabel(""+(i+1)));
        }
        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("测试表格");  //表格名称
        axisX.setHasLines(true); //x 轴分割线
        axisX.setTextSize(20);//设置字体大小
        axisX.setMaxLabelChars(10); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(axisValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        data.setAxisYLeft(axisY);//Y轴设置在左边
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        resetViewport();
    }

    private void resetViewport() {
        //设置行为属性，支持缩放、滑动以及平移
        chart.setInteractive(true);
        chart.setZoomType(ZoomType.HORIZONTAL);

        chart.setMaxZoom(8);//最大方法比例
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setLineChartData(data);
        chart.setVisibility(View.VISIBLE);
        chart.setValueSelectionEnabled(true);//设置节点点击后动画

        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         这4句代码可以设置X轴数据的显示个数（x轴0-7个数据），
         1    当数据点个数小于（29）的时候，缩小到极致hellochart默认的是所有显示。
         2    当数据点个数大于（29）的时候，
         2.1  若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
         2.2  若设置axisX.setMaxLabelChars(int count)这句话, 33个数据点测试，
         2.2.1  若 axisX.setMaxLabelChars(10); 里面的10大于v.right= 7; 里面的7，则 刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
         2.2.2  若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
         若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
         */
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = (MaxY+20);
        v.left = 0;
        v.right = 7;
        chart.setCurrentViewportWithAnimation(v);
    }
    private void setListY(){
        listY.add(100.f);listY.add(80.f);
        listY.add(88.5f);listY.add(55.65f);
        listY.add(48.321f);listY.add(19.875f);
        listY.add(35.78f);listY.add(68.654f);
        listY.add(28.321f);listY.add(39.875f);
        listY.add(75.78f);listY.add(88.654f);
    }
}
