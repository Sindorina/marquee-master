package com.smartpoint.retrofit;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class BaseObserver<T> implements Observer<T>,ProgressCancelListener {
    private static final String TAG = "BaseObserver";
    private Context mContext;
    private ProgressDialogHandler mProgressDialogHandler;
    private Disposable d;
    protected BaseObserver(Context context) {
        this.mContext = context.getApplicationContext();
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }
    //显示dialog
    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }
    //关闭dialog
    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)
                    .sendToTarget();
            mProgressDialogHandler = null;
        }
    }
    @Override
    public void onSubscribe(Disposable d) {
        showProgressDialog();
        this.d = d;
    }

    @Override
    public void onNext(T value) {
        onHandleSuccess(value);
        Log.d(TAG,"结果-->"+value.toString());
//        if (value.isSuccess()) {
//            T t = value.getData();
//            Log.e("BaseObserver","结果-->"+t.toString());
//
//            //onHandleSuccess((T) TEST2);
//        } else {
//            onHandleError(value.getErrmsg());
//        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError:" + e.toString());
        dismissProgressDialog();
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete");
        dismissProgressDialog();
    }

    protected abstract void onHandleSuccess(T t);

    protected void onHandleError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelProgress() {
        //如果处于订阅状态，则取消订阅
        if (!d.isDisposed()) {
            d.dispose();
        }
    }
}
