package com.intl.usercenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.intl.IntlGame;
import com.intl.channel.FaceBookSDK;
import com.intl.channel.FaceBookWeb;
import com.intl.channel.GoogleSDK;
import com.intl.channel.Guest;
import com.intl.entity.IntlDefine;
import com.intl.utils.IntlGameExceptionUtil;
import com.intl.utils.IntlGameUtil;
import com.intl.webview.WebCommandSender;
import com.intl.webview.WebSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Dictionary;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGameCenter {
    private static final String LOGIN_CENTER_WEB_COMMAND_DOMAIN = "yc.mobilesdk.logincenter";
    private static final String PERSON_CENTER_WEB_COMMAND_DOMAIN = "yc.mobilesdk.personcenter";
    private static IntlGameCenter _instance;
    private WebSession _webSession;
    public Activity activity;
        ProgressDialog _progressDialog;

    public static void init(Activity activity){

        if (_instance == null)
            _instance = new IntlGameCenter(activity);
    }
    public static IntlGameCenter getInstance() {

        return _instance;
    }
    private IntlGameCenter()
    {

    }
    private IntlGameCenter(Activity activity)
    {
        this.activity = activity;
        _webSession = new WebSession();
        registCommand();
    }
    public void showLoginWebView(Activity activity,String _uri) {
        _webSession.showDialog(activity,
                414,
                319,
                Uri.parse(IntlGame._urlHost+"/index.html"), false);

    }

    public void PersonCenter(Activity activity)
    {
        Account act = loadAccounts(activity);
        if(act !=null)
        _webSession.showDialog(activity,414,319,Uri.parse(IntlGame._urlHost+"/usercenter.html?openid="+act.getOpenid()+"&access_token="+act.getAccessToken()),false);
    }
    public void LoginCenter(final Activity activity, final Boolean isBindApi)
    {
        if (_progressDialog != null && _progressDialog.isShowing()) {
            return;
        } else {
            Log.d("WEB", "AutoLoginBusy");
        }
        if (WebSession.getIsShwoingWebPage()) {
            return;
        }
        final Account account = loadAccounts(activity.getApplicationContext());
        if (account == null) {
            showLoginWebView(activity,IntlGame._urlHost+"/index.html");
            return;
        }
//        if(account.getAccessTokenExpire()>(System.currentTimeMillis()/1000))
        if(false)
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
                        IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_SUCCESS,jsonObject.optString("openid"),jsonObject.optString("access_token"),null);
                    }else{
                        IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED,null,null,errorMsg);
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
                        IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_SUCCESS,jsonObject.optString("openid"),jsonObject.optString("access_token"),null);
                        try {
                            IntlGameUtil.logd("IntlGameLoginCenter","Refresh Account=>"+account.getJSONObj().toString());
                        } catch (JSONException e) {
                            IntlGameExceptionUtil.handle(e);
                        }
                    }else {
                        IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED,null,null,errorMsg);
                    }

                }
            });
            refreshAPI.Excute();
        }

    }


    public Account loadAccounts(Context context) {
        return SessionCache.loadAccount(context);
    }
    private void setAccount(Context context, Account account) {
        SessionCache.saveAccounts(context, account);
    }
    private void registCommand()
    {
        _webSession.regisetCommandListener(PERSON_CENTER_WEB_COMMAND_DOMAIN,
                "close",
                new WebSession.IWebCommandListener() {
                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        WebSession.currentWebSession().forceCloseSession();

                    }
                });
        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,
                "close",
                new WebSession.IWebCommandListener() {
                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        WebSession.currentWebSession().forceCloseSession();
                        IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_CANCEL,"login cancel",null,null);

                    }
                });
        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Google",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
                        WebSession.currentWebSession().forceCloseSession();
                        GoogleSDK.login(activity,false);
                    }
                }

        );
        _webSession.regisetCommandListener(PERSON_CENTER_WEB_COMMAND_DOMAIN,"Google",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
                        WebSession.currentWebSession().forceCloseSession();
                        GoogleSDK.login(activity,true);
                    }
                }

        );
        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Facebook",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
                        WebSession.currentWebSession().forceCloseSession();
                        FaceBookSDK.login(activity,false);
//                        new FaceBookWeb(activity,false).show();
                    }
                }

        );
        _webSession.regisetCommandListener(PERSON_CENTER_WEB_COMMAND_DOMAIN,"Facebook",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
                        WebSession.currentWebSession().forceCloseSession();
                        FaceBookSDK.login(activity,true);
//                        new FaceBookWeb(activity,true).show();
                    }
                }

        );
        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Guest",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
                        WebSession.currentWebSession().forceCloseSession();
                        Guest.login(activity);
                    }
                }

        );

        _webSession.regisetCommandListener(PERSON_CENTER_WEB_COMMAND_DOMAIN,"Switch",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
                        WebSession.currentWebSession().forceCloseSession();
                        IntlGame.iPersonCenterListener.onComplete("switchroles",IntlDefine.SWITCH,null);
                    }
                }

        );

        _webSession.setWebSessionListener(new WebSession.IWebSessionListener() {
            @Override
            public void onWebPlageLoadFailed(WebCommandSender sender, int errorCode, String description, String failingUrl) {
                String urlString = "file:///android_asset/logincenter_webpage_laod_failed.html";
                sender.redirectUri(Uri.parse(urlString));
            }

            @Override
            public void onWebSessionClosed() {
            }
        });

    }
}
