package com.smartpoint.marquee.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.smartpoint.marquee.R;
import com.smartpoint.util.LogUtils;

/**
 * Created by Administrator on 2018/6/6
 * 邮箱 18780569202@163.com
 */
public class SurfaceViewDrawActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SurfaceViewDrawActivity.class);
        activity.startActivity(intent);
    }
    private static final String TAG = "SurfaceViewActivity";
    private SurfaceView surfaceView;
    private SurfaceHolder mHolder;
    // 用于绘图的Canvas
    private Canvas mCanvas;
    private Path path;
    private float preX,preY;
    private Paint paint;
    //子线程标志位
    private boolean mIsDrawing;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.setFocusable(true);
        surfaceView.setFocusableInTouchMode(true);
        mHolder  = surfaceView.getHolder();
        path = new Path();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4.0f);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        LogUtils.logE(TAG,"ACTION_DOWN");
                        path.moveTo(event.getX(),event.getY());
                        preX = event.getX();
                        preY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        LogUtils.logE(TAG,"ACTION_MOVE");
                        float curX = event.getX();
                        float curY = event.getY();
                        float dX = Math.abs((curX-preX));
                        float dY = Math.abs((curY-preY));
                        if (dX>=3||dY>=3){
                            path.quadTo(preX,preY,(preX+curX)/2,(preY+curY)/2);
                        }
                        preX = event.getX();
                        preY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        LogUtils.logE(TAG,"ACTION_UP");
                        path.lineTo(preX,preY);
                        preX = event.getX();
                        preY = event.getY();
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtils.logE(TAG,"surfaceCreated");
        mIsDrawing = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start =System.currentTimeMillis();
                while(mIsDrawing){
                    draw();
//                    long end = System.currentTimeMillis();
//                    if(end-start<100){
//                        try{
//                            Thread.sleep(100-end+start);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            }
        }).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtils.logE(TAG,"surfaceChanged");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.logE(TAG,"surfaceDestroyed");
        mIsDrawing = false;
    }

    //绘图操作
    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(path,paint);
            // draw sth绘制过程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);//保证每次都将绘图的内容提交
        }
    }

}
