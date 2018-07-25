package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.smartpoint.adapter.SideDeleteAdapter;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.util.LogUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/24
 * 邮箱 18780569202@163.com
 */
public class SideDeleteActivity extends BaseActivity{
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SideDeleteActivity.class);
        activity.startActivity(intent);
    }
    private SwipeMenuRecyclerView recyclerView;
    private SideDeleteAdapter adapter;
    private SmartRefreshLayout smartRefreshLayout;
    @Override
    public int getContentViewId() {
        return R.layout.activity_litepal;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        findViewByIdNoCast(R.id.back).setOnClickListener(this);
        recyclerView = findViewByIdNoCast(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SideDeleteAdapter(getData(),R.layout.find_pop_item);
        // 设置监听器。
        recyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        // 菜单点击监听。
        recyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
        recyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO，搞事情...
                Toast.makeText(SideDeleteActivity.this,"点击了"+position+"项",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    // 测试数据
    public List<String> getData() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add("测试"+i);
        }
        return result;
    }
    // 创建菜单：
    SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            SwipeMenuItem deleteItem = new SwipeMenuItem(SideDeleteActivity.this);
//            // 各种文字和图标属性设置。
//            leftMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。
            // 各种文字和图标属性设置。
            SwipeMenuItem deleteItem = new SwipeMenuItem(SideDeleteActivity.this)
                    .setBackground(R.drawable.selector_red)
                    .setImage(R.mipmap.ic_action_delete)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            rightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

            SwipeMenuItem addItem = new SwipeMenuItem(SideDeleteActivity.this)
                    .setBackground(R.drawable.selector_green)
                    .setText("添加")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            rightMenu.addMenuItem(addItem); // 添加菜单到右侧。
            // 注意：哪边不想要菜单，那么不要添加即可。
        }
    };

    SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            LogUtils.logE("SideDeleteActivity","direction-->"+direction);
            LogUtils.logE("SideDeleteActivity","adapterPosition-->"+adapterPosition);
            LogUtils.logE("SideDeleteActivity","menuPosition-->"+menuPosition);
            if (menuPosition==0){//删除
                adapter.getContacts().remove(adapterPosition);
                adapter.notifyItemRemoved(adapterPosition);
            }else if (menuPosition==1){//添加
                adapter.getContacts().add("测试"+(adapter.getItemCount()));
                adapter.notifyDataSetChanged();
            }
        }
    };

}
