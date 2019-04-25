package com.hsy.student_app.utils;

import android.widget.Toast;

import com.hsy.student_app.MyApplication;

/**
 * Created by wangwentao on 2017/1/25.
 * Toast统一管理类
 */

public class ToastUtils {

    private static boolean isShow = true;//默认显示
    private static Toast mToast = null;//全局唯一的Toast


    /*private控制不应该被实例化*/
    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 全局控制是否显示Toast
     *
     * @param isShowToast
     */
    public static void controlShow(boolean isShowToast) {
        isShow = isShowToast;
    }

    /**
     * 取消Toast显示
     */
    public void cancelToast() {
        if (isShow && mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.sContext, message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showShort(int resId) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.sContext, resId, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.sContext, message, Toast.LENGTH_LONG);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showLong(int resId) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(MyApplication.sContext, resId, Toast.LENGTH_LONG);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }
}