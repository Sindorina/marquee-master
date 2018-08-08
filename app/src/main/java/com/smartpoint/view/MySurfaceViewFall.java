package com.smartpoint.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.smartpoint.marquee.R;

/**
 * Created by Administrator on 2018/8/2
 * 邮箱 18780569202@163.com
 */
public class MySurfaceViewFall extends SurfaceView implements SurfaceHolder.Callback, Runnable  {
    private Thread th;
    private SurfaceHolder sfh;
    private Canvas canvas;
    private Paint paint;
    private boolean flag;
    private float dy = 0;
    private float dr = 0;
    private boolean isfinish = false;
    private boolean canRestart = false;
    private float fallHeight = 400;
    public MySurfaceViewFall(Context context) {
        super(context);
        this.setKeepScreenOn(true);
        sfh = this.getHolder();
        sfh.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        th = new Thread(this);
        flag = true;
        th.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }
    public void draw() {
        try {
            canvas = sfh.lockCanvas();
            if (dy<fallHeight){
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.WHITE);
                canvas.drawCircle(400,100+dy,5,paint);
                canvas.drawCircle(350,100+dy,5,paint);
                canvas.drawCircle(300,100+dy,5,paint);
                canvas.drawCircle(250,100+dy,5,paint);
                canvas.drawCircle(200,100+dy,5,paint);
                canvas.drawCircle(150,100+dy,5,paint);
//                canvas.drawLine(400,100+dy,400,105+dy,paint);
            }else {
                isfinish = true;
            }
            if (dy>=fallHeight){
                if (dr<=160){
                    if (isfinish){
                        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    }
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(400,fallHeight+100,20+dr,paint);
                    canvas.drawCircle(350,fallHeight+100,20+dr,paint);
                    canvas.drawCircle(300,fallHeight+100,20+dr,paint);
                    canvas.drawCircle(250,fallHeight+100,20+dr,paint);
                    canvas.drawCircle(200,fallHeight+100,20+dr,paint);
                    canvas.drawCircle(150,fallHeight+100,20+dr,paint);
                }else {
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    canRestart = true;
                }
                dr+=20;
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
        dy+=10;
        if (canRestart){
            dy = 0;
            dr = 0;
            canRestart = false;
            isfinish = false;
        }
    }
    @Override
    public void run() {
        while (flag) {
            draw();
            try {
                Thread.sleep(50);
            } catch (Exception ex) {
            }
        }
    }
}
