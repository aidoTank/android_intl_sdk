package com.intl.entity;

import com.intl.utils.IntlGameExceptionUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/11/27
 */
public class Account {

    private String _channel;
    private String _refresh_token;
    private int _refresh_token_expire;
    private String _openid;
    private String _access_token;
    private int _access_token_expire;
    private Boolean _first_authorize;
    public Account(String channel,String refresh_token,int refresh_token_expire,String openid,String access_token,int access_token_expire,boolean first_authorize){
        _channel = channel;
        _access_token = access_token;
        _access_token_expire = access_token_expire;
        _refresh_token_expire = refresh_token_expire;
        _openid = openid;
        _refresh_token = refresh_token;
        _first_authorize = first_authorize;
    }
    public Account(String channel, JSONObject jsonObject) {
        _channel = channel;
        try {
            _refresh_token = jsonObject.getString("refresh_token");
            _access_token_expire = jsonObject.getInt("access_token_expire");
            _refresh_token_expire = jsonObject.getInt("refresh_token_expire");
            _openid = jsonObject.getString("openid");
            _access_token = jsonObject.getString("access_token");
            _first_authorize = jsonObject.getBoolean("first_authorize");
        } catch (JSONException e) {
            IntlGameExceptionUtil.handle(e);
        }

    }

    public void setOpenid(String openid)
    {
        _openid = openid;
    }
    public void setAccessToken(String accessToken)
    {
        _access_token = accessToken;
    }
    public void setAccessTokenExprie(int accessTokenExprie)
    {
        _access_token_expire = accessTokenExprie;
    }
    public void setRefreshToken(String refreshToken)
    {
        _refresh_token = refreshToken;
    }
    public void setRefreshTokenExpire(int refreshTokenExpire)
    {
        _refresh_token_expire = refreshTokenExpire;
    }
    public void setIsFirstAuthorize(boolean first_authorize)
    {
        _first_authorize = first_authorize;
    }
    public String getChannel()
    {
        return _channel;
    }
    public String getOpenid()
    {
        return _openid;
    }
    public String getAccessToken()
    {
        return _access_token;
    }
    public   String getRefreshToken()
    {
        return _refresh_token;
    }
    public int getRefreshTokenExpire()
    {
        return _refresh_token_expire;
    }
    public int getAccessTokenExpire()
    {
        return _access_token_expire;
    }
    public boolean getIsFirstAuthorize()
    {
        return _first_authorize;
    }


    public JSONObject getJSONObj() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("channel",_channel);
        obj.put("refresh_token",_refresh_token);
        obj.put("refresh_token_expire",_refresh_token_expire);
        obj.put("openid", _openid);
        obj.put("access_token", _access_token);
        obj.put("access_token_expire",_access_token_expire);
        obj.put("first_authorize",_first_authorize);
        return obj;
    }
}
