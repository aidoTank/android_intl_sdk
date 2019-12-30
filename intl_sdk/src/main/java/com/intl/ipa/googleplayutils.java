package com.intl.ipa;

import android.app.Activity;
import android.util.Log;

import com.intl.IntlGame;
import com.intl.utils.IntlGameToast;
import com.intl.utils.IntlGameUtil;

/**
 * @Author: yujingliang
 * @Date: 2019/12/24
 */
public class googleplayutils {
    private static String TAG = "googleplayutils";
    public static String roleinfo = null;
    public static void google(final Activity context, final String itemid, final IGoogleInAppPurchaseV3Listener googleplayv3Listener) {
        if(IntlGame.isLogin(context)&&roleinfo!=null)
        {
            Log.i(TAG, "googleplayutils: go to=> GooglePlayPayUtils.googlepay");
            GooglePlayPayUtils.googlepay(context, itemid, roleinfo, new GooglePlayPayUtils.GooglePayListener() {
                @Override
                public void onComplete(int code, String msg) {
                    IntlGameUtil.logd(TAG,"code : "+code+" msg : "+msg );
                    if(code == 0)
                    {
                        googleplayv3Listener.onComplete(0, "支付成功");
                    }else{
                        googleplayv3Listener.onComplete(1, "支付失败");
                    }
                }
            });
        }else {
            IntlGameToast.showMessage(context,"No login");
        }
    }
    public static void SetGameRoleInfo(String roleinfo)
    {
        googleplayutils.roleinfo = roleinfo;
    }
    public static void onResume(final Activity context)
    {
        GooglePlayPayUtils.onResume(context);
    }

    public static void onDestroy()
    {
        GooglePlayPayUtils.onDestroy();
    }

    public interface IGoogleInAppPurchaseV3Listener {
        void onComplete(int code, String msg);
    }
}
