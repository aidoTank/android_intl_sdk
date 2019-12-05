package com.intl.usercenter;

import com.intl.IntlGame;
import com.intl.httphelper.HttpThreadHelper;
import com.intl.utils.IntlGameExceptionUtil;
import com.intl.utils.IntlGameLoading;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/12/3
 */
public class GetAccessTokeTwoAPI {
    private IgetAccessTokenCallback igetAccessToken;
    private HttpThreadHelper httpThreadHelper;

    public GetAccessTokeTwoAPI(final Session session)
    {
        JSONObject jsonObject = new JSONObject();
        final String url = IntlGame.urlHost +"/api/auth/authorize/" + session.getChannel() + "?client_id=" + IntlGame.GPclientid;
        try{
            jsonObject.put("request_type", session.getRequestType());
            jsonObject.put("account_id", session.get_account_id());
            jsonObject.put("access_token", session.get_access_token());
            jsonObject.put("access_token_expire", session.get_access_token_expire());
            jsonObject.put("refresh_token", session.get_refresh_token());
            jsonObject.put("refresh_token_expire", session.get_refresh_token_expire());
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
                        igetAccessToken.AfterGetAccessToken(session.getChannel(),null,result.responseData.optString("ErrorMessage"));
                    }
                }else {

                    igetAccessToken.AfterGetAccessToken(session.getChannel(),null,result.ex.getMessage());
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
