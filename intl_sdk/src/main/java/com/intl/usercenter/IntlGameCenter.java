package com.intl.usercenter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.intl.IntlGame;
import com.intl.channel.FaceBookSDK;
import com.intl.channel.GoogleSDK;
import com.intl.channel.Guest;
import com.intl.entity.IntlDefine;
import com.intl.utils.IntlGameExceptionUtil;
import com.intl.utils.IntlGameUtil;
import com.intl.webview.WebCommandSender;
import com.intl.webview.WebSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Dictionary;
import java.util.HashMap;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGameCenter {
    private static final String LOGIN_CENTER_WEB_COMMAND_DOMAIN = "yc.mobilesdk.logincenter";
    private static final String PERSON_CENTER_WEB_COMMAND_DOMAIN = "yc.mobilesdk.personcenter";
    private static IntlGameCenter _instance;
    private WebSession _webSession;
    public WeakReference<Activity> activity;
    private IntlGameHandler handler;
    static class IntlGameHandler extends Handler{
        WeakReference<Activity> activity;
        IntlGameHandler(WeakReference<Activity> activity){
            this.activity = activity;
        }
        @Override
        public void handleMessage(Message msg){
            HashMap Msgmap = (HashMap)msg.obj;
            WebSession.currentWebSession().forceCloseSession();
            if(String.valueOf(Msgmap.get("commandDomain")) .equals(LOGIN_CENTER_WEB_COMMAND_DOMAIN))
            {
                switch (String.valueOf( Msgmap.get("command"))){
                    case "close":
                        if(IntlGame.iLoginListener != null)
                        {
                            IntlGame.iLoginListener.onComplete(IntlDefine.CANCEL,"login cancel",null,null);
                        }
                        break;
                    case "Google":
                        GoogleSDK.login(this.activity,false);
                        break;
                    case "Facebook":
                        FaceBookSDK.login(activity,false);
                        break;
                    case "Guest":
                        Guest.login(activity);
                        break;
                }
                return;
            }

            if(String.valueOf(Msgmap.get("commandDomain")) .equals(PERSON_CENTER_WEB_COMMAND_DOMAIN)){
                switch (String.valueOf( Msgmap.get("command"))){
                    case "close":
                        if(IntlGame.iPersonCenterListener != null)
                        {
                            IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.CANCEL,null);
                        }
                        break;
                    case "Google":
                        GoogleSDK.login(activity,true);

                        break;
                    case "Facebook":
                        FaceBookSDK.login(activity,false);

                        break;
                    case "Switch":
                        if(IntlGame.iPersonCenterListener !=null){
                            IntlGame.iPersonCenterListener.onComplete("switchroles",IntlDefine.SUCCESS,null);
                        }
                        break;
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
    public void showLoginWebView(Activity activity,String _uri) {
        _webSession.showDialog(activity,
                414,
                319,
                Uri.parse(_uri), false);

    }

    public void PersonCenter(Activity activity)
    {
        Account act = loadAccounts(activity);
        if(act !=null)
        _webSession.showDialog(activity,414,319,Uri.parse(IntlGame.urlHost +"/usercenter.html?openid="+act.getOpenid()+"&access_token="+act.getAccessToken()),false);
    }
    public void LoginCenter(final Activity activity)
    {
        if (WebSession.getIsShwoingWebPage()) {
            return;
        }
        final Account account = loadAccounts(activity.getApplicationContext());
        if (account == null) {
            showLoginWebView(activity,IntlGame.urlHost +"/index.html");
            return;
        }
        if(account.getAccessTokenExpire()>IntlGameUtil.getUTCTimeStr())
//        if(false)
        {
            IntlGameUtil.logd("IntlGame","AccessTokenExpire =>"+account.getAccessTokenExpire()+" currentTime=>"+System.currentTimeMillis()/1000);
            CheckAccessTokenAPI checkAPI = new CheckAccessTokenAPI(account);
            checkAPI.setListener(new CheckAccessTokenAPI.ICheckAccessTokenCallback() {
                @Override
                public void AfterCheck(JSONObject jsonObject,String errorMsg) {
                    if(jsonObject != null)
                    {
                        IntlGameUtil.logd("IntlGame","AccessToken is Effective");
                        account.setOpenid(jsonObject.optString("openid"));
                        account.setAccessToken(jsonObject.optString("access_token"));
                        account.setAccessTokenExprie(jsonObject.optInt("access_token_expire"));
                        setAccount(activity,account);
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
                map.put("args",args);
                Message msg = new Message();
                msg.obj = map;
                handler.sendMessage(msg);
            }
        });
//        _webSession.regisetCommandListener(PERSON_CENTER_WEB_COMMAND_DOMAIN,
//                "close",
//                new WebSession.IWebCommandListener() {
//                    @Override
//                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
//                        WebSession.currentWebSession().forceCloseSession();
//                        Message msg = new Message();
//                        msg.what = IntlDefine.BIND_MSG;
//                        msg.obj = "bind";
//                        handler.sendMessage(msg);
//
//
//                    }
//                });
//        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,
//                "close",
//                new WebSession.IWebCommandListener() {
//                    @Override
//                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
//                        WebSession.currentWebSession().forceCloseSession();
//                        if(IntlGame.iLoginListener != null)
//                        {
//                            IntlGame.iLoginListener.onComplete(IntlDefine.CANCEL,"login cancel",null,null);
//                        }
//
//                    }
//                });
//        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Google",
//                new WebSession.IWebCommandListener() {
//
//                    @Override
//                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
//                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
//                        WebSession.currentWebSession().forceCloseSession();
//                        GoogleSDK.login(activity,false);
//                    }
//                }
//
//        );
//        _webSession.regisetCommandListener(PERSON_CENTER_WEB_COMMAND_DOMAIN,"Google",
//                new WebSession.IWebCommandListener() {
//                    @Override
//                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
//                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
//                        WebSession.currentWebSession().forceCloseSession();
//                        GoogleSDK.login(activity,true);
//                    }
//                }
//
//        );
//        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Facebook",
//                new WebSession.IWebCommandListener() {
//
//                    @Override
//                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
//                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
//                        WebSession.currentWebSession().forceCloseSession();
//                        FaceBookSDK.login(activity,false);
////                        new FaceBookWeb(activity,false).show();
//                    }
//                }
//
//        );
//        _webSession.regisetCommandListener(PERSON_CENTER_WEB_COMMAND_DOMAIN,"Facebook",
//                new WebSession.IWebCommandListener() {
//
//                    @Override
//                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
//                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
//                        WebSession.currentWebSession().forceCloseSession();
//                        FaceBookSDK.login(activity,true);
////                        new FaceBookWeb(activity,true).show();
//                    }
//                }
//
//        );
//        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Guest",
//                new WebSession.IWebCommandListener() {
//
//                    @Override
//                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
//                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
//                        WebSession.currentWebSession().forceCloseSession();
//                        Guest.login(activity);
//                    }
//                }
//
//        );
//
//        _webSession.regisetCommandListener(PERSON_CENTER_WEB_COMMAND_DOMAIN,"Switch",
//                new WebSession.IWebCommandListener() {
//
//                    @Override
//                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
//                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
//                        WebSession.currentWebSession().forceCloseSession();
//                        if(IntlGame.iPersonCenterListener !=null){
//                            IntlGame.iPersonCenterListener.onComplete("switchroles",IntlDefine.SUCCESS,null);
//                        }
//                    }
//                }
//
//        );

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
