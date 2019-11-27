package com.intl.usercenter;

import com.intl.IntlGame;
import com.intl.httphelper.HttpThreadHelper;
import com.intl.utils.IntlGameExceptionUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/11/27
 */
public class AutoLoginAPI {
    private Account _account;
    private IAutoLoginListener iAutoLoginListener;
    private HttpThreadHelper httpThreadHelper = null;
    public AutoLoginAPI(final Account accout)
    {
        _account = accout;
        JSONObject jsonObject = new JSONObject();
        final String url = "http://agg.ycgame.com/api/auth/authorize/" + accout.getChannel() + "?client_id=" + IntlGame.GPclientid;

        try{
            jsonObject.put("request_type", accout.getAccessToken());
        }catch (JSONException e){
            IntlGameExceptionUtil.handle(e);
        }
        httpThreadHelper = new HttpThreadHelper(jsonObject, url, new HttpThreadHelper.HttpCallback() {
            @Override
            public void onPostExecute(HttpThreadHelper.HttpResult result) {
                if(result.ex == null||result.httpCode == 200)
                {
                    if(result.responseData.optInt("ErrorCode") == 0&& result.responseData.optString("ErrorMessage").equals("Successed"))
                    {
                        JSONObject datajson = result.responseData.optJSONObject("Data");
                        iAutoLoginListener.AfterLogin(datajson);
                    }
                }
            }
        });

    }
    void Excute() {
        if(httpThreadHelper != null)
            httpThreadHelper.start();
    }

    void setListener(IAutoLoginListener listener) {
        iAutoLoginListener = listener;
    }

    interface  IAutoLoginListener {
        void AfterLogin(JSONObject jsonObject);
    }
}
