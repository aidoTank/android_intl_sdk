package com.intl.utils;

import android.content.Context;
import android.util.Log;

import com.facebook.internal.Validate;
import com.intl.IntlGame;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: yujingliang
 * @Date: 2020/1/15
 */
public class MsgManager {

    private static final String TAG = "MsgManager";
    public static JSONObject msg = null;
    public static String getMsg(String key)
    {
        try{
            String lan = IntlGameLanguageCache.loadLan(IntlContext.getApplicationContext());
            Log.i(TAG, "msg.getJSONObject("+lan+").getString("+key+")="+msg.getJSONObject(lan).getString(key));
            return msg.getJSONObject(lan).getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return key;
        }
    }
    public static void readParamsFromAssets(Context context)
    {
        BufferedReader reader = null;
        StringBuilder returnString = new StringBuilder();
        loop: {
            try {
                reader = new BufferedReader(new InputStreamReader(context.getAssets().open("msg.json"), "UTF-8"));

                while(true) {
                    String mLine;
                    if ((mLine = reader.readLine()) == null) {
                        break loop;
                    }

                    returnString.append(mLine);
                }
            } catch (IOException e) {

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }

            }

            return;
        }
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(returnString.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Validate.notNull(jsonObject, "msg.json");
        msg = jsonObject;
    }
}
