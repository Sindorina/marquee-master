package com.smartpoint.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.smartpoint.marquee.R;
import com.smartpoint.util.LogUtils;

/**
 * Created by Administrator on 2018/8/2
 * 邮箱 18780569202@163.com
 */
public class MySurfaceViewTest extends SurfaceView implements SurfaceHolder.Callback, Runnable  {
    private Thread th;
    private SurfaceHolder sfh;
    private Canvas canvas;
    private Paint paint;
    private boolean flag;
    private float wholeBlood = 100;
    private float curBlood = 100;
    private boolean isForward = true;
    private boolean isAdd = true;
    private float bloodWidth = 70;
    private float bloodHeight = 10;
    private float preX = 100,preY = 20;
    public MySurfaceViewTest(Context context) {
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
            canvas.drawColor(Color.WHITE);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pe);
            canvas.drawBitmap(bitmap,preX,preY+30,null);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setTextSize(12);
            if (curBlood==0){
                canvas.drawText("game over",preX,preY,paint);
            }else if (curBlood>0){
                canvas.drawText((int)curBlood+"/100",preX,preY,paint);
            }
            canvas.drawRect(preX,preY+10,preX+70,preY+20, paint);
            if (curBlood>=0){
                paint.setColor(Color.GREEN);
                paint.setStyle(Paint.Style.FILL);
                //canvas.drawRect(101,31,169,39, paint);
                canvas.drawRect(preX+1,preY+11,(curBlood/wholeBlood)*68+preX+1,preY+19, paint);
            }
            if (isAdd){
                if (curBlood<100){
                    curBlood++;
                }else {
                    isAdd = false;
                }
            }else {
                if (curBlood>0){
                    curBlood--;
                }else {
                    isAdd = true;
                }
            }
//            if (isForward){
//                if (preX<500){
//                    preX+=20;
//                }else {
//                    isForward = false;
//                }
//            }else {
//                if (preX>=100){
//                    preX-=20;
//                }else {
//                    isForward = true;
//                }
//            }

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        preX = event.getX();
        preY = event.getY();
        return true;
    }
}
