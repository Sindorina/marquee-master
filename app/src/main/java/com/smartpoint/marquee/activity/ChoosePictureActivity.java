package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.photoPicker.NineGridView;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

public class ChoosePictureActivity extends BaseActivity{
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ChoosePictureActivity.class);
        activity.startActivity(intent);
    }

    private TextView textView;//添加图片
    private NineGridView grid;
    @Override
    public int getContentViewId() {
        return R.layout.activity_choose_picture;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        textView = findViewByIdNoCast(R.id.textView);
        grid = findViewByIdNoCast(R.id.grid);
        textView.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView:
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(ChoosePictureActivity.this, PhotoPicker.REQUEST_CODE);
                break;
        }
    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                grid.setUrlList(photos);
            }
        }
    }

}
