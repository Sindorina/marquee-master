package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.iwf.photopicker.PhotoPicker;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by Administrator on 2018/7/24
 * 邮箱 18780569202@163.com
 */
public class DownloadActivity extends BaseActivity{
    private Button blur;
    private TextView title;
    private ImageView imageView;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, DownloadActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_download;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        blur = findViewByIdNoCast(R.id.blur);
        blur.setOnClickListener(this);
        title = findViewByIdNoCast(R.id.title);
        findViewByIdNoCast(R.id.back).setOnClickListener(this);
        title.setText("下载页面");
        imageView = findViewByIdNoCast(R.id.imageView);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.blur:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(DownloadActivity.this, PhotoPicker.REQUEST_CODE);
                break;
            case R.id.back:
                finish();
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
                if (photos.size()>0){
                    MultiTransformation multi = new MultiTransformation(
                            new BlurTransformation(25),
                            new RoundedCornersTransformation(128, 0, RoundedCornersTransformation.CornerType.ALL));
                    Glide.with(this).load(photos.get(0))
                            .apply(bitmapTransform(multi))
                            .into(imageView);
                }
            }
        }
    }
}
