package com.intl.usercenter;

import com.intl.IntlGame;
import com.intl.httphelper.HttpThreadHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/11/28
 */
public class CheckAccessTokenAPI {

    private ICheckAccessTokenCallback iCheckAccessTokenCallback;
    private Account _account;
    private HttpThreadHelper httpThreadHelper;
    public CheckAccessTokenAPI(final Account account)
    {
        _account = account;
        JSONObject jsonObject = new JSONObject();
        final String url = "https://agg.ycgame.com/api/auth/check/?client_id=" + IntlGame.GPclientid+"&debug=true";
        try{
            jsonObject.put("openid", _account.getOpenid());
            jsonObject.put("access_token", _account.getAccessToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpThreadHelper = new HttpThreadHelper(
                jsonObject, url, new HttpThreadHelper.HttpCallback() {
            @Override
            public void onPostExecute(HttpThreadHelper.HttpResult result) {
                if(result.ex == null&&result.httpCode == 200)
                {
                    if(result.responseData.optInt("ErrorCode") == 0&& result.responseData.optString("ErrorMessage").equals("Successed"))
                    {
                        JSONObject datajson = result.responseData.optJSONObject("Data");
                        iCheckAccessTokenCallback.AfterCheck(datajson);
                    }
                    else {
                        iCheckAccessTokenCallback.AfterCheck(null);
                    }
                }else{
                    iCheckAccessTokenCallback.AfterCheck(null);
                }
            }
        }

        );
    }
    public void Excute() {
        httpThreadHelper.start();
    }
    public void setListener(ICheckAccessTokenCallback listener)
    {
        iCheckAccessTokenCallback = listener;
    }
    public interface ICheckAccessTokenCallback {
        void AfterCheck(JSONObject jsonObject);
    }
}
