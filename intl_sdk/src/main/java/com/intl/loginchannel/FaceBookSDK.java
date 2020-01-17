package com.intl.loginchannel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.intl.entity.IntlDefine;
import com.intl.IntlGame;
import com.intl.entity.Account;
import com.intl.api.AuthorizeFAPI;
import com.intl.api.GuestBindFAPI;
import com.intl.entity.Session;
import com.intl.usercenter.AccountCache;
import com.intl.utils.IntlGameLoading;
import com.intl.utils.IntlGameUtil;
import com.intl.utils.MsgManager;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yujingliang
 * @Date: 2019/11/8
 */
public class FaceBookSDK {

    private static CallbackManager callbackManager;
    private static LoginManager loginManager;
    /**
     * 登录
     */
    public static void login(final WeakReference<Activity> activity, final boolean isBind) {
        IntlGameLoading.getInstance().show(activity.get());
        logout();
        callbackManager = CallbackManager.Factory.create();
        getLoginManager().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // login success
                AccessToken accessToken = loginResult.getAccessToken();
                getLoginInfo(accessToken,isBind,activity);
            }

            @Override
            public void onCancel() {

                //取消登录
                if(isBind)
                {
                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_CANCEL,null,MsgManager.getMsg("cancel"));
                }else {
                    IntlGame.iLoginListener.onComplete(IntlDefine.CANCEL,null, null,MsgManager.getMsg("cancel"));
                }

            }

            @Override
            public void onError(FacebookException error) {

                //登录错误
                if(isBind)
                {
                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_FAILED,null,MsgManager.getMsg("error_login_fb"));
                }else {
                    IntlGame.iLoginListener.onComplete(IntlDefine.FAILED, null,null, MsgManager.getMsg("error_login_fb"));
                }
            }
        });
        IntlGameLoading.getInstance().hide();
        //判断当前token，如果不为空，则已经获取过权限，否则读取权限走registerCallback回调
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Profile profile = Profile.getCurrentProfile();
        if (accessToken == null || accessToken.isExpired() || profile == null) {
            LoginManager.getInstance().logInWithReadPermissions(activity.get(), Arrays.asList("public_profile"));
        } else {
            getLoginInfo(accessToken,isBind,activity);
        }
    }

    /**
     * 登录
     */
    public static void SwitchLogin(final WeakReference<Activity> activity) {
        IntlGameLoading.getInstance().show(activity.get());
        logout();
        callbackManager = CallbackManager.Factory.create();
        getLoginManager().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // login success
                AccessToken accessToken = loginResult.getAccessToken();
                getLoginInfo(accessToken,activity);
            }

            @Override
            public void onCancel() {
                IntlGame.iSwitchAccountListener.onComplete(IntlDefine.CANCEL,null, null,MsgManager.getMsg("cancel"));

            }

            @Override
            public void onError(FacebookException error) {
                //登录错误
                IntlGame.iSwitchAccountListener.onComplete(IntlDefine.FAILED, null,null,MsgManager.getMsg("error_login_fb"));
            }
        });
        IntlGameLoading.getInstance().hide();
        //判断当前token，如果不为空，则已经获取过权限，否则读取权限走registerCallback回调
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Profile profile = Profile.getCurrentProfile();
        if (accessToken == null || accessToken.isExpired() || profile == null) {
            LoginManager.getInstance().logInWithReadPermissions(activity.get(), Arrays.asList("public_profile"));
        } else {
            getLoginInfo(accessToken,activity);
        }
    }

    public static void logout() {
        if (IsLoggedIn())
        {
            Log.d("FacebookSDK", "logout");
            getLoginManager().logOut();
        }
    }


    /**
     * 获取登录信息
     *
     * @param accessToken
     */
    private static void getLoginInfo(final AccessToken accessToken, final boolean _isBind,final WeakReference<Activity> activity) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, final GraphResponse response) {
                if (object != null) {
                    Session session = new Session("facebook",accessToken.getToken());
                    session.set_account_id(accessToken.getUserId());
                    session.set_access_token_expire(accessToken.getDataAccessExpirationTime().getTime()/1000);
                    if(_isBind)
                    {
                        GuestBindFAPI guestBindAPI = new GuestBindFAPI(session);
                        guestBindAPI.setListener(new GuestBindFAPI.IGuestBindCallback() {
                            @Override
                            public void AfterBind(int resultCode,JSONObject accountJson,String errorMsg) {
                                if(IntlGame.iLoginListener ==null)
                                    return;
                                if(resultCode == 0)
                                {
                                    IntlGameUtil.logd("GuestBindAPI","Bind success!");
                                    AccountCache.setAccountsChannel(activity.get(),"facebook");
                                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_SUCCESS,null,MsgManager.getMsg("login_connect_account_success"));
                                } else if(resultCode == 10010){
                                    IntlGameUtil.logd("GuestBindAPI","Bind failed!===>该账户已经绑定了游客账号");
                                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.HAVE_BIND,null,MsgManager.getMsg("already_bind"));
                                }else {
                                    IntlGameUtil.logd("GuestBindAPI","Bind failed!");
                                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_FAILED,null,MsgManager.getMsg("error_login_fb"));
                                }

                            }
                        });
                        guestBindAPI.Excute();
                    }else {

                        final AuthorizeFAPI accessTokeAPI = new AuthorizeFAPI(session);
                        accessTokeAPI.setListener(new AuthorizeFAPI.IgetAccessTokenCallback() {
                            @Override
                            public void AfterGetAccessToken(String channel,JSONObject accountJson,String errorMsg) {
                                if(IntlGame.iLoginListener == null)
                                    return;
                                if(accountJson != null)
                                {
                                    Account userac = new Account(channel,accountJson);
                                    boolean first_authorize = userac.getIsFirstAuthorize();
                                    if(first_authorize){
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("user_id", accountJson.optString("openid"));
                                        IntlGame.AfEvent(activity.get(), "af_complete_registration", map);
                                    }
                                    AccountCache.saveAccounts(activity.get(),userac);
                                    IntlGame.iLoginListener.onComplete(IntlDefine.SUCCESS,accountJson.optString("openid"),accountJson.optString("access_token"),null);
                                }else {
                                    IntlGame.iLoginListener.onComplete(IntlDefine.FAILED,null,null,MsgManager.getMsg("please_check_connect_internet"));
                                }

                            }
                        });
                        accessTokeAPI.Excute();
                    }
                }else{
                    if(_isBind)
                    {
                        if(IntlGame.iPersonCenterListener != null)
                            IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_FAILED,null,MsgManager.getMsg("error_login_fb"));
                    }else {
                        if(IntlGame.iLoginListener != null)
                            IntlGame.iLoginListener.onComplete(IntlDefine.FAILED,null,null,MsgManager.getMsg("error_login_fb"));
                    }
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone,age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();

    }

    /**
     * 获取登录信息
     *
     * @param accessToken
     */
    private static void getLoginInfo(final AccessToken accessToken,final WeakReference<Activity> activity) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, final GraphResponse response) {
                if (object != null) {
                    Session session = new Session("facebook",accessToken.getToken());
                    session.set_account_id(accessToken.getUserId());
                    session.set_access_token_expire(accessToken.getDataAccessExpirationTime().getTime()/1000);
                    final AuthorizeFAPI accessTokeAPI = new AuthorizeFAPI(session);
                    accessTokeAPI.setListener(new AuthorizeFAPI.IgetAccessTokenCallback() {
                        @Override
                        public void AfterGetAccessToken(String channel,JSONObject accountJson,String errorMsg) {
                            if(IntlGame.iSwitchAccountListener == null)
                                return;
                            if(accountJson != null)
                            {
                                Account userac = new Account(channel,accountJson);
                                boolean first_authorize = userac.getIsFirstAuthorize();
                                if(first_authorize){
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("user_id", accountJson.optString("openid"));
                                    IntlGame.AfEvent(activity.get(), "af_complete_registration", map);
                                }
                                AccountCache.saveAccounts(activity.get(),userac);
                                IntlGame.iSwitchAccountListener.onComplete(IntlDefine.SWITCH_SUCCESS,accountJson.optString("openid"),accountJson.optString("access_token"),null);
                            }else {
                                IntlGame.iSwitchAccountListener.onComplete(IntlDefine.SWITCH_FAILED,null,null,MsgManager.getMsg("please_check_connect_internet"));
                            }

                        }
                    });
                    accessTokeAPI.Excute();
                } else{

                    if(IntlGame.iSwitchAccountListener != null)
                        IntlGame.iSwitchAccountListener.onComplete(IntlDefine.SWITCH_FAILED,null,null,MsgManager.getMsg("please_check_connect_internet"));
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone,age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();

    }


    /**
     * 是否登录成功
     * @return
     */
    public static boolean IsLoggedIn()
    {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }


    /**
     * 获取loginMananger
     *
     * @return
     */
    private static LoginManager getLoginManager() {
        if (loginManager == null) {
            loginManager = LoginManager.getInstance();
        }
        return loginManager;
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
