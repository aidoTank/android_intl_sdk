package com.intl.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.intl.entity.Account;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2020/1/6
 */
public class IntlGameLanguageCache {
    private static String GP_LANGUAGE_CACHE_NAME = "agl.lan";
    public static String loadLan(Context context)
    {
        SharedPreferences preference = context.getSharedPreferences(GP_LANGUAGE_CACHE_NAME,Context.MODE_PRIVATE);
        if(!preference.contains("language"))
        {
            return IntlGameUtil.getDeviceLanguage(context);
        }
        return preference.getString("language","en");
    }

    public static void saveLan(Context context, String language)
    {
        SharedPreferences preference = context.getSharedPreferences(GP_LANGUAGE_CACHE_NAME, Context.MODE_PRIVATE);
        if (language != null) {
            preference.edit().putString("language", language).apply();
        }
    }

}
