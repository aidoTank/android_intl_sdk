package com.intl.loginchannel;

import android.app.Activity;

import com.intl.IntlGame;
import com.intl.entity.IntlDefine;
import com.intl.entity.Account;
import com.intl.api.AuthorizeGUAPI;
import com.intl.entity.Session;
import com.intl.usercenter.AccountCache;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * @Author: yujingliang
 * @Date: 2019/11/28
 */
public class Guest {

    public static void login(final WeakReference<Activity> activity)
    {
        Session session = new Session("ycgame",IntlGame.GooggleID,"guest");
        AuthorizeGUAPI guestLoginAPI = new AuthorizeGUAPI(session);
        guestLoginAPI.setListener(new AuthorizeGUAPI.IGuestLoginCallback() {
            @Override
            public void AfterGuestLogin(String channel, JSONObject jsonObject,String errorMsg) {
                if(jsonObject != null){
                    AccountCache.saveAccounts(activity.get(),new Account(channel,jsonObject));
                    IntlGame.isFirstUseLogin = false;
                    IntlGame.iLoginListener.onComplete(IntlDefine.SUCCESS,jsonObject.optString("openid"),jsonObject.optString("access_token"),null);
                }else {
                    IntlGame.iLoginListener.onComplete(IntlDefine.FAILED,null,null,errorMsg);
                }
            }
        });
        guestLoginAPI.Excute();
    }
    public static void logout()
    {
        if(IntlGame.iLogoutListener!= null)
            IntlGame.iLogoutListener.onComplete(IntlDefine.SUCCESS,null);
    }
}
