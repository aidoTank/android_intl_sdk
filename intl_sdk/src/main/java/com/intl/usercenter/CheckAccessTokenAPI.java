package com.intl.usercenter;

import com.intl.IntlGame;
import com.intl.httphelper.HttpThreadHelper;
import com.intl.utils.IntlGameLoading;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/11/28
 */
public class CheckAccessTokenAPI {

    private ICheckAccessTokenCallback iCheckAccessTokenCallback;
    private HttpThreadHelper httpThreadHelper;
    public CheckAccessTokenAPI(final Account account)
    {
        JSONObject jsonObject = new JSONObject();
        final String url = IntlGame.urlHost +"/api/auth/check/?client_id=" + IntlGame.GPclientid+"&debug=true";
        try{
            jsonObject.put("openid", account.getOpenid());
            jsonObject.put("access_token", account.getAccessToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IntlGameLoading.getInstance().show(IntlGameCenter.getInstance().activity.get());
        httpThreadHelper = new HttpThreadHelper(
                jsonObject, url, new HttpThreadHelper.HttpCallback() {
            @Override
            public void onPostExecute(HttpThreadHelper.HttpResult result) {
                IntlGameLoading.getInstance().hide();
                if(result.ex == null&&result.httpCode == 200)
                {
                    if(result.responseData.optInt("ErrorCode") == 0&& result.responseData.optString("ErrorMessage").equals("Successed"))
                    {
                        JSONObject datajson = result.responseData.optJSONObject("Data");
                        iCheckAccessTokenCallback.AfterCheck(datajson,null);
                    }
                    else {
                        iCheckAccessTokenCallback.AfterCheck(null,result.responseData.optString("ErrorMessage"));
                    }
                }else{
                    iCheckAccessTokenCallback.AfterCheck(null, result.ex != null ? result.ex.getMessage() : null);
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
        void AfterCheck(JSONObject jsonObject, String errorMsg);
    }
}
