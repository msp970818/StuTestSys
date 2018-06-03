package com.example.stutestsys.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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

import com.example.stutestsys.MainActivity;
import com.example.stutestsys.R;
import com.example.stutestsys.dao.DBHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
//        ,CompoundButton.OnCheckedChangeListener
{

    private EditText login_name,login_password;
    private Button login_button,back,register_button;
    private CheckBox rememeber_password,autologin;
    private ImageView see_password;
    private DBHelper dbHelper;
    private String name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setupEvents();
        }


    private void initViews(){
        login_name = (EditText) findViewById(R.id.edit_name);
        login_password = (EditText) findViewById(R.id.edit_password);
        rememeber_password = (CheckBox) findViewById(R.id.remember_password);
        autologin = (CheckBox) findViewById(R.id.checkBox_autologin);
        see_password = (ImageView) findViewById(R.id.iv_see_password);
        login_button = (Button) findViewById(R.id.login_button);
        back = (Button) findViewById(R.id.bt_back);
        login_button.setOnClickListener(this);
        register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(this);
//        rememeber_password.setOnCheckedChangeListener(this);
//        autologin.setOnCheckedChangeListener(this);
//        see_password.setOnClickListener(this);

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
                if (login_name.getText().toString().trim().equals("") |
                        login_password.getText().toString().trim().equals("")){
                    Toast.makeText(this, "请输入账号或者注册账号！", Toast.LENGTH_SHORT).show();
                }else {
                    readUserInfo();
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
                    login_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else {
                    see_password.setSelected(true);
                    //密码可见
                    login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
        }
    }

    /**
     * 读取SharedPreferences存储的键值对
     * */
    public void readUsersInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("UsersInfo",MODE_PRIVATE);
        name = sharedPreferences.getString("username","");
        password = sharedPreferences.getString("password","");
    }

    /**
     * 读取UserData.db中的用户信息
     * */
    protected void readUserInfo() {
        if (login(login_name.getText().toString(), login_password.getText().toString())) {
            Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("Username",login_name.getText().toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "账户或密码错误，请重新输入！！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 验证登录信息
     * */
    public boolean login(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "Select * from usertable where username=? and password=?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }
}
