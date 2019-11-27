package com.intl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.intl.channel.FaceBookSDK;
import com.intl.channel.GoogleSDK;
import com.intl.usercenter.IntlGameLoginCenter;
import com.intl.utils.IntlGameExceptionUtil;
import com.intl.utils.IntlGameUtil;
import com.intl.webview.WebSession;

import java.util.Map;


/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGame extends Activity {
    public static int LogMode = 1;
    public static ILoginListener iLoginListener;
    public static ILogoutListener iLogoutListener;
    public static String GooggleID = "";
    public static String GoogleClientId;
    public static String FacebookClientId;
    public static Application application;
    public static String UUID = "";
    @SuppressLint("HardwareIds")
    public static void init(final Activity activity, String devKey, final String googleClientId,final String fbClientId, String url, IInitListener iInitListener)
    {
        GoogleClientId = googleClientId;
        FacebookClientId = fbClientId;
        UUID = Settings.Secure.getString(activity.getContentResolver(), "android_id");
        try{
            Uri uri = Uri.parse(url);
            IntlGameLoginCenter.init(activity, uri,414,319);
        }catch (Exception e){
            IntlGameExceptionUtil.handle(e);
        }
        if(devKey != null)
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
            AppsFlyerLib.getInstance().setAndroidIdData(IntlGameUtil.getLocalAndroidId(activity));
            AppsFlyerLib.getInstance().setDebugLog(true);

        }


        IntlGameUtil.getLocalGoogleAdID(activity, new IntlGameUtil.IGgetLocalGoogleAdIdListener() {
            @Override
            public void onComplete(int code, String ID) {
                IntlGame.GooggleID = ID;
                Log.d("getLocalGoogleAdID", "GooggleID: "+ID);

            }
        });
    }
    public static void Login(Activity activity, ILoginListener _iLoginListener)
    {
        iLoginListener = _iLoginListener;
        IntlGameLoginCenter.getInstance().autoLogin(activity);
    }
    public static void LogOut()
    {
        FaceBookSDK.logout();
        GoogleSDK.logout();
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

    public static void IntlonActivityResults(int requestCode, int resultCode, Intent intent)
    {
        GoogleSDK.onActivityResult(requestCode,resultCode,intent);
        FaceBookSDK.onActivityResult(requestCode,resultCode,intent);
    }

    public static void IntlonResume()
    {
        WebSession.setDialogVisiable(true);
    }
    public static void IntlonPause()
    {
        WebSession.setDialogVisiable(false);
    }
    public interface ILoginListener{
        void onComplete(int code,String token);
    }
    public interface ILogoutListener{
        void onComplete();
    }
}
