package com.smartpoint.marquee.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartpoint.entity.User;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.sqLite.SqliteDao;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/4/28
 * 邮箱 18780569202@163.com
 */
public class SQLiteActivity extends BaseActivity{
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SQLiteActivity.class);
        activity.startActivity(intent);
    }
    SqliteDao dao = new SqliteDao("user");
    Button buttonSave,buttonRead,buttonReadAll,buttonDel,buttonDelAll;
    TextView textView;
    @Override
    public int getContentViewId() {
        return R.layout.activity_sqlite;
    }

    @Override
    public void beforeInitView() {
        checkPermission();
    }
    @Override
    public void initView(){
        buttonReadAll = findViewById(R.id.buttonReadAll);
        buttonRead = findViewById(R.id.buttonRead);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDel = findViewById(R.id.buttonDel);
        buttonDelAll = findViewById(R.id.buttonDelAll);
        textView = findViewById(R.id.textView);
        buttonRead.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonDel.setOnClickListener(this);
        buttonDelAll.setOnClickListener(this);
        buttonReadAll.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSave://存
                User user = new User("小昭","20");
                User user1 = new User("小王","21");
                User user2 = new User("小里","22");
                dao.add(user);dao.add(user1);dao.add(user2);
                break;
            case R.id.buttonRead://取
                StringBuilder builder = new StringBuilder();
                List<User> list = dao.queryByName("小昭");
                if (list.size()==0){
                    textView.setText("");
                    Toast.makeText(this,"未查询到相关数据",Toast.LENGTH_SHORT).show();
                    return;
                }
                for (User user3:list){
                    builder.append(user3.getName())
                            .append("    ").append(user3.getAge())
                    .append("    ");
                }
                textView.setText(builder.toString());
                break;
            case R.id.buttonReadAll://取所有数据
                StringBuilder builder1 = new StringBuilder();
                List<User> list1 = dao.queryAll();
                if (list1.size()==0){
                    textView.setText("");
                    Toast.makeText(this,"未查询到相关数据",Toast.LENGTH_SHORT).show();
                    return;
                }
                for (User user3:list1){
                    builder1.append(user3.getName())
                            .append("    ").append(user3.getAge())
                            .append("    ");
                }
                textView.setText(builder1.toString());
                break;
            case R.id.buttonDelAll://删除表内所有所有数据
                dao.deleteAll();
                break;
            case R.id.buttonDel://通过名字删除
                dao.delete("小昭");
                break;
        }
    }
    private void checkPermission(){
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_SETTINGS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted){
                        }else if (permission.shouldShowRequestPermissionRationale){
                            return;
                        }else {
                            return;
                        }
                    }
                });
    }

}
