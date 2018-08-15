package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.gjiazhe.wavesidebar.WaveSideBar;
import com.smartpoint.adapter.ContactsAdapter;
import com.smartpoint.entity.Contact;
import com.smartpoint.marquee.R;
import com.smartpoint.marquee.base.BaseActivity;
import com.smartpoint.util.LogUtils;
import com.smartpoint.util.PingYinUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/4/27
 * 邮箱 18780569202@163.com
 */
public class ContactActivity extends BaseActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ContactActivity.class);
        activity.startActivity(intent);
    }
    private RecyclerView rvContacts;
    private WaveSideBar sideBar;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ContactsAdapter adapter;
    private String[] indexArray;
    private  char[] chars;
    private static final String TAG = "ContactActivity";
    @Override
    public int getContentViewId() {
        return R.layout.activity_contact;
    }

    @Override
    public void beforeInitView() {
        setData();
    }
    @Override
    public void initView() {
        setContentView(R.layout.activity_contact);
        rvContacts = findViewById(R.id.rv_contacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactsAdapter(contacts, R.layout.item_contacts);
        rvContacts.setAdapter(adapter);
        sideBar = findViewById(R.id.side_bar);
        sideBar.setIndexItems(distinctBySetOrder(indexArray));
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i = 0; i < contacts.size(); i++) {
                    if (contacts.get(i).getIndex().equals(index)) {
                        ((LinearLayoutManager) rvContacts.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
        adapter.setOnItemClick(new ContactsAdapter.OnItemClick() {
            @Override
            public void clickItem(int position) {
                String res = adapter.getContacts().get(position).getName();
                Toast.makeText(ContactActivity.this, res, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void initData() {
        contacts.addAll(Contact.getEnglishContacts());
    }

    private void setData() {
        List<String> list = new ArrayList<>();
        List<Contact> list2 = new ArrayList<>();
        list.add("北京");
        list.add("播放");
        list.add("人多");
        list.add("提示");
        list.add("一上线");
        list.add("欧典");
        list.add("李四");
        list.add("是否");
        list.add("奥迪");
        list.add("傲");
        list.add("北京");
        list.add("播放");
        list.add("人多");
        list.add("提示");
        list.add("一上线");
        list.add("欧典");
        list.add("李四");
        list.add("是否");
        list.add("奥迪");
        list.add("傲");
        indexArray = new String[list.size()];
        chars = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            char p = PingYinUtil.getSpells(list.get(i));
            chars[i] = p;
        }
        Arrays.sort(chars);
        for (int j=0;j<chars.length;j++){
            indexArray[j] = chars[j]+"";
            list2.add(new Contact(chars[j]+"",list.get(j)));
        }
        contacts.addAll(list2);
    }
    /**
     * 通过set去重, 不打乱原有list的顺序
     *       list中相同的对象会被去重复
     *
     * @param strings
     * @return String[]
     * */
    private  String[] distinctBySetOrder(String[] strings){
        Set<String> set = new HashSet<>();
        List<String> list = new ArrayList<>();
        for (int i=0;i<strings.length;i++) {
            if(set.add(strings[i])){
                list.add(strings[i]);
            }
        }
        return list.toArray(new String[list.size()]);
    }

    @Override
    public void onClick(View v) {

    }
}
