package com.intl.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/11/27
 */
public class Session {

    private String _channel;
    private String _authcode;
    private String _request_type;
    private String _account_id;
    private String _access_token;
    private int _access_token_expire;
    private String _refresh_token;
    private int _refresh_token_expire;

    public Session(String channel,String authcode,String request_type)
    {
        _channel = channel;
        _authcode = authcode;
        _request_type = request_type;
    }
    public Session()
    {}
    public String getChannel()
    {
        return _channel;
    }
    public String getRequestType()
    {
        return _request_type;
    }
    public String getAuthCode()
    {
        return _authcode;
    }
    public String get_account_id()
    {
        return _account_id;
    }
    public String get_access_token()
    {
        return _access_token;
    }
    public int get_access_token_expire()
    {
        return _access_token_expire;
    }
    public String get_refresh_token()
    {
        return _refresh_token;
    }
    public int get_refresh_token_expire()
    {
        return _refresh_token_expire;
    }

    public void setChannel(String channel)
    {
        _channel = channel;
    }
    public void settAuthCode(String authCode)
    {
         _authcode = authCode;
    }
    public void set_account_id(String account_id)
    {
         _account_id = account_id;
    }
    public void set_access_token(String access_token)
    {
         _access_token = access_token;
    }
    public void set_access_token_expire(int access_token_expire)
    {
         _access_token_expire = access_token_expire;
    }
    public void set_refresh_token(String refresh_token)
    {
         _refresh_token = refresh_token;
    }
    public void set_refresh_token_expire(int refresh_token_expire)
    {
         _refresh_token_expire = refresh_token_expire;
    }
    JSONObject getJSONObj() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("channel", _channel);
        obj.put("request_type", _request_type);
        obj.put("account_id", _account_id);
        obj.put("access_token", _access_token);
        obj.put("access_token_expire", _access_token_expire);
        obj.put("refresh_token", _refresh_token);
        obj.put("refresh_token_expire", _refresh_token_expire);

        return obj;
    }
}
