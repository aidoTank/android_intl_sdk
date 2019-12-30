package com.intl.af;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.intl.IntlGame;
import com.intl.utils.IntlGameUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yujingliang
 * @Date: 2019/12/6
 */
public class AFManager {
    private static final String TAG = "AFManager";
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

    public void AfEvent(Context context, String eventname, Map<String, Object> map) {
        AppsFlyerLib.getInstance().trackEvent(context, eventname, map);
    }

    public void AdPurchase(Context context, String itemId, String pay_currency, String pay_amount, String purchase_type)
    {
        IntlGameUtil.logd(TAG, "商品ID：" + itemId);
        IntlGameUtil.logd(TAG, "货币类型：" + pay_currency);
        IntlGameUtil.logd(TAG, "货币钱数：" + pay_amount);
        try {
            AppsFlyerLib.getInstance().setCurrencyCode(pay_currency);
            IntlGameUtil.logd(TAG, "Appsflyer Add to cart event");
            Map<String, Object> eventValue = new HashMap<>();
            eventValue.put("af_revenue", pay_amount);
            eventValue.put("af_content_type", purchase_type);
            eventValue.put("af_content_id", itemId);
            eventValue.put("af_currency", pay_currency);
            AppsFlyerLib.getInstance().trackEvent(context, "af_purchase", eventValue);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }
}
