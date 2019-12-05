package com.intl.channel;

import android.app.Activity;

import com.intl.IntlGame;
import com.intl.entity.IntlDefine;
import com.intl.usercenter.Account;
import com.intl.usercenter.GuestLoginAPI;
import com.intl.usercenter.Session;
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
        GuestLoginAPI guestLoginAPI = new GuestLoginAPI(session);
        guestLoginAPI.setListener(new GuestLoginAPI.IGuestLoginCallback() {
            @Override
            public void AfterGuestLogin(String channel, JSONObject jsonObject,String errorMsg) {
                if(jsonObject != null){
                    AccountCache.saveAccounts(activity.get(),new Account(channel,jsonObject));
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
