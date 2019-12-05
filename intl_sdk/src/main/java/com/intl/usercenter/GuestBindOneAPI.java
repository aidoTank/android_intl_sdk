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
public class GuestBindOneAPI {
    private IGuestBindCallback iGuestBindCallback;
    private HttpThreadHelper httpThreadHelper;
    public GuestBindOneAPI(Session session){
        JSONObject jsonObject = new JSONObject();
        final String url = IntlGame.urlHost +"/api/sources/guestbind/" + session.getChannel() + "?client_id=" + IntlGame.GPclientid;
        try{
            jsonObject.put("request_type", session.getRequestType());
            jsonObject.put("code", session.getAuthCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IntlGameLoading.getInstance().show(IntlGameCenter.getInstance().activity.get());
        httpThreadHelper = new HttpThreadHelper(
                AccountCache.loadAccount(IntlGameCenter.getInstance().activity.get()),
                jsonObject, url, new HttpThreadHelper.HttpCallback() {
            @Override
            public void onPostExecute(HttpThreadHelper.HttpResult result) {
                IntlGameLoading.getInstance().hide();
                if(result.ex == null&&result.httpCode == 200)
                {
                    if(result.responseData.optInt("ErrorCode") == 0&& result.responseData.optString("ErrorMessage").equals("Successed"))
                    {
                        JSONObject datajson = result.responseData.optJSONObject("Data");
                        iGuestBindCallback.AfterBind(0,null);
                    }
                    else {
                        iGuestBindCallback.AfterBind(-1,result.responseData.optString("ErrorMessage"));
                    }
                }else {
                    iGuestBindCallback.AfterBind(-1, result.ex != null ? result.ex.getMessage() : null);
                }
            }
        }
        );



    }
    public void Excute() {
        httpThreadHelper.start();
    }
    public void setListener(IGuestBindCallback listener)
    {
        iGuestBindCallback = listener;
    }
    public interface IGuestBindCallback{
        void AfterBind(int resultCode, String errorMsg);
    }

}
