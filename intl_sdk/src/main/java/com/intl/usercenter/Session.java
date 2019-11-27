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
    public Session(String channel,String authcode,String request_type)
    {
        _channel = channel;
        _authcode = authcode;
        _request_type = request_type;
    }
    public String getChannel()
    {
        return _channel;
    }

    public String getAuthCode()
    {
        return _authcode;
    }
    public String getRequestType()
    {
        return _request_type;
    }
    JSONObject getJSONObj() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("channel", _channel);
        obj.put("request_type", _request_type);
        obj.put("code", _authcode);
        return obj;
    }
}
