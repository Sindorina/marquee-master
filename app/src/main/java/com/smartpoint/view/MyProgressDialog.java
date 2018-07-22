package com.smartpoint.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.smartpoint.marquee.R;

public class MyProgressDialog extends ProgressDialog {
    public MyProgressDialog(Context context) {
        super(context);
    }
    public MyProgressDialog(Context context,int theme) {
        super(context,theme);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }
    private void init(Context context) {
        setContentView(R.layout.progressbar);//loading的xml文件
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }
}
