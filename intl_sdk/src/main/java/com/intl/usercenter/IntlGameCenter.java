package com.intl.usercenter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.intl.IntlGame;
import com.intl.api.AuthorizeCheckAPI;
import com.intl.api.RefreshAPI;
import com.intl.entity.Account;
import com.intl.ipa.IntlGameGooglePlayV3;
import com.intl.loginchannel.FaceBookSDK;
import com.intl.loginchannel.GoogleSDK;
import com.intl.loginchannel.Guest;
import com.intl.entity.IntlDefine;
import com.intl.utils.IntlGameExceptionUtil;
import com.intl.utils.IntlGameLoading;
import com.intl.utils.IntlGameUtil;
import com.intl.webview.WebCommandSender;
import com.intl.webview.WebSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Locale;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGameCenter {
    private static final String LOGIN_CENTER_WEB_COMMAND_DOMAIN = "yc.mobilesdk.logincenter";
    private static final String PERSON_CENTER_WEB_COMMAND_DOMAIN = "yc.mobilesdk.personcenter";
    private static final String TAG = "IntlGamePay";
    private static IntlGameCenter _instance;
    private WebSession _webSession;
    public WeakReference<Activity> activity;
    private IntlGameHandler handler;
    public static boolean isSwitch = false;
    static class IntlGameHandler extends Handler{
        WeakReference<Activity> activity;
        IntlGameHandler(WeakReference<Activity> activity){
            this.activity = activity;
        }
        @Override
        public void handleMessage(Message msg){
            HashMap Msgmap = (HashMap)msg.obj;
            WebSession.currentWebSession().forceCloseSession();
            boolean isLoginScene = false;
            if(String.valueOf( Msgmap.get("command")).equals("ShowLoginMainPage"))
            {
                if(IntlGame.iPersonCenterListener !=null){
                    isSwitch = true;
                    IntlGame.iPersonCenterListener.onComplete("switchroles",IntlDefine.SUCCESS, String.valueOf(Msgmap.get("args")),null);
                }
                return;
            }
            if(String.valueOf(Msgmap.get("commandDomain")) .equals(LOGIN_CENTER_WEB_COMMAND_DOMAIN)) {
                isLoginScene = true;
                if(String.valueOf( Msgmap.get("command")).equals("close"))
                {
                    if(!isSwitch)
                    {
                        activity.get().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                IntlGameCenter.getInstance().LoginCenter(activity.get());
                            }
                        });
                    }
                }
                if(String.valueOf( Msgmap.get("command")).equals("Google"))
                {
                    if(isSwitch)
                    {
                        GoogleSDK.SwitchLogin(this.activity);
                    }else {
                        GoogleSDK.login(this.activity,!isLoginScene);
                    }

                }
                if(String.valueOf( Msgmap.get("command")).equals("Facebook"))
                {
                    if(isSwitch)
                    {
                        FaceBookSDK.SwitchLogin(this.activity);

                    }else {
                        FaceBookSDK.login(activity,!isLoginScene);
                    }
                }
                if(String.valueOf( Msgmap.get("command")).equals("Guest"))
                {
                    Guest.login(activity);
                }
            }
            if(String.valueOf(Msgmap.get("commandDomain")) .equals(PERSON_CENTER_WEB_COMMAND_DOMAIN)) {
                isLoginScene = false;
                if(String.valueOf( Msgmap.get("command")).equals("Google")){
                    GoogleSDK.login(this.activity,!isLoginScene);
                    //GoogleSDK.SwitchLogin(this.activity);
                }
                if(String.valueOf( Msgmap.get("command")).equals("Facebook")){
                    FaceBookSDK.login(activity,!isLoginScene);
                    //FaceBookSDK.SwitchLogin(activity);
                }

            }
        }
    }

    public static void init(Activity activity){

        if (_instance == null)
        {
            _instance = new IntlGameCenter(activity);

        }
    }
    private static String getLanguage(Activity activity)
    {
        Locale locale = activity.getResources().getConfiguration().locale;
        return locale.getLanguage();
    }
    public static IntlGameCenter getInstance() {

        return _instance;
    }
    private IntlGameCenter(Activity activity)
    {
        this.activity = new WeakReference<>(activity);
        handler = new IntlGameHandler(this.activity);
        _webSession = new WebSession();
        registCommand();
    }
    public void showWebView(Activity activity, String _uri) {
        Log.d(TAG, "showWebView: uri=>"+_uri);
        _webSession.showDialog(activity,
                520,
                315,
                Uri.parse(_uri), false);

    }
    public boolean isLogin(Activity activity)
    {
        return AccountCache.loadAccount(activity) != null;
    }

    public boolean isGuest(Activity activity)
    {
        if(!isLogin(activity))
        {
            return false;
        }
        Account account = AccountCache.loadAccount(activity);
        return account.getChannel().equals("ycgame");
    }
    public void LogOut(Activity activity)
    {
        AccountCache.cleanAccounts(activity);
        GoogleSDK.logout(activity);
        Guest.logout();
        FaceBookSDK.logout();
    }
    private void channelLogout(Activity activity)
    {
        GoogleSDK.logout(activity);
        FaceBookSDK.logout();
        Guest.logout();
    }
    public void onPause(){
        WebSession.setDialogVisiable(false);
    }
    public void onResume(){
        WebSession.setDialogVisiable(true);
        IntlGameGooglePlayV3.onResume(activity.get());
    }
    public void onDestroy()
    {
        IntlGameLoading.getInstance().destroy();
        IntlGameGooglePlayV3.onDestroy();
    }

    public void SetGameRoleInfo(String role_info,boolean bcreate)
    {
        IntlGameGooglePlayV3.SetGameRoleInfo(role_info);
        if(bcreate)
        {
            //创建角色
        }
    }

    public  void googlePlayV3(Context context, String productId) {
        IntlGameUtil.logd(TAG, "开始调用googlePlayV3()方法");
        IntlGameUtil.logd(TAG, "context对象：" + context);
        IntlGameUtil.logd(TAG, ">>>>>>>>>>>>");
        IntlGameUtil.logd(TAG, "开始调用google界面充值.");
        IntlGameGooglePlayV3.googles(context, productId);
    }

    public void PersonCenter(Activity activity)
    {
        Account act = loadAccounts(activity);
        if(act !=null)
        _webSession.showDialog(activity,520,315,Uri.parse(IntlGame.urlHost +"/usercenter.html?openid="+act.getOpenid()+"&access_token="+act.getAccessToken()+"&channeltype=agl&language="+IntlGame.language),false);
    }
    public void LoginCenter(final Activity activity)
    {
        if (WebSession.getIsShwoingWebPage()) {
            return;
        }
        final Account account = loadAccounts(activity.getApplicationContext());
        if (account == null) {
            showWebView(activity,IntlGame.urlHost +"/index.html?channeltype=agl&language="+IntlGame.language);
            return;
        }
        if(account.getAccessTokenExpire()>IntlGameUtil.getUTCTimeStr())
//        if(false)
        {
            IntlGameUtil.logd("IntlGame","AccessTokenExpire =>"+account.getAccessTokenExpire()+" currentTime=>"+System.currentTimeMillis()/1000);
            AuthorizeCheckAPI checkAPI = new AuthorizeCheckAPI(account);
            checkAPI.setListener(new AuthorizeCheckAPI.ICheckAccessTokenCallback() {
                @Override
                public void AfterCheck(final JSONObject jsonObject, String errorMsg) {
                    if(jsonObject != null)
                    {
                        IntlGameUtil.logd("IntlGame","AccessToken is Effective");
                        IntlGame.iLoginListener.onComplete(IntlDefine.SUCCESS,jsonObject.optString("openid"),jsonObject.optString("access_token"),null);
                    }else{
                        IntlGame.iLoginListener.onComplete(IntlDefine.FAILED,null,null,errorMsg);
                    }
                }
            });
            checkAPI.Excute();
        }
        else{
            IntlGameUtil.logd("IntlGame","AccessToken is Expired");
            RefreshAPI refreshAPI = new RefreshAPI(account);
            refreshAPI.setListener(new RefreshAPI.IRefreshCallback() {
                @Override
                public void AfterRefresh(JSONObject jsonObject,String errorMsg) {
                    if(jsonObject != null){
                        account.setOpenid(jsonObject.optString("openid"));
                        account.setAccessToken(jsonObject.optString("access_token"));
                        account.setAccessTokenExprie(jsonObject.optInt("access_token_expire"));
                        account.setRefreshToken(jsonObject.optString("refresh_token"));
                        account.setRefreshTokenExpire(jsonObject.optInt("refresh_token_expire"));
                        setAccount(activity,account);
                        if(IntlGame.iLoginListener != null)
                        {
                            IntlGame.iLoginListener.onComplete(IntlDefine.SUCCESS,jsonObject.optString("openid"),jsonObject.optString("access_token"),null);
                        }
                        try {
                            IntlGameUtil.logd("IntlGameLoginCenter","Refresh Account=>"+account.getJSONObj().toString());
                        } catch (JSONException e) {
                            IntlGameExceptionUtil.handle(e);
                        }
                    }else {
                        if(IntlGame.iLoginListener != null)
                        {
                            IntlGame.iLoginListener.onComplete(IntlDefine.FAILED,null,null,errorMsg);
                        }
                    }

                }
            });
            refreshAPI.Excute();
        }

    }

    public void changerAccount(final Activity activity,String arg)
    {
        String ext = null;
        String channel = null;
        String lan = null;
        try {
            if(arg == null)
            {
                ext = "";
            }else {
                JSONObject jsonObject = new JSONObject(arg);
                ext =  jsonObject.optString("extensionInfo");
                channel =  "agl";
                lan =  IntlGame.language;

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        if(ext == null)
            ext="";
        if(channel == null)
            channel = "";
        if(lan == null)
            lan = "";

        Log.d(TAG, "changerAccount: channel=>"+channel+" ext=>"+ext+" lan=>"+lan);
        if (WebSession.getIsShwoingWebPage()) {
            return;
        }
        if(IntlGame.isLogin(activity))
        {
            channelLogout(activity);
        }
        showWebView(activity,IntlGame.urlHost +"/index.html?channeltype="+channel+"&language="+IntlGame.language+"&trans_data="+ext);

    }

    public Account loadAccounts(Context context) {
        return AccountCache.loadAccount(context);
    }
    private void setAccount(Context context, Account account) {
        AccountCache.saveAccounts(context, account);
    }
    private void registCommand()
    {
        _webSession.regisetCommandListener(new WebSession.IWebCommandListener() {
            @Override
            public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                HashMap<String,Object> map = new HashMap<>();
                map.put("commandDomain",commandDomain);
                map.put("command",command);
                map.put("args",args.get("trans_data"));
                Message msg = new Message();
                msg.obj = map;
                handler.sendMessage(msg);
            }
        });

        _webSession.setWebSessionListener(new WebSession.IWebSessionListener() {
            @Override
            public void onWebPlageLoadFailed(WebCommandSender sender, int errorCode, String description, String failingUrl) {
                String urlString = "file:///android_asset/logincenter_webpage_laod_failed.html";
                sender.redirectUri(Uri.parse(urlString));
            }

            @Override
            public void onWebSessionClosed() {
                IntlGameUtil.logd("IntlGameCenter","onWebSessionClosed");
            }
        });

    }
}
