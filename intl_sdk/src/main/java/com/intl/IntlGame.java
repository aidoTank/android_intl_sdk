package com.intl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.intl.af.AFManager;
import com.intl.entity.Account;
import com.intl.ipa.IntlGameGooglePlayV3;
import com.intl.ipa.googleplayutils;
import com.intl.loginchannel.FaceBookSDK;
import com.intl.loginchannel.GoogleSDK;
import com.intl.entity.IntlDefine;
import com.intl.usercenter.IntlGameCenter;
import com.intl.utils.IntlContext;
import com.intl.utils.IntlGameExceptionUtil;
import com.intl.utils.IntlGameLanguageCache;
import com.intl.utils.IntlGameUtil;
import com.intl.utils.MsgManager;

import org.json.JSONObject;

import java.util.Map;


/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGame extends Activity {
    private static final String TAG = "IntlGame";
    public static int LogMode = 1;
    public static ILoginCenterListener iLoginListener;
    public static IPersonCenterListener iPersonCenterListener;
    public static ILogoutListener iLogoutListener;
    public static ISwitchAccountListener iSwitchAccountListener;
    public static googleplayutils.IGoogleInAppPurchaseV3Listener googleplayv3Listener;
    public static String GPclientid;
    public static String GPsecretid;
    public static String GooggleID = "";
    public static String GoogleClientId;
    public static String FacebookClientId;
    public static Application application;
    public static String Game;
    public static int retryTime = 0;
    public static String urlHost = "https://gather-auth.ycgame.com";
    public static void init(final Activity activity,String game,String devKey, final String google_clientid, final String facebook_clientid,  String gp_clientid, String gp_secret,final IInitListener iInitListener)
    {
        Game = game;
        GoogleClientId = google_clientid;
        FacebookClientId = facebook_clientid;
        GPclientid = gp_clientid;
        GPsecretid = gp_secret;
        try{
            IntlGameCenter.init(activity);
            MsgManager.readParamsFromAssets(IntlContext.getApplicationContext());
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
    public static void setLanguage(Activity activity,String language)
    {
        if (!language.equals("cn") && !language.equals("tw") && !language.equals("en")&&!language.equals("th")) {
            language = "en";
        }
        Log.i(TAG, "setLanguage: lan = "+language);
        IntlGameLanguageCache.saveLan(activity,language);
    }

    public static void LoginCenter(Activity activity, ILoginCenterListener _iLoginListener)
    {
        iLoginListener = _iLoginListener;
        IntlGameCenter.getInstance().LoginCenter(activity);
    }

    public static void googlePlayV3(Context context, String productId, googleplayutils.IGoogleInAppPurchaseV3Listener googleplayv3Listener) {
        IntlGame.googleplayv3Listener = googleplayv3Listener;
        IntlGameCenter.getInstance().googlePlayV3(context,productId);
    }
    public static void changeAccount(Activity activity, String arg, ISwitchAccountListener _iSwitchAccountListener){
        iSwitchAccountListener = _iSwitchAccountListener;
        IntlGameCenter.getInstance().changerAccount(activity,arg);
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

    public static boolean isGuest(Activity activity)
    {
        return IntlGameCenter.getInstance().isGuest(activity);
    }
    public static void LoginCenterLogout(Activity activity,ILogoutListener _iLogoutListener)
    {
        iLogoutListener = _iLogoutListener;
        IntlGameCenter.getInstance().LogOut(activity);
    }

    public static void Afinit(Application context)
    {
        application = context;
    }
    public static void AfEvent(Context context, String eventname, Map<String, Object> map) {
        AFManager.getInstance().AfEvent(context,eventname,map );
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
    public static void SetGameRoleInfo(String role_info,boolean bcreate)
    {
        IntlGameCenter.getInstance().SetGameRoleInfo(role_info,bcreate);
    }
    public interface IInitListener {
        void onComplete(int code, String msg);
    }
    public interface ILoginCenterListener {
        void onComplete(int code, String openid, String token, String errorMsg);
    }
    public interface ISwitchAccountListener{
        void onComplete(int code, String openid, String token, String errorMsg);
    }
    public interface IPersonCenterListener{
        void onComplete(String type, int code, String arg, String errorMsg);
    }
    public interface ILogoutListener{
        void onComplete(int code, String errorMsg);
    }
}
