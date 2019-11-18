package com.intl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.appsflyer.AppsFlyerLib;

import java.util.Map;


/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGame extends Activity {
    public static Handler IGonGameHandler;
    public static String GooggleID = "";
    public static String devKey = "";
    public static String GoogleAppId = "883426973649-jqul09g64m0too4adsbscat6i2b6lptf.apps.googleusercontent.com";
    public static Application application;
    public static void init(final Context context,IInitListener iInitListener)
    {
        IntlGameHandlerManage.IntlGame_HandlerManage(context);
        if(devKey != null)
        {
            Message afInitMsg = new Message();
            afInitMsg.what = IntlGameHandlerManageNum.appsflyMsg;
            IGonGameHandler.sendMessage(afInitMsg);
        }
        Message initMsg = new Message();
        initMsg.what = IntlGameHandlerManageNum.init;
        IGonGameHandler.sendMessage(initMsg);
        IntlGameUtil.getLocalGoogleAdID(context, new IntlGameUtil.IGgetLocalGoogleAdIdListener() {
            @Override
            public void onComplete(int code, String ID) {
                IntlGame.GooggleID = ID;
            }
        });
    }
    public static void OpenLoginCenter(Activity activity)
    {
        UserCenter.getInstance().showLoginWebView(activity);
    }
    public interface IInitListener {
        void onComplete(int var1, String var2);
    }
    public static void Afinit(Application context)
    {
        application = context;
    }
    public static void AfEvent(Context context, String eventname, Map<String, Object> map) {
        AppsFlyerLib.getInstance().trackEvent(context, eventname, map);
    }
}
