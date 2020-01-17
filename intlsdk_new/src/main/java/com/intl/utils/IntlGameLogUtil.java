package com.intl.utils;

import android.util.Log;

/**
 * @Author: yujingliang
 * @Date: 2019/11/21
 */
public class IntlGameLogUtil {
    private static String TAG = "IntlGameLogUtil";

    public IntlGameLogUtil() {
    }

    public static void i(String tag, String msg) {
//        if (IntlGame.LogMode != 0) {
            Log.i(tag, msg);
//        }
    }

    public static void i(String tag, boolean msg) {
        i(tag, String.valueOf(msg));
    }

    public static void i(String tag, int msg) {
        i(tag, String.valueOf(msg));
    }

    public static void i(String tag, long msg) {
        i(tag, String.valueOf(msg));
    }


}
