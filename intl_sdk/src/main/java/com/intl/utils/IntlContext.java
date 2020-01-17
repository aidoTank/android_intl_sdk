package com.intl.utils;

import android.content.Context;

/**
 * @Author: yujingliang
 * @Date: 2020/1/15
 */
public class IntlContext {
    private static Context applicationContext;
    public static Context getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(Context applicationContext) {
        IntlContext.applicationContext = applicationContext;
    }
}
