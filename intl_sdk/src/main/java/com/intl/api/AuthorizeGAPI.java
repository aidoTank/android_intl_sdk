package com.intl.api;

import com.intl.IntlGame;
import com.intl.entity.Session;
import com.intl.httphelper.HttpThreadHelper;
import com.intl.usercenter.IntlGameCenter;
import com.intl.utils.IntlGameExceptionUtil;
import com.intl.utils.IntlGameLoading;
import com.intl.utils.IntlGameUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/11/27
 */
public class AuthorizeGAPI {

    private IgetAccessTokenCallback igetAccessToken;
    private HttpThreadHelper httpThreadHelper;

    public AuthorizeGAPI(final Session session)
    {
        JSONObject jsonObject = new JSONObject();
        final String url = IntlGame.urlHost +"/api/auth/authorize/" + session.getChannel() + "?client_id=" + IntlGame.GPclientid;
        try{
            jsonObject.put("request_type", session.getRequestType());
            jsonObject.put("code", session.getAuthCode());

        } catch (JSONException e) {
            IntlGameExceptionUtil.handle(e);
        }
        IntlGameLoading.getInstance().show(IntlGameCenter.getInstance().activity.get());
        httpThreadHelper = new HttpThreadHelper(
                jsonObject, url, new HttpThreadHelper.HttpCallback() {
            @Override
            public void onPostExecute(HttpThreadHelper.HttpResult result) {
                IntlGameLoading.getInstance().hide();
                JSONObject datajson;
                if(result.ex == null&&result.httpCode == 200)
                {
                    if(result.responseData.optInt("ErrorCode") == 0&& result.responseData.optString("ErrorMessage").equals("Successed"))
                    {
                        datajson = result.responseData.optJSONObject("Data");
                        igetAccessToken.AfterGetAccessToken(session.getChannel(),datajson,null);
                    }
                    else {
                        IntlGameUtil.logd("IntlEX","GetAccessTokeGoogleAPI error:"+result.responseData.toString());
                        igetAccessToken.AfterGetAccessToken(session.getChannel(),null,result.responseData.optString("ErrorMessage"));
                    }
                }else {
                    IntlGameUtil.logd("IntlEX","GetAccessTokeGoogleAPI time out:"+ (result.ex != null ? result.ex.getMessage() : null));
                    igetAccessToken.AfterGetAccessToken(session.getChannel(),null,(result.ex != null ? result.ex.getMessage() : null));
                }
            }
        }

        );

    }
    public void Excute() {
        httpThreadHelper.start();
    }
    public void setListener(IgetAccessTokenCallback _igetAccessToken)
    {
        igetAccessToken = _igetAccessToken;
    }
    public interface IgetAccessTokenCallback {
        void AfterGetAccessToken(String channel, JSONObject accountJson, String errorMsg);
    }
}
