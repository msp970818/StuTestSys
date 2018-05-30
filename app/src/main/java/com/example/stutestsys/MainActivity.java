package com.example.stutestsys;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;

public class MainActivity extends AppCompatActivity {

    private int index;//点击的fragment的下标
    private int currentTabIndex=0;//当前的fragment的下标
    private int number=3;//显示的数字

    private BottomNavigationBar mBottomNavigationBar;
    private OneFragment mOneFragment;
    private TwoFragment mTwoFragment;
    private ThreeFragment mThreeFragment;
    private FourFragment mFourFragment;

    private Fragment[] mFragments;
    private TextBadgeItem mTextBadgeItem;
    private ShapeBadgeItem mShapeBadgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initBottomNavigationBar();
        initListener();

    }

    private void initListener() {

        findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {//数字++事件
            @Override
            public void onClick(View v) {
                if (number<0){
                    mTextBadgeItem.hide();
                    number=1;
                }else {
                    number++;
                    mTextBadgeItem.setText(number+"").show();
                }
            }
        });
        findViewById(R.id.bt_cut).setOnClickListener(new View.OnClickListener() {//数字--事件
            @Override
            public void onClick(View v) {
                if (number<0){
                    mTextBadgeItem.hide();
                    number=0;
                }else {
                    mTextBadgeItem.setText(number+"").show();
                    number--;
                }
            }
        });

        mBottomNavigationBar //设置lab点击事件
                .setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(int position) {
                        switch (position){
                            case 0:
                                index = 0;
                                break;
                            case 1:
                                index = 1;
                                break;
                            case 2:
                                index = 2;
                                break;
                            case 3:
                                index = 3;
                                break;
                        }
                        if (index == 2) {
                            startActivity(new Intent(MainActivity.this, ChatActivity.class));
                            return;
                        }
                        if (currentTabIndex != index) {
                            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                            trx.hide(mFragments[currentTabIndex]);
                            if (!mFragments[index].isAdded()) {
                                trx.add(R.id.fl, mFragments[index]);
                            }
                            trx.show(mFragments[index]).commit();
                        }
                        currentTabIndex = index;
//                        Toast.makeText(MainActivity.this,"onTabSelected"+position,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTabUnselected(int position) {
                        if (index==2){
                            mBottomNavigationBar.selectTab(position);
                        }
//                        Toast.makeText(MainActivity.this,"onTabUnselected"+position,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTabReselected(int position) {
//                        Toast.makeText(MainActivity.this,"onTabReselected"+position,Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void initBottomNavigationBar() {
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);

        mBottomNavigationBar //值得一提，模式跟背景的设置都要在添加tab前面，不然不会有效果。
                .setActiveColor(R.color.green);//选中颜色 图标和文字
//                .setInActiveColor("#8e8e8e")//默认未选择颜色
//                .setBarBackgroundColor(R.color.white);//默认背景色

        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.homepage_click,"首页")
                        .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.homepage_normal))
                        .setBadgeItem(mShapeBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.grown_wall_click,"成长墙")
                        .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.grown_wall_normal)))
                .addItem(new BottomNavigationItem(R.drawable.study_click,"学管通")
                        .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.study_normal))
                        .setBadgeItem(mTextBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.me_click,"我的")
                        .setInactiveIcon(ContextCompat.getDrawable(MainActivity.this,R.drawable.me_normal)))
                .setFirstSelectedPosition(0)//设置默认选择的按钮
                .initialise();//所有的设置需在调用该方法前完成
    }

    private void initView() {

        mOneFragment=new OneFragment();
        mTwoFragment=new TwoFragment();
        mThreeFragment=new ThreeFragment();
        mFourFragment=new FourFragment();

        mBottomNavigationBar= (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        mTextBadgeItem = new TextBadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.colorAccent)
                .setAnimationDuration(200)
                .setText("3")
                .setHideOnSelect(false);

        mShapeBadgeItem = new ShapeBadgeItem()
                .setShapeColorResource(R.color.colorPrimary)
                .setGravity(Gravity.TOP | Gravity.END)
                .setHideOnSelect(false);

        mFragments=new Fragment[]{mOneFragment,mTwoFragment,mThreeFragment,mFourFragment};
    }
}
