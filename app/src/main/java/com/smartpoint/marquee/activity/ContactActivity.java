package com.smartpoint.marquee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.gjiazhe.wavesidebar.WaveSideBar;
import com.smartpoint.adapter.ContactsAdapter;
import com.smartpoint.entity.Contact;
import com.smartpoint.marquee.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/27
 * 邮箱 18780569202@163.com
 */
public class ContactActivity extends AppCompatActivity {
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, ContactActivity.class);
        activity.startActivity(intent);
    }
    private RecyclerView rvContacts;
    private WaveSideBar sideBar;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ContactsAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }
    private void initView() {
        setContentView(R.layout.activity_contact);
        rvContacts =  findViewById(R.id.rv_contacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactsAdapter(contacts, R.layout.item_contacts);
        rvContacts.setAdapter(adapter);
        sideBar =  findViewById(R.id.side_bar);
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i=0; i<contacts.size(); i++) {
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
                Toast.makeText(ContactActivity.this,res,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        contacts.addAll(Contact.getEnglishContacts());
    }
}
