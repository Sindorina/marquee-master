package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartpoint.adapter.PictureChooseAdapter;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.util.LogUtils;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

public class ChoosePictureActivity extends BaseActivity{
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ChoosePictureActivity.class);
        activity.startActivity(intent);
    }

    private TextView textView;//添加图片
    private RecyclerView recyclerView_pic;
    private PictureChooseAdapter adapter;
    private ArrayList<String> listPicture;
    @Override
    public int getContentViewId() {
        return R.layout.activity_choose_picture;
    }

    @Override
    public void beforeInitView() {
        listPicture =new ArrayList<>();
    }

    @Override
    public void initView() {
        textView = findViewByIdNoCast(R.id.textView);
        textView.setOnClickListener(this);
        recyclerView_pic = findViewByIdNoCast(R.id.recyclerView_pic);
        adapter = new PictureChooseAdapter(listPicture,R.layout.choose_picture_item);
        recyclerView_pic.setLayoutManager(new GridLayoutManager(this,5));
        recyclerView_pic.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView:
                PhotoPicker.builder()
                        .setPhotoCount(10)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(ChoosePictureActivity.this, PhotoPicker.REQUEST_CODE);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                for (String str:photos){
                    LogUtils.logE("ChoosePictureActivity","图片路径-->"+str);
                }
                //adapter.getContacts().clear();
                adapter.getContacts().addAll(photos);
                adapter.notifyDataSetChanged();
            }
        }
    }

}
