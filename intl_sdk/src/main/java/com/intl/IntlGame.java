package com.intl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.intl.af.AFManager;
import com.intl.loginchannel.FaceBookSDK;
import com.intl.loginchannel.GoogleSDK;
import com.intl.entity.IntlDefine;
import com.intl.usercenter.IntlGameCenter;
import com.intl.utils.IntlGameExceptionUtil;
import com.intl.utils.IntlGameUtil;

import java.util.Map;


/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGame extends Activity {
    public static int LogMode = 1;
    public static ILoginCenterListener iLoginListener;
    public static IPersonCenterListener iPersonCenterListener;
    public static ILogoutListener iLogoutListener;
    public static String GPclientid;
    public static String GPsecretid;
    public static String GooggleID = "";
    public static String GoogleClientId;
    public static String FacebookClientId;
    public static Application application;
    public static String urlHost = "https://gather-auth.ycgame.com";
    public static void init(final Activity activity, String devKey, final String google_clientid, final String facebook_clientid,  String gp_clientid, String gp_secret,final IInitListener iInitListener)
    {
        GoogleClientId = google_clientid;
        FacebookClientId = facebook_clientid;
        GPclientid = gp_clientid;
        GPsecretid = gp_secret;
        try{
            IntlGameCenter.init(activity);
        }catch (Exception e){
            IntlGameExceptionUtil.handle(e);
        }
        if(devKey != null)
        {
            AFManager.getInstance().AFinit(activity,devKey,IntlGame.application);
        }

        IntlGameUtil.getLocalGoogleAdID(activity, new IntlGameUtil.IGgetLocalGoogleAdIdListener() {
            @Override
            public void onComplete(int code, String ID) {
                IntlGame.GooggleID = ID;
                iInitListener.onComplete(IntlDefine.SUCCESS,ID);
                Log.d("getLocalGoogleAdID", "GooggleID: "+ID);

            }
        });
    }
    public static void LoginCenter(Activity activity, ILoginCenterListener _iLoginListener)
    {
        iLoginListener = _iLoginListener;
        IntlGameCenter.getInstance().LoginCenter(activity);
    }

    public static void PersonCenter(Activity activity, IPersonCenterListener _iPersonCenterListener)
    {
        iPersonCenterListener = _iPersonCenterListener;
        IntlGameCenter.getInstance().PersonCenter(activity);
    }
    public static boolean isLogin(Activity activity)
    {
        return IntlGameCenter.getInstance().isLogin(activity);
    }

    public static void LogOut(Activity activity,ILogoutListener _iLogoutListener)
    {
        iLogoutListener = _iLogoutListener;
        IntlGameCenter.getInstance().LogOut(activity);
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
        IntlGameCenter.getInstance().onResume();
    }
    public static void IntlonPause()
    {
        IntlGameCenter.getInstance().onPause();
    }
    public static void IntlonDestroy()
    {
        IntlGameCenter.getInstance().onDestroy();
    }

    public interface IInitListener {
        void onComplete(int code, String msg);
    }
    public interface ILoginCenterListener {
        void onComplete(int code, String openid, String token, String errorMsg);
    }
    public interface IPersonCenterListener{
        void onComplete(String type, int code, String errorMsg);
    }
    public interface ILogoutListener{
        void onComplete(int code,String errorMsg);
    }
}
