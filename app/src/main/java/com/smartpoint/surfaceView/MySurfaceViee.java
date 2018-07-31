package com.smartpoint.surfaceView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.smartpoint.util.LogUtils;

import java.util.Random;
import java.util.Vector;

public class MySurfaceViee extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Thread th;  
    private SurfaceHolder sfh;
    private Canvas canvas;
    private Paint paint;
    private boolean flag;  
    public static int screenW, screenH;  
    private Vector<MyArc> vc;//这里定义装我们自定义圆形的容器
    private Random ran;//随即库
    public MySurfaceViee(Context context) {
        super(context);  
        this.setKeepScreenOn(true);  
        vc = new Vector<>();
        ran = new Random();//备注1  
        sfh = this.getHolder();  
        sfh.addCallback(this);  
        paint = new Paint();  
        paint.setAntiAlias(true);  
        setFocusable(true);
        for (int i = 0;i<10;i++){
            vc.add(new MyArc(i*40+10,40,40));
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtils.logE("MySurfaceViee","surfaceCreated");
        flag = true;//这里都是上一篇刚讲过的。。。  
        th = new Thread(this);  
        screenW = this.getWidth();  
        screenH = this.getHeight();  
        th.start();  
    }  
    public void draw() {  
        try {  
            canvas = sfh.lockCanvas();  
            canvas.drawColor(Color.BLACK);
            if (vc != null) {//当容器不为空，遍历容器中所有圆形画方法  
                for (int i = 0; i < vc.size(); i++) {  
                    vc.elementAt(i).drawMyArc(canvas, paint);  
                }  
            }  
        } catch (Exception e) {  
            // TODO: handle exception  
        } finally {  
            try {  
                if (canvas != null)  
                    sfh.unlockCanvasAndPost(canvas);  
            } catch (Exception e2) {  
            }  
        }  
    }  
    private void logic() {//主逻辑  
        if (vc != null) {//当容器不为空，遍历容器中所有圆形逻辑  
            for (int i = 0; i < vc.size(); i++) {  
                vc.elementAt(i).logic();  
            }  
        }  
    }  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //当按键事件响应，我们往容器中仍个我们的圆形实例  
        vc.addElement(new MyArc(ran.nextInt(this.getWidth()), ran.nextInt(100), ran.nextInt(50)));  
        return true;  
    }
    @Override
    public void run() {  
        // TODO Auto-generated method stub  
        while (flag) {  
            logic();  
            draw();  
            try {  
                Thread.sleep(100);  
            } catch (Exception ex) {  
            }  
        }  
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtils.logE("MySurfaceViee","surfaceChanged");
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.logE("MySurfaceViee","surfaceDestroyed");
        flag = false;  
    }  
}  