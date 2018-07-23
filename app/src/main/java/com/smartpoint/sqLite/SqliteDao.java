package com.smartpoint.sqLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.smartpoint.MyApplication;
import com.smartpoint.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/28
 * 邮箱 18780569202@163.com
 */
public class SqliteDao implements ISqliteLogic{
    private MySqliteOpenHelper helper;
    private SQLiteDatabase db;//数据库
    public SqliteDao(String tableName) {
        helper = new MySqliteOpenHelper(MyApplication.newInstance(),tableName);
        boolean a = (helper==null);
        Log.e("ost","helper为空-->"+a);
        db  = helper.getWritableDatabase();
        //
    }

    @Override
    public void add(User user) {
        ContentValues values = new ContentValues();
        values.put(SqlString.NAME,user.getName());
        values.put(SqlString.AGE,user.getAge());
        db.insert(SqlString.USER_TABLE,"id",values);
    }

    @Override
    public List<User> queryByName(String name) {
        String queryString = "select * from  user where name = ? ";
        Cursor cursor = db.rawQuery(queryString,new String[]{name});
        List<User> list = new ArrayList<>();
        while (cursor.moveToNext()){
            String name1 = cursor.getString(cursor.getColumnIndex(SqlString.NAME));
            String age1 = cursor.getString(cursor.getColumnIndex(SqlString.AGE));
            list.add(new User(name1,age1));
        }
        return list;
    }

    @Override
    public void delete(String name) {
        db.delete("user","name = ?",new String[]{name});
    }

    @Override
    public void deleteAll() {
        db.execSQL("delete from user");
    }

    @Override
    public List<User> queryAll() {
        String queryString = "select * from user";
        Cursor cursor = db.rawQuery(queryString,new String[]{});
        List<User> list = new ArrayList<>();
        while (cursor.moveToNext()){
            String name1 = cursor.getString(cursor.getColumnIndex(SqlString.NAME));
            String age1 = cursor.getString(cursor.getColumnIndex(SqlString.AGE));
            list.add(new User(name1,age1));
        }
        return list;
    }
}
