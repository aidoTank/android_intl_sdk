package com.intl.usercenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;

import com.intl.IntlGame;
import com.intl.channel.FaceBookSDK;
import com.intl.channel.GoogleSDK;
import com.intl.entity.IntlDefine;
import com.intl.webview.WebCommandSender;
import com.intl.webview.WebSession;

import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGameLoginCenter {
    private static final String LOGIN_CENTER_WEB_COMMAND_DOMAIN = "yc.mobilesdk.logincenter";
    @SuppressLint("StaticFieldLeak")
    private static IntlGameLoginCenter _instance;
    private Uri _uri;
    private  int _dialogWidth;
    private  int _dialogHeight;
    private WebSession _webSession;
    private Activity activity;
    ProgressDialog _progressDialog;

    public static void init(Activity activity, Uri uri, int width, int height){

        if (_instance == null)
            _instance = new IntlGameLoginCenter( activity,uri, width, height);
    }
    public static IntlGameLoginCenter getInstance() {

        return _instance;
    }
    private IntlGameLoginCenter(Activity activity, Uri uri, int width, int height)
    {
        this.activity = activity;
        _uri = uri;
        _dialogWidth = width;
        _dialogHeight = height;
        _webSession = new WebSession();
        registCommand();
    }
    public void showLoginWebView(Activity activity) {
        _webSession.showDialog(activity,
                _dialogWidth,
                _dialogHeight,
                _uri, false);
    }

    public void autoLogin(Activity activity)
    {
        if (_progressDialog != null && _progressDialog.isShowing()) {
            return;
        } else {
            Log.d("WEB", "AutoLoginBusy");
        }
        if (WebSession.getIsShwoingWebPage()) {
            return;
        }
        Account account = loadAccounts(activity.getApplicationContext());
        if (account == null) {
            showLoginWebView(activity);
            return;
        }
        if(account.getAccessTokenExpire()>System.currentTimeMillis())
        {
            //TODO:验证AccessToken的有效性
        }
        else{
            //TODO:刷新令牌
            //TODO：如果refreshToken无效则重新调起登录
        }
        _progressDialog = new ProgressDialog(activity);
        _progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return false;
                } else  {
                    return true;
                }
            }
        });
        _progressDialog.setCanceledOnTouchOutside(false);
        _progressDialog.setMessage("login...");
        _progressDialog.show();
//        AutoLoginAPI autoLoginAPI = new AutoLoginAPI(account);
//        autoLoginAPI.setListener(new )

    }
    private Account loadAccounts(Context context) {
        return SessionCache.loadAccount(context);
    }
    private void setAccount(Context context, Account account) {
        SessionCache.saveAccounts(context, account);
    }
    private void registCommand()
    {
        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Google",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
                        WebSession.currentWebSession().forceCloseSession();
                        GoogleSDK.login(activity);
                    }
                }

        );
        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Facebook",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
                        WebSession.currentWebSession().forceCloseSession();
                        FaceBookSDK.login(activity);
                    }
                }

        );
        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Guest",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
                    }
                }

        );
        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"closeerror",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("IntlGameLoginCenter", "handleCommand: "+command);
                        WebSession.currentWebSession().forceCloseSession();
                        //GoogleSDK.login(activity);
                        IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED,"onWebPlageLoadFailed");
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
