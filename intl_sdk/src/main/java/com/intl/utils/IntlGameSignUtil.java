package com.intl.utils;

import com.intl.IntlGame;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: yujingliang
 * @Date: 2019/11/28
 */
public class IntlGameSignUtil {

    public static String Sign(JSONObject jsonObject,long tm)
    {
        HashMap<String,Object> data = new HashMap<>();
        data.put("tm",tm);
        Iterator it = jsonObject.keys();
        while (it.hasNext())
        {
            String key = it.next().toString();
            String value = null;
            try {
                value = jsonObject.get(key).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.put(key, value);
        }
        List<String> sorted = new ArrayList<>(data.keySet());
        StringBuilder sb = new StringBuilder();
        sb.append(IntlGame.GPsecretid);
        for(String key:sorted)
        {
            sb.append(key).append(data.get(key));
        }
        return SHA1(sb.toString());

    }
    private static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes("UTF-8"));
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte message : messageDigest) {
                String shaHex = Integer.toHexString(message & 0xFF);
                if (shaHex.length() < 2)
                    hexString.append(0);

                hexString.append(shaHex);
            }
            return hexString.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
