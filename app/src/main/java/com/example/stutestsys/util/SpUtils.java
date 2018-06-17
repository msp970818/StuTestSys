package com.example.stutestsys.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

    //定义一个SharePreferences
    SharedPreferences sp;

    public SpUtils (Context context,String fileName){

        sp = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
    }

    public static class ContentValue{
        String key;
        Object value;

        //通过构造方法来传入Key和value
        public ContentValue(String key,Object value) {
            this.key = key;
            this.value = value;
        }
    }

    //一次可以传入多个ContentValue对象的值
    public void putValue(ContentValue... contentValues){
        SharedPreferences.Editor editor = sp.edit();
        //数据分类存储
        for (ContentValue contentValue : contentValues){
            //字符型
            if (contentValue.value instanceof String){
                editor.putString(contentValue.key,contentValue.value.toString()).commit();
            }
            //int
            if (contentValue.value instanceof Integer){
                editor.putInt(contentValue.key,Integer.parseInt(contentValue.value.toString())).commit();
            }
            //long
            if (contentValue.value instanceof Long){
                editor.putLong(contentValue.key,Long.parseLong(contentValue.value.toString())).commit();
            }
            //布尔型
            if (contentValue.value instanceof Boolean){
                editor.putBoolean(contentValue.key,Boolean.parseBoolean(contentValue.value.toString())).commit();
            }
        }
    }

    //读取数据的方法
    public String getString(String key){
        return sp.getString(key,null);
    }

    public Boolean getBoolean (String key,Boolean value){
        return sp.getBoolean(key,value);
    }

    public Long getLong (String key){
        return sp.getLong(key,-1);
    }
    public int getInt(String key){
        return sp.getInt(key,-1);
    }


    //清楚当前文件的所有数据
    public void clear(){
        sp.edit().clear().commit();
    }

}
