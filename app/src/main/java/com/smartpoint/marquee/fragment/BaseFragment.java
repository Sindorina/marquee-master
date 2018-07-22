package com.smartpoint.marquee.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getResource(), container,false);
            beforeInitView();
            initView(rootView);
            initData();
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
    }

    protected <T extends View> T findViewByIdNoCast(int id) {
        return rootView == null ? null : (T) rootView.findViewById(id);
    }

    protected abstract int getResource();

    protected abstract void beforeInitView();

    protected abstract void initView(View rootView);

    protected abstract void initData();

    /**
     * 根据id设置击事件
     * @param ids
     */
    protected void setOnClick(int... ids) {

        for (int id : ids) {
            if (id != -1)
                findViewByIdNoCast(id).setOnClickListener(this);
        }

    }

    public void setOnClick(View... views) {
        for (View view : views)
            view.setOnClickListener(this);

    }

}
