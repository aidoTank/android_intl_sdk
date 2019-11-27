package com.intl.usercenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;

import com.intl.utils.IntlGameExceptionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: yujingliang
 * @Date: 2019/11/22
 */
public class SessionCache {
    private static String GP_SEESION_CACHE_NAME = "gp.auto.login";
    public static Account loadAccount(Context context)
    {
        SharedPreferences preference = context.getSharedPreferences(GP_SEESION_CACHE_NAME,Context.MODE_PRIVATE);
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
                String openid = jsonObj.optString("openid");
                String access_token = jsonObj.optString("access_token");
                return new Account(openid,access_token);
            }
            return null;

        }

    }

    public static void saveAccounts(Context context, Account account)
    {
        SharedPreferences preference = context.getSharedPreferences(GP_SEESION_CACHE_NAME, Context.MODE_PRIVATE);
        try {
            if (account != null) {
                JSONObject accountJSON = account.getJSONObj();
                preference.edit().putString("account", accountJSON.toString()).apply();
            }
        } catch (JSONException e) {
            IntlGameExceptionUtil.handle(e);
        }
    }
}
