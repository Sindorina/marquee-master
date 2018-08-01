package com.smartpoint.surfaceView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class MyArc {
    private int arc_x, arc_y, arc_r;//圆形的X,Y坐标和半径  
    private float speed_x = 1.2f, speed_y = 1.2f;//小球的x、y的速度  
    private float vertical_speed;//加速度  
    private float horizontal_speed;//水平加速度,大家自己试着添加吧  
    private final float ACC = 0.135f;//为了模拟加速度的偏移值  
    private final float RECESSION = 0.2f;//每次弹起的衰退系数   
    private boolean isDown = true;//是否处于下落  状态  
    private Random ran;//随即数库

    /**
     * @param x 圆形X坐标
     * @param y 圆形Y坐标
     * @param r 圆形半径
     * @定义圆形的构造函数
     */
    public MyArc(int x, int y, int r) {
        ran = new Random();
        this.arc_x = x;
        this.arc_y = y;
        this.arc_r = r;
    }

    public void drawMyArc(Canvas canvas, Paint paint) {//每个圆形都应该拥有一套绘画方法
        paint.setColor(getRandomColor());//不断的获取随即颜色，对圆形进行填充(实现圆形闪烁效果)  
        canvas.drawArc(new RectF(arc_x + speed_x, arc_y + speed_y, arc_x + 2 *
                arc_r + speed_x, arc_y + 2 * arc_r + speed_y), 0, 360, true, paint);
    }

    /**
     * @return
     * @返回一个随即颜色
     */
    public int getRandomColor() {
        int ran_color = ran.nextInt(8);
        int temp_color = 0;
        switch (ran_color) {
            case 0:
                temp_color = Color.WHITE;
                break;
            case 1:
                temp_color = Color.BLUE;
                break;
            case 2:
                temp_color = Color.CYAN;
                break;
            case 3:
                temp_color = Color.DKGRAY;
                break;
            case 4:
                temp_color = Color.RED;
                break;
            case 6:
                temp_color = Color.GREEN;
            case 7:
                temp_color = Color.GRAY;
            case 8:
                temp_color = Color.YELLOW;
                break;
        }
        return temp_color;
    }

    /**
     * 圆形的逻辑
     */
    public void logic() {//每个圆形都应该拥有一套逻辑  
        if (isDown) {//圆形下落逻辑  
            /*--备注1-*/
            speed_y += vertical_speed;//圆形的Y轴速度加上加速度
            int count = (int) vertical_speed++;
            //这里拿另外一个变量记下当前速度偏移量  
            //如果下面的for (int i = 0; i < vertical_speed++; i++) {}这样就就死循环了 - -  
            for (int i = 0; i < count; i++) {//备注1  
                /*--备注2-*/
                vertical_speed += ACC;
            }
        } else {//圆形反弹逻辑  
            speed_y -= vertical_speed;
            int count = (int) vertical_speed--;
            for (int i = 0; i < count; i++) {
                vertical_speed -= ACC;
            }
        }
        if (isCollision()) {
            isDown = !isDown;//当发生碰撞说明圆形的方向要改变一下了！  
            vertical_speed -= vertical_speed * RECESSION;//每次碰撞都会衰减反弹的加速度  
        }
    }

    /**
     * 圆形与屏幕底部的碰撞
     *
     * @return
     * @返回true 发生碰撞
     */
    public boolean isCollision() {
        return arc_y + 2 * arc_r + speed_y >= MySurfaceViee.screenH;
    }
}  
