package com.smartpoint.sqLite;

import com.smartpoint.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28
 * 邮箱 18780569202@163.com
 */
public interface ISqliteLogic {
    //添加数据到数据库
    void add(User user);
    //通过名字查询数据数据
    List<User> queryByName(String name);
    //通过名字删除
    void delete(String name);
    //清空所有数据
    void deleteAll();
    //查询所有数据
    List<User> queryAll();
}
