package com.intl.usercenter;

import com.intl.IntlGame;
import com.intl.httphelper.HttpThreadHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/11/27
 */
public class GetAccessTokeAPI {

    private IgetAccessToken igetAccessToken;
    private Session _session;
    private HttpThreadHelper httpThreadHelper;

    public GetAccessTokeAPI(final Session session)
    {
        _session = session;
        JSONObject jsonObject = new JSONObject();
        final String url = "http://agg.ycgame.com/api/auth/authorize/" + _session.getChannel() + "?client_id=" + IntlGame.GPclientid;
        try{
            jsonObject.put("request_type", _session.getRequestType());
            jsonObject.put("code", _session.getAuthCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpThreadHelper = new HttpThreadHelper(
                jsonObject, url, new HttpThreadHelper.HttpCallback() {
            @Override
            public void onPostExecute(HttpThreadHelper.HttpResult result) {
                if(result.ex == null||result.httpCode == 200)
                {
                    if(result.responseData.optInt("ErrorCode") == 0&& result.responseData.optString("ErrorMessage").equals("Successed"))
                    {
                        JSONObject datajson = result.responseData.optJSONObject("Data");
                        igetAccessToken.AfterGetAccessToken(_session.getChannel(),datajson);
                    }
                }
            }
        }

        );

    }
    public void Excute() {
        httpThreadHelper.start();
    }
    public void setListener(IgetAccessToken _igetAccessToken)
    {
        igetAccessToken = _igetAccessToken;
    }
    public interface IgetAccessToken{
        void AfterGetAccessToken(String channel,JSONObject accountJson);
    }
}
