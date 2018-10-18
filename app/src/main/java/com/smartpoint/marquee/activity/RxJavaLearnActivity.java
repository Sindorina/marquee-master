package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smartpoint.adapter.RefreshAdapter;
import com.smartpoint.entity.Movie;
import com.smartpoint.entity.Subjects;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.retrofit.ApiMethod;
import com.smartpoint.retrofit.BaseObserver;
import com.smartpoint.retrofit.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxJavaLearnActivity extends BaseActivity {
    private static final String TAG = "RxJavaLearnActivity";
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private RefreshAdapter adapter;
    private List<String> list;
    private Button btn;
    private Disposable disposable;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, RxJavaLearnActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_rxjava;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        initRefresh();
        btn = findViewByIdNoCast(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getData();
        //test();
        //test2();
    }

    private void getData() {
        Observable<Movie> observable = RetrofitFactory.getInstance().getMovieInfo(0,10);
        ApiMethod.ApiSubscribe(observable, new BaseObserver<Movie>(this) {
            @Override
            protected void onHandleSuccess(Movie movie) {
                Log.d(TAG, "onNext: " + movie.getTitle());
                List<Subjects> list = movie.getSubjects();
                List<String> res = new ArrayList<>();
                for (Subjects sub : list) {
                    res.add(sub.getId());
                    res.add(sub.getYear());
                    res.add(sub.getTitle());
                }
                adapter.getContacts().addAll(res);
                adapter.notifyDataSetChanged();
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
            }
        });
    }


    private void test() {
        //被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("数据1传送");
            }
        });
        //观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.e(TAG,"数据-->"+s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    private void test2(){
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                List<String> list = new ArrayList<>();
                list.add("江畔何人初见月？江月何年初照人？");
                list.add("人生代代无穷已，江月年年只相似。");
                list.add("不知江月待何人，但见长江送流水。");
                list.add("白云一片去悠悠，青枫浦上不胜愁。");
                list.add("谁家今夜扁舟子？何处相思明月楼？");
                list.add("可怜楼上月徘徊，应照离人妆镜台。");
                list.add("玉户帘中卷不去，捣衣砧上拂还来。");
                list.add("此时相望不相闻，愿逐月华流照君。");
                e.onNext(list);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        adapter.getContacts().addAll(strings);
                        adapter.notifyDataSetChanged();
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.finishLoadMore();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void initRefresh(){
        list = new ArrayList<>();
        recyclerView = findViewByIdNoCast(R.id.recyclerView);
        smartRefreshLayout = findViewByIdNoCast(R.id.smartRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RefreshAdapter(list,R.layout.find_pop_item);
        recyclerView.setAdapter(adapter);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                adapter.getContacts().clear();
                test2();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                test2();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                disposable = Flowable.intervalRange(0,11,0,1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                btn.setText("重新获取(" + (10 - aLong) + ")");
                                btn.setEnabled(false);
                            }
                        }).doOnComplete(new Action() {
                            @Override
                            public void run() throws Exception {
                                //倒计时完毕置为可点击状态
                                btn.setEnabled(true);
                                btn.setText("获取验证码");
                            }
                        }).subscribe();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null){
            disposable.dispose();
        }
    }
}

