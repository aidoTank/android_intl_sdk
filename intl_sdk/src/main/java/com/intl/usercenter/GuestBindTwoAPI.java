package com.intl.usercenter;

import com.intl.IntlGame;
import com.intl.httphelper.HttpThreadHelper;
import com.intl.utils.IntlGameLoading;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/12/4
 */
public class GuestBindTwoAPI {
    private IGuestBindCallback iGuestBindCallback;
    private Session _session;
    private HttpThreadHelper httpThreadHelper;
    public GuestBindTwoAPI(Session session){
        _session = session;
        JSONObject jsonObject = new JSONObject();
        final String url = IntlGame.urlHost +"/api/sources/guestbind/" + _session.getChannel() + "?client_id=" + IntlGame.GPclientid;
        try{
            jsonObject.put("request_type", _session.getRequestType());
            jsonObject.put("account_id", _session.get_account_id());
            jsonObject.put("access_token", _session.get_access_token());
            jsonObject.put("access_token_expire", _session.get_access_token_expire());
            jsonObject.put("refresh_token", _session.get_refresh_token());
            jsonObject.put("refresh_token_expire", _session.get_refresh_token_expire());
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
