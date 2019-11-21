package com.intl.utils;

import android.util.Log;

import com.intl.ad.IntlGameAdstrack;

/**
 * @Author: yujingliang
 * @Date: 2019/11/21
 */
public class IntlGameLogUtil {
    private static String TAG = "IntlGameLogUtil";
    private static final int noPrint = 0;
    private static final int Print = 1;

    public IntlGameLogUtil() {
    }

    public static void i(String tag, String msg) {
        if (IntlGameAdstrack.LemonLogMode != 0) {
            Log.i(tag, msg);
        }
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
