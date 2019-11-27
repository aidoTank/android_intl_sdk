package com.intl.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/11/27
 */
public class Account {

    private String _refresh_token;
    private String _openid;
    private String _access_token;
    private int _refresh_token_expire;
    private int _access_token_expire;
    Account(String refresh_token,int refresh_token_expire,String openid,String access_token,int access_token_expire){
        _access_token = access_token;
        _access_token_expire = access_token_expire;
        _refresh_token_expire = refresh_token_expire;
        _openid = openid;
        _access_token = access_token;
    }
    String getOpenid()
    {
        return _openid;
    }
    String getAccessToken()
    {
        return _access_token;
    }
    String getRefreshToken()
    {
        return _refresh_token;
    }


    JSONObject getJSONObj() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("openid", _openid);
        obj.put("access_token", _access_token);
        return obj;
    }
}
