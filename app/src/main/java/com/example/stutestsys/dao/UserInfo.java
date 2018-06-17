package com.example.stutestsys.dao;

class UserInfo {

    public int _id;     //id
    public String name;     //名字
    public String account;     //用户名
    public String password;     //密码
    public int grade;         //年级
    public String info;     //信息

    public UserInfo() {
    }

    public UserInfo(String account) {
        this.account = account;
    }

    public UserInfo(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public UserInfo(String account, int grade, String info) {
        this.account = account;
        this.grade = grade;
        this.info = info;
    }

    public UserInfo(String account, String password, int grade, String info) {
        this.account = account;
        this.password = password;
        this.grade = grade;
        this.info = info;
    }
}
