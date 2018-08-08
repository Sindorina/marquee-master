package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.smartpoint.adapter.VideoShowAdapter;
import com.smartpoint.entity.FileInfo;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/7
 * 邮箱 18780569202@163.com
 */
public class VideoShowActivity extends BaseActivity{
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, VideoShowActivity.class);
        activity.startActivity(intent);
    }
    private RecyclerView recyclerView;
    private VideoShowAdapter adapter;
    @Override
    public int getContentViewId() {
        return R.layout.activity_video_show;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        recyclerView = findViewByIdNoCast(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoShowAdapter(queryAllVideo(this),R.layout.video_show_item);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClick(new VideoShowAdapter.OnItemClick() {
            @Override
            public void clickItem(int position) {
              String path =  adapter.getFileInfos().get(position).getPath();
              if (!TextUtils.isEmpty(path)){
                  MediaPlayActivity.start(VideoShowActivity.this,path);
              }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
    public ArrayList<FileInfo> queryAllVideo(final Context context) {
        if (context == null) { //判断传入的参数的有效性
            return null;
        }
        ArrayList<FileInfo> videos = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            //查询数据库，参数分别为（路径，要查询的列名，条件语句，条件参数，排序）
            cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null ,null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    FileInfo video = new FileInfo();
                    video.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID))); //获取唯一id
                    video.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))); //文件路径
                    video.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))); //文件名
                    //...   还有很多属性可以设置
                    //可以通过下一行查看属性名，然后在Video.Media.里寻找对应常量名
                    //Log.i(TAG, "queryAllImage --- all column name --- " + cursor.getColumnName(cursor.getPosition()));

                    //获取缩略图（如果数据量大的话，会很耗时——需要考虑如何开辟子线程加载）
                    /*
                     * 可以访问android.provider.MediaStore.Video.Thumbnails查询图片缩略图
                     * Thumbnails下的getThumbnail方法可以获得图片缩略图，其中第三个参数类型还可以选择MINI_KIND
                     */
                    videos.add(video);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return videos;
    }
}
