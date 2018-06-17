package com.example.stutestsys.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.audiofx.BassBoost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stutestsys.R;
import com.example.stutestsys.dao.DBHelper;
import com.example.stutestsys.util.Base64Utils;
import com.example.stutestsys.util.ConstantValue;
import com.example.stutestsys.util.SpUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "LoginActivity";
    private EditText login_name,login_password;
    private Button login_button,register_button,back;
    private CheckBox remember_password,autologin;
    private ImageView see_password;
    private DBHelper dbHelper;
    private String account, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initData();
        setupEvents();

    }

    private void initData() {

        //1.判断是否第一次登陆
        if(firstLogin()){
            //true，那2个checkbox不能勾选，so是false
            remember_password.setChecked(false);
            autologin.setChecked(false);
        }

        //2.判断是否记住密码
        if (rememberPassword()){
            //if(true)
            //勾选记住密码
            remember_password.setChecked(true);
            //把密码和账户输入到相应的输入框内
            setTextAccountAndPassword();
        }else {
            //否则只把账号放在输入框
            setTextAccount();
        }
        //3.判断是否自动登录
        if (autoLogin()){
            autologin.setChecked(true);
            readUserInfo();
            loadCheckBoxState();
        }
    }

    /**
     * @return boolean类型    返回存在本地的自动登录的状态
     */
    private boolean autoLogin() {
        SpUtils helper = new SpUtils(this,"setting");
        boolean autoLogin = helper.getBoolean(ConstantValue.AUTOLOGIN,false);
        return autoLogin;
    }

    /**
     * 只setName，不setpassword
     */
    private void setTextAccount() {
        login_name.setText(""+getLocalAccount());
    }

    private void setTextAccountAndPassword() {
        login_name.setText(""+getLocalAccount());
        login_password.setText(""+getLocalPassword());

    }



    private boolean firstLogin() {
        SpUtils helper = new SpUtils(this,"setting");
        //
        boolean first = helper.getBoolean(ConstantValue.FIRST,true);
        if (first){
            helper.putValue(new SpUtils.ContentValue(ConstantValue.FIRST,false),
                    new SpUtils.ContentValue(ConstantValue.REMEMBERPASSWORD, false),
                    new SpUtils.ContentValue(ConstantValue.AUTOLOGIN, false),
                    new SpUtils.ContentValue(ConstantValue.NAME, ""),
                    new SpUtils.ContentValue(ConstantValue.PASSWORD, ""));
            return true;
        }
        return false;
    }

    private void initViews(){
        login_name = (EditText) findViewById(R.id.edit_name);
        login_password = (EditText) findViewById(R.id.edit_password);
        remember_password = (CheckBox) findViewById(R.id.remember_password);
        autologin = (CheckBox) findViewById(R.id.checkBox_autologin);
        see_password = (ImageView) findViewById(R.id.iv_see_password);
        login_button = (Button) findViewById(R.id.login_button);
        back = (Button) findViewById(R.id.back_button);
        login_button.setOnClickListener(this);
        register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(this);
        back.setOnClickListener(this);
        see_password.setOnClickListener(this);
        remember_password.setOnCheckedChangeListener(this);
        autologin.setOnCheckedChangeListener(this);
        dbHelper = new DBHelper(this,"Data.db",null,1);
    }

    private void setupEvents (){
        login_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    login_name.clearFocus();
                }
                return false;
            }
        });
        login_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    login_password.clearFocus();
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(login_password.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                if (login_name.getText().toString().trim().equals("") | login_password.getText().toString().trim().equals("")){
                    Toast.makeText(this, "请输入账号或者注册账号！", Toast.LENGTH_SHORT).show();
                }else {
                    //读取Data.db 的usertable表信息
                    readUserInfo();
                    //保存一下,checkbox信息和账户、密码，到本地
                    loadCheckBoxState();
                    //关闭loginactivity
//                    finish();
                }
                break;
            case R.id.register_button:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_see_password:
                if (see_password.isSelected()){
                    see_password.setSelected(false);
                    //密码不可见
                    login_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else {
                    see_password.setSelected(true);
                    //密码可见
                    login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            case R.id.back_button:
                finish();
                break;
        }
    }

    /**
     * 读取UserData.db中的用户信息
     * */
    protected void readUserInfo() {

        String account,password;
        account =  login_name.getText().toString();
        password = login_password.getText().toString();
        if (login(account,password)) {
            Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("Username",login_name.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "账户或密码错误，请重新输入！！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *  读取Checkbox的状态
     */
    private void loadCheckBoxState() {
        saveCheckBoxState(remember_password,autologin );
    }

    /**
     * 保存记住密码和自动登录的CheckBox状态
     */
    private void saveCheckBoxState(CheckBox rememeber_password,CheckBox autologin) {
        //获取sp对象，使用自定义类的方法来获取对象
        SpUtils helper = new SpUtils(this,"setting");
        String account = login_name.getText().toString();
        String password = login_password.getText().toString();
        //if设置自动登录
        if (autologin.isChecked()){
            //创建记住密码和自动登录都是选择的，保存密码数据
            helper.putValue(
                    new SpUtils.ContentValue(ConstantValue.REMEMBERPASSWORD,true),
                    new SpUtils.ContentValue(ConstantValue.AUTOLOGIN,true));
            SaveAccountAndPassword(account,password);
        }else if (!rememeber_password.isChecked()){
            //如果没有记住密码，自动登录也不选，密码为空
            helper.putValue(
                    new SpUtils.ContentValue(ConstantValue.AUTOLOGIN,false),
                    new SpUtils.ContentValue(ConstantValue.REMEMBERPASSWORD,false),
                    new SpUtils.ContentValue(ConstantValue.PASSWORD,"")
            );
        }else if (rememeber_password.isChecked()){
            //如果记住密码但是没有自动登录
            helper.putValue(
                    new SpUtils.ContentValue(ConstantValue.REMEMBERPASSWORD,true),
                    new SpUtils.ContentValue(ConstantValue.AUTOLOGIN,false)
            );
            //保存账号和密码
            SaveAccountAndPassword(account,password);
        }
    }

    /**
     * 保存账号和密码
     */
    private void SaveAccountAndPassword(String account, String password){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "Select * from usertable where account=? and password=?";
        Cursor cursor = db.rawQuery(sql,new String[]{account,password});
        if (cursor.moveToFirst()){
            do {
                //遍历Cursor 对象，取出数据并打印
                account = cursor.getString(cursor.getColumnIndex("account"));
                password = cursor.getString(cursor.getColumnIndex("password"));
                Log.d(TAG, "account :" + account);
                Log.d(TAG, "password" + password);
                SpUtils helper = new SpUtils(this,"setting");
                helper.putValue(new SpUtils.ContentValue(ConstantValue.NAME,account));
                helper.putValue(new SpUtils.ContentValue(ConstantValue.PASSWORD, Base64Utils.encryptBASE64(password)));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    /**
     * 获取保存在本地的用户名
     * @return 返回本地保存的用户名
     */
    public String getLocalAccount() {
        SpUtils helper = new SpUtils(this,"setting");
        String account = helper.getString(ConstantValue.NAME);
        return account;
    }

    /**
     *  获取保存在本地的密码
     *  @return 返回本地的密码
     */
    public String getLocalPassword() {
        SpUtils helper = new SpUtils(this,"setting");
        String password = helper.getString(ConstantValue.PASSWORD);
//        return password;
        //用Base64进行解码
        return Base64Utils.decryptBASE64(password);
    }

    private boolean rememberPassword() {
        SpUtils helper = new SpUtils(this,"setting");
        boolean rememberPassword = helper.getBoolean(ConstantValue.REMEMBERPASSWORD,false);
        return rememberPassword;
    }

    /**
     * 验证登录信息
     * */
    public boolean login(String account, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "Select * from usertable where account=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[]{account, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView == remember_password){//记住密码选框发生改变时

            if (!isChecked){//取消记住密码时，同时取消自动登录
                autologin.setChecked(false);
            }
        }else if (buttonView == autologin){ //自动登录发生改变时,同时选中 记住密码
            if (isChecked){
                remember_password.setChecked(true);
            }

        }
    }
}
