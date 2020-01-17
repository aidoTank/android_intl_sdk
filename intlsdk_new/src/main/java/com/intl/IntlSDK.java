package com.intl;

import android.app.Activity;

import com.intl.utils.LocaleManager;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class IntlSDK {
    private static volatile IntlSDK instance;

    public static IntlSDK getInstance() {
        if (instance == null) {
            synchronized(IntlSDK.class) {
                if (instance == null) {
                    instance = new IntlSDK();
                }
            }
        }
        return instance;
    }

    public  void init(Activity activity)
    {
        LocaleManager.getInstance().setLocale(activity);

    }

}
