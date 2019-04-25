package com.hsy.student_app;

import android.app.Application;
import android.content.Context;

/**
 * Description:
 * Data：2019/3/16-14:25
 * Author: Summer
 */
public class MyApplication extends Application {

    public static Context sContext;//全局的Context对象

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;

    }
}
