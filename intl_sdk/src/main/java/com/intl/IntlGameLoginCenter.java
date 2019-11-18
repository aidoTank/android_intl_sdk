package com.intl;

import android.app.Activity;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGameLoginCenter extends Activity {

    public static void ShowLoginCenter(Activity activity)
    {
        UserCenter.getInstance().showLoginWebView(activity);
    }
}
