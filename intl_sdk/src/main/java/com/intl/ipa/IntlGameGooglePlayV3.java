package com.intl.ipa;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

import com.intl.IntlGame;

/**
 * @Author: yujingliang
 * @Date: 2019/12/24
 */
public class IntlGameGooglePlayV3 {

    private static final String TAG = "LemonGameGooglePlayV3Activity";

    public static void googles(Context context, String productId) {
        googleplayutils.google((Activity)context, productId, IntlGame.googleplayv3Listener);
    }

    public static void SetGameRoleInfo(String roleinfo)
    {
        googleplayutils.SetGameRoleInfo(roleinfo);
    }
    public static void onResume(final Activity context)
    {
        googleplayutils.onResume(context);
    }
    public static void onDestroy()
    {
        googleplayutils.onDestroy();
    }
}
