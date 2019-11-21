package com.intl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.intl.channel.FaceBookSDK;
import com.intl.channel.GoogleSDK;
import com.intl.sqlite.IntlGameDBHelper;
import com.intl.utils.IntlGameUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGame extends Activity {
    public static Handler IntlGameHandler;
    public static IntlGameDBHelper db;
    public static ILoginListener iLoginListener;
    public static ILogoutListener iLogoutListener;
    public static Handler IGonGameHandler;
    public static String GooggleID = "";
    public static String GoogleClientId;
    public static Application application;
    public static String UUID = "";
    //public static String Url;
    @SuppressLint("HardwareIds")
    public static void init(final Activity activity, String devKey, String googleClientId, String url, IInitListener iInitListener)
    {
        GoogleClientId = googleClientId;
        UUID = Settings.Secure.getString(activity.getContentResolver(), "android_id");
        try{
            Uri uri = Uri.parse(url);
            IntlGameLoginCenter.init(activity, uri,414,319);
        }catch (Exception e){

        }
        if(devKey != null)
        {
            AppsFlyerConversionListener conversionDataListener = new AppsFlyerConversionListener(){

                @Override
                public void onInstallConversionDataLoaded(Map<String, String> map) {

                }

                @Override
                public void onInstallConversionFailure(String s) {

                }

                @Override
                public void onAppOpenAttribution(Map<String, String> map) {

                }

                @Override
                public void onAttributionFailure(String s) {

                }
            };
            AppsFlyerLib.getInstance().init(devKey, conversionDataListener, activity);
            AppsFlyerLib.getInstance().setAndroidIdData(IntlGameUtil.getLocalAndroidId(activity));
            AppsFlyerLib.getInstance().setDebugLog(true);
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("Start", "1");
            AfEvent(activity, "Start_AppLaunch", mapa);

        }

        IntlGameUtil.getLocalGoogleAdID(activity, new IntlGameUtil.IGgetLocalGoogleAdIdListener() {
            @Override
            public void onComplete(int code, String ID) {
                IntlGame.GooggleID = ID;

            }
        });
    }
    public static void Login(Activity activity, ILoginListener _iLoginListener)
    {
        iLoginListener = _iLoginListener;
//        if(Url !=null) {
            IntlGameLoginCenter.getInstance().showLoginWebView(activity);
//        }else{
//            GFLoginActivity.LoginCenter(activity);
//        }
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

    public static void onActivityResults(int requestCode, int resultCode, Intent intent)
    {
        GoogleSDK.onActivityResult(requestCode,resultCode,intent);
        FaceBookSDK.onActivityResult(requestCode,resultCode,intent);
    }

    public interface ILoginListener{
        void onComplete(int code,String token);
    }
    public interface ILogoutListener{
        void onComplete();
    }
}
