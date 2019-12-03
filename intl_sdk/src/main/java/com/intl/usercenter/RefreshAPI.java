package com.intl.usercenter;

import com.intl.IntlGame;
import com.intl.httphelper.HttpThreadHelper;
import com.intl.utils.IntlGameExceptionUtil;
import com.intl.utils.IntlGameLoading;
import com.intl.utils.IntlGameSignUtil;
import com.intl.utils.IntlGameUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @Author: yujingliang
 * @Date: 2019/11/28
 */
public class RefreshAPI {
    private IRefreshCallback iRefreshCallback;
    private HttpThreadHelper httpThreadHelper;
    private Account _account;
    public RefreshAPI(Account account)
    {
        _account = account;
        JSONObject jsonObject = new JSONObject();
        HashMap<String,String> headers = new HashMap<>();
        final String url = IntlGame._urlHost+"/api/auth/refresh/?client_id=" + IntlGame.GPclientid;
        try{
            jsonObject.put("openid", account.getOpenid());
            jsonObject.put("refresh_token", account.getRefreshToken());
            String Timestamp = IntlGameUtil.getUTCTimeStr();
            String signstr = IntlGameSignUtil.Sign(jsonObject,Timestamp);
            headers.put("Authorization",signstr);
            headers.put("Timestamp", String.valueOf(Timestamp));
            IntlGameUtil.logd("RefreshAPI","UTC: "+Timestamp+" Sign: "+signstr);

        } catch (JSONException e) {
            IntlGameExceptionUtil.handle(e);
        }
        IntlGameLoading.getInstance().show(IntlGameCenter.getInstance().activity);
        httpThreadHelper = new HttpThreadHelper(
                headers,
                jsonObject, url, new HttpThreadHelper.HttpCallback() {
            @Override
            public void onPostExecute(HttpThreadHelper.HttpResult result) {
                if (result.ex == null || result.httpCode == 200) {
                    IntlGameLoading.getInstance().hide();
                    if (result.responseData.optInt("ErrorCode") == 0 && result.responseData.optString("ErrorMessage").equals("Successed")) {
                        JSONObject datajson = result.responseData.optJSONObject("Data");
                        IntlGameUtil.logd("RefreshAPI","refreshAccessToken Success!");
                        iRefreshCallback.AfterRefresh(datajson,null);
                    }else {
                        iRefreshCallback.AfterRefresh(null,result.responseData.optString("ErrorMessage"));
                    }
                }else {
                    iRefreshCallback.AfterRefresh(null,result.ex.getMessage());
                }
            }
        }

        );
    }

    public void Excute() {
        httpThreadHelper.start();
    }
    public void setListener(IRefreshCallback listener)
    {
        iRefreshCallback = listener;
    }
    public interface IRefreshCallback{
        void AfterRefresh(JSONObject jsonObject, String errorMsg);
    }
}
