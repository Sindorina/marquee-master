package com.smartpoint.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/4/28
 * 邮箱 18780569202@163.com
 */
public class User extends DataSupport{
    public User(String name, String age) {
        this.name = name;
        this.age = age;
    }

    private String name;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
