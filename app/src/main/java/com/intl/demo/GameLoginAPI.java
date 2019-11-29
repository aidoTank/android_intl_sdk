package com.intl.demo;

import android.app.Activity;

import com.intl.IntlGame;
import com.intl.httphelper.HttpThreadHelper;
import com.intl.usercenter.Account;
import com.intl.usercenter.Session;
import com.intl.utils.IntlGameExceptionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @Author: yujingliang
 * @Date: 2019/11/28
 */
public class GameLoginAPI {

    private ILoginCallback iLoginCallback;
    private HttpThreadHelper httpThreadHelper;

    public GameLoginAPI(final Account account)
    {
        JSONObject jsonObject = new JSONObject();
        final String url = "http://pss-i.ycgame.com/member.ashx?q=3auth_n&ch=agl&did=99000736614286";
        try{
            jsonObject.put("openid", account.getOpenid());
            jsonObject.put("access_token", account.getAccessToken());
        } catch (JSONException e) {
            IntlGameExceptionUtil.handle(e);
        }
        httpThreadHelper = new HttpThreadHelper(
                jsonObject, url, new HttpThreadHelper.HttpCallback() {
            @Override
            public void onPostExecute(HttpThreadHelper.HttpResult result) {
                if(result.ex == null&&result.httpCode == 200)
                {
                    if(result.responseData.optInt("ok") == 0)
                    {
                        iLoginCallback.AfterLogin(result.responseData.toString());
                    }
                    else {
                        iLoginCallback.AfterLogin(result.responseData.toString());
                    }
                }else {
                    iLoginCallback.AfterLogin(result.responseData.toString());
                }
            }
        }

        );

    }
    public void Excute() {
        httpThreadHelper.start();
    }
    public void setListener(ILoginCallback listener)
    {
        iLoginCallback = listener;
    }
    public interface ILoginCallback {
        void AfterLogin(String result);
    }
}
