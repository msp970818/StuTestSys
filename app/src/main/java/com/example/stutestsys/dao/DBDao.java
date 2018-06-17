package com.example.stutestsys.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBDao {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBDao(Context context) {
    }

    /**
     * add UserInfo List
     * @param userInfos
     */
    public void add(List<UserInfo> userInfos){
        //开始事务
        String sql = "insert into usertable values(null,?,?)";
        db.beginTransaction();
        for (UserInfo userInfo : userInfos){
            db.execSQL(sql,new Object[]{userInfo.account,userInfo.password});
        }
        //设置事务完成
        db.setTransactionSuccessful();
        //结束事务
        db.endTransaction();
    }

    /**
     * add UserInfo String.etc
     * @param username
     * @param userpassword
     */
    public void add(String username,String userpassword){
        String sql = "insert into usertable values(null,?,?)";
        db.beginTransaction();
        db.execSQL(sql,new Object[]{username,userpassword});
    }

    /**
     * query all userInfo return list
     * @return
     */
    public List<UserInfo> query(){
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        Cursor cursor = quertTheCursor();
        while (cursor.moveToNext()){
            UserInfo userInfo = new UserInfo();
            userInfo._id = cursor.getInt(cursor.getColumnIndex("_id"));
            userInfo.account = cursor.getString(cursor.getColumnIndex("account"));
            userInfo.password = cursor.getString(cursor.getColumnIndex("password"));
            userInfo.grade = cursor.getInt(cursor.getColumnIndex("grade"));
            userInfo.info = cursor.getString(cursor.getColumnIndex("info"));
            userInfos.add(userInfo);
        }
        cursor.close();
        return userInfos;
    }

    private Cursor quertTheCursor() {
        Cursor cursor = db.rawQuery("SELECT * FROM usertable",null);
        return cursor;
    }

    public void closeDB(){
        db.close();
    }


}
