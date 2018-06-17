package com.example.stutestsys.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Ma on 2018/5/31.
 */

public class DBHelper extends SQLiteOpenHelper {

    private final static int DB_VERSION = 1;

    private final static String DATABASE_NAME = "Data.db";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        String usersInfo_table = "create table usertable(" +
                "id integer primary key autoincrement," +
                "account text," +
                "password text," +
                "name text," +
                "grade integer);";
        db.execSQL(usersInfo_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table usertable add column other string");
    }
}
