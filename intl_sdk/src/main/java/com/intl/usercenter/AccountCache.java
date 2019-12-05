package com.intl.usercenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.intl.utils.IntlGameExceptionUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/11/22
 */
public class AccountCache {
    private static String GP_ACCOUNT_CACHE_NAME = "gp.auto.login";
    public static Account loadAccount(Context context)
    {
        SharedPreferences preference = context.getSharedPreferences(GP_ACCOUNT_CACHE_NAME,Context.MODE_PRIVATE);
        String jsonStr = preference.getString("account",null);
        if(jsonStr == null)
        {
            return null;
        }else {
            JSONObject jsonObj = null;
            try{
                jsonObj = new JSONObject(jsonStr);

            }catch (JSONException e){
                IntlGameExceptionUtil.handle(e);

            }
            if(jsonObj != null)
            {
                String channel = jsonObj.optString("channel");
                String refresh_token = jsonObj.optString("refresh_token");
                int refresh_token_expire = jsonObj.optInt("refresh_token_expire");
                String openid = jsonObj.optString("openid");
                String access_token = jsonObj.optString("access_token");
                int access_token_expire = jsonObj.optInt("access_token_expire");
                return new Account(channel,refresh_token,refresh_token_expire,openid,access_token,access_token_expire);
            }
            return null;

        }

    }

    public static void saveAccounts(Context context, Account account)
    {
        SharedPreferences preference = context.getSharedPreferences(GP_ACCOUNT_CACHE_NAME, Context.MODE_PRIVATE);
        try {
            if (account != null) {
                JSONObject accountJSON = account.getJSONObj();
                preference.edit().putString("account", accountJSON.toString()).apply();
            }
        } catch (JSONException e) {
            IntlGameExceptionUtil.handle(e);
        }
    }

    public static void cleanAccounts(Context context)
    {
        SharedPreferences preference = context.getSharedPreferences(GP_ACCOUNT_CACHE_NAME, Context.MODE_PRIVATE);
        if(preference != null)
        {
            preference.edit().remove("account").apply();
        }
    }
}
