package com.intl.channel;

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
import com.intl.usercenter.Account;
import com.intl.usercenter.GetAccessTokeOneAPI;
import com.intl.usercenter.GuestBindAPI;
import com.intl.usercenter.Session;
import com.intl.usercenter.SessionCache;
import com.intl.utils.IntlGameUtil;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: yujingliang
 * @Date: 2019/11/8
 */
public class FaceBookSDK {

    private static Activity act;
    private static CallbackManager callbackManager;
    private static List<String> permissions;
    private static LoginManager loginManager;
    /**
     * 登录
     */
    public static void login(Activity activity, final boolean isBind) {
        act = activity;
        callbackManager = CallbackManager.Factory.create();
        getLoginManager().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // login success
                AccessToken accessToken = loginResult.getAccessToken();
                getLoginInfo(accessToken,isBind);
            }

            @Override
            public void onCancel() {

                //取消登录
                if(isBind)
                {
                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_CANCEL,null);
                }else {
                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_CANCEL,null, null,null);
                }

            }

            @Override
            public void onError(FacebookException error) {

                //登录错误
                if(isBind)
                {
                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_FAILED,error.toString());
                }else {
                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED, null,null,error.toString());
                }
            }
        });

        //判断当前token，如果不为空，则已经获取过权限，否则读取权限走registerCallback回调
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Profile profile = Profile.getCurrentProfile();
        if (accessToken == null || accessToken.isExpired() || profile == null) {
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
        } else {
            getLoginInfo(accessToken,isBind);
        }
    }

    public static void logout() {
            Log.d("FacebookSDK", "logout");
            getLoginManager().logOut();
    }


    /**
     * 获取登录信息
     *
     * @param accessToken
     */
    private static void getLoginInfo(final AccessToken accessToken, final boolean _isBind) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    Session session = new Session("facebook",accessToken.getToken(),"code");
                    if(_isBind)
                    {
                        GuestBindAPI guestBindAPI = new GuestBindAPI(session);
                        guestBindAPI.setListener(new GuestBindAPI.IGuestBindCallback() {
                            @Override
                            public void AfterBind(int resultCode,String errorMsg) {
                                if(resultCode == 0)
                                {
                                    IntlGameUtil.logd("GuestBindAPI","Bind success!");
                                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_SUCCESS,errorMsg);
                                }else {
                                    IntlGameUtil.logd("GuestBindAPI","Bind failed!");
                                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_FAILED,errorMsg);
                                }

                            }
                        });
                        guestBindAPI.Excute();
                    }else {
                        final GetAccessTokeOneAPI accessTokeAPI = new GetAccessTokeOneAPI(session);
                        accessTokeAPI.setListener(new GetAccessTokeOneAPI.IgetAccessTokenCallback() {
                            @Override
                            public void AfterGetAccessToken(String channel,JSONObject accountJson,String errorMsg) {
                                if(accountJson != null)
                                {
                                    SessionCache.saveAccounts(act,new Account(channel,accountJson));

                                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_SUCCESS,accountJson.optString("openid"),accountJson.optString("access_token"),null);
                                }else {
                                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED,null,null,errorMsg);
                                }

                            }
                        });
                        accessTokeAPI.Excute();
                    }                }else{
//                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED,response.getRawResponse());
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone,age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();

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
