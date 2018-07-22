package com.smartpoint.marquee.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartpoint.entity.User;
import com.smartpoint.marquee.R;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/4/28
 * 邮箱 18780569202@163.com
 */
public class LitePalActivity extends AppCompatActivity implements View.OnClickListener{
    private SQLiteDatabase db;
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, LitePalActivity.class);
        activity.startActivity(intent);
    }

    Button buttonSave,buttonRead,buttonReadAll,buttonDel,buttonDelAll;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        checkPermission();
        initView();
        db = Connector.getDatabase();
    }
    private void initView(){
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSave://存
                List<User> list3 = new ArrayList<>();
                User user = new User("小昭","22");
                User user4 = new User("小昭","22");
                User user2 = new User("小王","25");
                list3.add(user);list3.add(user4);list3.add(user2);
                DataSupport.saveAll(list3);
                break;
            case R.id.buttonRead://取
                List<User> list2 = DataSupport.where("name = ?","小昭").find(User.class);
                StringBuilder sb = new StringBuilder();
                for (User user1:list2){
                    sb.append(user1.getName()).append("        ")
                            .append(user1.getAge()).append("        ");
                }
                textView.setText(sb.toString());
                break;
            case R.id.buttonReadAll://取所有数据
                List<User> list = DataSupport.findAll(User.class);
                StringBuilder sb2 = new StringBuilder();
                for (User user1:list){
                    sb2.append(user1.getName()).append("        ")
                            .append(user1.getAge()).append("        ");
                }
                textView.setText(sb2.toString());
                break;
            case R.id.buttonDelAll://删除表内所有所有数据
                DataSupport.deleteAll(User.class);
                break;
            case R.id.buttonDel://通过名字删除
                DataSupport.deleteAll(User.class,"name = ?","小昭");
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
