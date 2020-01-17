package com.intl.base;

import android.content.Context;

/**
 * @Author: yujingliang
 * @Date: 2020/1/13
 */
public class IntlContext {
    private static Context applicationContext;

    private IntlContext() {
    }

    public static Context getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(Context applicationContext) {
        IntlContext.applicationContext = applicationContext;
    }
}
