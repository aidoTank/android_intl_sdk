package com.intl.channel;

import android.app.Activity;

import com.intl.IntlGame;
import com.intl.entity.IntlDefine;
import com.intl.usercenter.Account;
import com.intl.usercenter.GuestLoginAPI;
import com.intl.usercenter.Session;
import com.intl.usercenter.SessionCache;
import com.intl.utils.IntlGameUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/11/28
 */
public class Guest {

    public static void login(final Activity activity)
    {
        Session session = new Session("ycgame",IntlGame.GooggleID,"guest");
        GuestLoginAPI guestLoginAPI = new GuestLoginAPI(session);
        guestLoginAPI.setListener(new GuestLoginAPI.IGuestLoginCallback() {
            @Override
            public void AfterGuestLogin(String channel, JSONObject jsonObject,String errorMsg) {
                if(jsonObject != null){
                    SessionCache.saveAccounts(activity,new Account(channel,jsonObject));
                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_SUCCESS,jsonObject.optString("openid"),jsonObject.optString("access_token"),null);
                }else {
                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED,null,null,errorMsg);
                }
            }
        });
        guestLoginAPI.Excute();
    }
}
