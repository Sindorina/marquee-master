package com.smartpoint.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/18
 * 邮箱 18780569202@163.com
 */
public class MyDrawView extends View implements View.OnTouchListener{
   private List<MyDrawInfo> list;
    public MyDrawView(Context context) {
        super(context);
        init();
    }
    public MyDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i=0;i<list.size();i++){
            if (list.get(i).path!=null){
                canvas.drawPath(list.get(i).path,list.get(i).paint);
            }
        }
        if (path!=null){
            canvas.drawPath(path,paint);
        }
    }
    private void init(){
        list = new ArrayList<>();
        this.setOnTouchListener(this);
        setPaintStyle(Color.GREEN);
    }

    /**
     * 设置画笔样式
     */
    public void setPaintStyle(int color){
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        //设置抗锯齿
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(5);
    }
    private Paint paint;
    private Path path;
    private float preX,preY;
    private final static float LIMITED = 3;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction()==MotionEvent.ACTION_DOWN){
            path = new Path();
            path.moveTo(event.getX(),event.getY());
            preX = event.getX();
            preY = event.getY();
        }else if (event.getAction()==MotionEvent.ACTION_MOVE){
            float curX = event.getX();
            float curY = event.getY();
            float dX = Math.abs((curX-preX));
            float dY = Math.abs((curY-preY));
            if (dX>=LIMITED||dY>=LIMITED){
                path.quadTo(preX,preY,(preX+curX)/2,(preY+curY)/2);
            }
            preX = event.getX();
            preY = event.getY();
        }else if (event.getAction()==MotionEvent.ACTION_UP){
            path.lineTo(event.getX(),event.getY());
            preX = event.getX();
            preY = event.getY();
            list.add(new MyDrawInfo(paint,path));
        }
        invalidate();
        return true;
    }
    public class MyDrawInfo{
        public Paint paint;
        public Path path;

        public MyDrawInfo(Paint paint, Path path) {
            this.paint = paint;
            this.path = path;
        }
    }
    //清除
    public void clear(){
        path = new Path();
        list.clear();
        invalidate();
    }

}
