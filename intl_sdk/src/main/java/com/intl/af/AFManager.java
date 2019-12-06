package com.intl.af;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.intl.IntlGame;
import com.intl.utils.IntlGameUtil;

import java.util.Map;

/**
 * @Author: yujingliang
 * @Date: 2019/12/6
 */
public class AFManager {
    private static AFManager instance;
    public static AFManager getInstance()
    {
        if(instance == null)
            instance = new AFManager();
        return instance;
    }
    public void AFinit(Activity activity, String devKey, Application context)
    {
        AppsFlyerConversionListener conversionDataListener = new AppsFlyerConversionListener(){

            @Override
            public void onInstallConversionDataLoaded(Map<String, String> map) {
                Log.d("AppsFlyer", "onInstallConversionDataLoaded: "+map);
            }

            @Override
            public void onInstallConversionFailure(String s) {
                Log.d("AppsFlyer", "onInstallConversionFailure: "+s);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {
                Log.d("AppsFlyer", "onAppOpenAttribution: "+map);
            }

            @Override
            public void onAttributionFailure(String s) {

            }
        };
        AppsFlyerLib.getInstance().init(devKey, conversionDataListener, activity);
        AppsFlyerLib.getInstance().startTracking(context);
        AppsFlyerLib.getInstance().setAndroidIdData(IntlGameUtil.getLocalAndroidId(activity));
        AppsFlyerLib.getInstance().setDebugLog(true);
    }
}
