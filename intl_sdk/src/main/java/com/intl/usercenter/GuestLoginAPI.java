package com.intl.usercenter;

import com.intl.IntlGame;
import com.intl.httphelper.HttpThreadHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/11/28
 */
public class GuestLoginAPI {
    private Session _session;
    private IGuestLoginCallback iGuestLoginCallback;
    private HttpThreadHelper httpThreadHelper;
    public GuestLoginAPI(Session session)
    {
        _session = session;
        JSONObject jsonObject = new JSONObject();
        final String url = "https://agg.ycgame.com/api/auth/authorize/?client_id=" + IntlGame.GPclientid;
        try{
            jsonObject.put("request_type", _session.getRequestType());
            jsonObject.put("unique_id", _session.getAuthCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpThreadHelper = new HttpThreadHelper(
                jsonObject, url, new HttpThreadHelper.HttpCallback() {
            @Override
            public void onPostExecute(HttpThreadHelper.HttpResult result) {
                if (result.ex == null && result.httpCode == 200) {
                    if (result.responseData.optInt("ErrorCode") == 0 && result.responseData.optString("ErrorMessage").equals("Successed")) {
                        JSONObject datajson = result.responseData.optJSONObject("Data");
                        iGuestLoginCallback.AfterGuestLogin(_session.getChannel(), datajson);
                    }
                } else {
                    iGuestLoginCallback.AfterGuestLogin(_session.getChannel(), null);
                }
            }
        }
        );
    }

    public void Excute() {
        httpThreadHelper.start();
    }
    public void setListener(IGuestLoginCallback listener)
    {
        iGuestLoginCallback = listener;
    }
    public interface IGuestLoginCallback{
        void AfterGuestLogin(String channel,JSONObject jsonObject);
    }
}
