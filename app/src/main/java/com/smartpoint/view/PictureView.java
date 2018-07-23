package com.smartpoint.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/6/5
 * 邮箱 18780569202@163.com
 */
public class PictureView extends View {
    private Paint paint;
    private Path path;
    private Timer timer;
    float i = 0;
    float x, y = 0;
    private Canvas mCanvas;
    private boolean isFirst = true;

    public PictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PictureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PictureView(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw();
        canvas.drawPath(path, paint);
    }

    private void init() {
        path = new Path();
        //背景圆画笔
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1.0f);
    }

    private void draw() {
        timer = new Timer();
        if (isFirst){
            path.moveTo(0, 400);
        }
        timer.schedule(new MyTimerTask(), 0, 1000);

    }

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(200);
        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 200) {
                if (i <= (Math.PI) * 10) {
                    isFirst = false;
                    y = 40 * (float) Math.sin(i) + 400;
                    path.lineTo(40 * i, y);
                    i += 0.02;
                    invalidate();
                } else {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    }

                }
            }
            return false;
        }
    });

}
