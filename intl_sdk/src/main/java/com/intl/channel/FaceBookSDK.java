package com.intl.channel;

import android.app.Activity;
import android.app.ProgressDialog;
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
    private static ProgressDialog fbprogressDialog;

    /**
     * 登录
     */
    public static void login(Activity activity) {
        fbprogressDialog = new ProgressDialog(activity);
        fbprogressDialog.setMessage("Loading...");
        fbprogressDialog.show();
        act = activity;
        callbackManager = CallbackManager.Factory.create();
        getLoginManager().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // login success
                AccessToken accessToken = loginResult.getAccessToken();
                getLoginInfo(accessToken);
            }

            @Override
            public void onCancel() {

                //取消登录
                IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_CANCEL,"");

            }

            @Override
            public void onError(FacebookException error) {

                //登录错误
                IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED,error.getMessage());
            }
        });

        //判断当前token，如果不为空，则已经获取过权限，否则读取权限走registerCallback回调
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Profile profile = Profile.getCurrentProfile();
        if (accessToken == null || accessToken.isExpired() || profile == null) {
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile, email, user_birthday, user_friends"));
        } else {
            getLoginInfo(accessToken);
        }

        diss();

    }

    public static void logout() {
        if (IsLoggedIn(act))
        {
            Log.d("FacebookSDK", "logout");
            getLoginManager().logOut();
        }
    }

    /**
     * 是否登录成功
     * @param activity
     * @return
     */
    public static boolean IsLoggedIn(Activity activity)
    {
        if (activity == null)
        {
            return false;
        }
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }

    /**
     * 获取登录信息
     *
     * @param accessToken
     */
    private static void getLoginInfo(final AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_SUCCESS,accessToken.getToken());
                }else{
                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED,response.getRawResponse());
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


    private static void diss()
    {
        fbprogressDialog.dismiss();
    }
}
