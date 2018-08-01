package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.smartpoint.marquee.R;
import com.smartpoint.surfaceView.MySurfaceViee;
import com.smartpoint.view.MySurfaceView;

/**
 * Created by Administrator on 2018/6/6
 * 邮箱 18780569202@163.com
 */
public class SurfaceViewDrawActivity2 extends AppCompatActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SurfaceViewDrawActivity2.class);
        activity.startActivity(intent);
    }
    private LinearLayout root;
    private LinearLayout.LayoutParams params;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_surface_view);
        root = findViewById(R.id.root);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_surface, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                MySurfaceViee surfaceViee = new MySurfaceViee(this);
                surfaceViee.setLayoutParams(params);
                root.addView(surfaceViee);
                return true;
            case R.id.item2:
                MySurfaceView surfaceView  = new MySurfaceView(this);
                surfaceView.setLayoutParams(params);
                root.addView(surfaceView);
                return true;
        }
        return true;
    }
}
