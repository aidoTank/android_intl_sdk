package com.intl;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGameHandlerManage {
    static void checkHandler() {
        try {
            if (IntlGame.IGonGameHandler == null) {
                IntlGame.IGonGameHandler = new Handler();
            }
        } catch (Exception var1) {
            IntlGame.IGonGameHandler = null;
        }

    }
//    public static void IntlGame_HandlerManage(final Activity activity)
//    {
//        if(IntlGame.IGonGameHandler == null)
//        {
//            IntlGame.IGonGameHandler = new Handler(Looper.getMainLooper()){
//                public void handleMessage(Message msg)
//                {
//                    super.handleMessage(msg);
//                    HashMap mapx;
//                    switch (msg.what)
//                    {
//                        case IntlGameHandlerManageNum.init:
//                            UserCenter.init(activity, Uri.parse("http://10.0.2.2:8080/app/logincenter.html"),414,319);
//                            break;
//                        case IntlGameHandlerManageNum.appsflyMsg:
//                            AppsFlyerConversionListener conversionDataListener = new AppsFlyerConversionListener(){
//
//                                @Override
//                                public void onInstallConversionDataLoaded(Map<String, String> map) {
//
//                                }
//
//                                @Override
//                                public void onInstallConversionFailure(String s) {
//
//                                }
//
//                                @Override
//                                public void onAppOpenAttribution(Map<String, String> map) {
//
//                                }
//
//                                @Override
//                                public void onAttributionFailure(String s) {
//
//                                }
//                            };
//                            AppsFlyerLib.getInstance().init(IntlGame.devKey, conversionDataListener, activity);
//                            AppsFlyerLib.getInstance().setAndroidIdData(IntlGameUtil.getLocalAndroidId(activity));
//                            AppsFlyerLib.getInstance().setDebugLog(true);
//                            Map<String, Object> mapa = new HashMap<>();
//                            mapa.put("Start", "1");
//                            IntlGame.AfEvent(activity, "Start_AppLaunch", mapa);
//                            break;
//                    }
//                }
//            };
//        }
//    }
}
