package com.hsy.student_app.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.hsy.student_app.R;
import com.hsy.student_app.fragment.FragmentAdress;
import com.hsy.student_app.fragment.FragmentFound;
import com.hsy.student_app.fragment.FragmentHome;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hsy.student_app.helper.Contact.USER;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = "ActivityMain";
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationView bottomNavigationBar;
    private SharedPreferences sp;
    private FragmentHome fragmentHome;
    private FragmentAdress fragmentAdress;
    private FragmentFound fragmentFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        isLogin();
        initBottomNativiBar();


    }

    /**
     * @Data 2019/4/20 11:35
     * @deprecated 底部导航栏
     */
    private void initBottomNativiBar() {
        bottomNavigationBar.setItemIconTintList(null);
        Resources resource=getBaseContext().getResources();
        ColorStateList csl=resource.getColorStateList(R.color.bottom_text_color);
        bottomNavigationBar.setItemTextAppearanceActive(R.style.bottom_selected_text);
        bottomNavigationBar.setItemTextAppearanceInactive(R.style.bottom_normal_text);
        bottomNavigationBar.setItemTextColor(csl);
        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager fm = ActivityMain.this.getSupportFragmentManager();
                //开启事务
                FragmentTransaction transaction = fm.beginTransaction();
                resetToDefaultIcon();//图标全部默认
                switch (menuItem.getItemId()) {
                    case R.id.navigation_phone:
                        if (fragmentAdress == null) {
                            fragmentAdress = FragmentAdress.newInstance();
                        }
                        menuItem.setIcon(R.drawable.un_adress);
                        menuItem.setChecked(true);
                        if (transaction==null)transaction =fm.beginTransaction();
                        transaction.replace(R.id.fragment, fragmentAdress).commit();
                        break;
                    case R.id.navigation_home:
                        if (fragmentHome == null) {
                            fragmentHome = FragmentHome.newInstance();
                        }
                        menuItem.setIcon(R.drawable.un_home);
                        menuItem.setChecked(true);
                        if (transaction==null)transaction =fm.beginTransaction();
                        transaction.replace(R.id.fragment, fragmentHome).commit();
                        break;
                    case R.id.navigation_found:
                        if (fragmentFound == null) {
                            fragmentFound = fragmentFound.newInstance();
                        }
                        menuItem.setIcon(R.drawable.found);
                        menuItem.setChecked(true);
                        if (transaction==null)transaction =fm.beginTransaction();
                        transaction.replace(R.id.fragment, fragmentFound).commit();
                        break;
                }

                return false;
            }
        });

        //默认显示出来FragmentAdress
        FragmentManager fm = ActivityMain.this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (fragmentHome == null) {
            fragmentHome = FragmentHome.newInstance();
        }
        transaction.replace(R.id.fragment, fragmentHome).commit();
        bottomNavigationBar.getMenu().getItem(1).setChecked(true);
        bottomNavigationBar.getMenu().getItem(1).setIcon(R.drawable.un_home);
    }

    private void resetToDefaultIcon() {
        MenuItem adress =  bottomNavigationBar.getMenu().findItem(R.id.navigation_phone);
        adress.setIcon(R.drawable.adress);
        MenuItem home =  bottomNavigationBar.getMenu().findItem(R.id.navigation_home);
        home.setIcon(R.drawable.home);
        MenuItem found =  bottomNavigationBar.getMenu().findItem(R.id.navigation_found);
        found.setIcon(R.drawable.unfound);
    }




    /**
     * @Data 2019/4/20 11:34
     * @deprecated 判断是否登录
     */
    private void isLogin() {
        if (sp == null) sp = getSharedPreferences(USER, Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        if (isLogin) {

        } else {
            startActivity(new Intent(this, ActivityLogin.class));
        }
    }
}
