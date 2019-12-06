package com.intl.api;

import com.intl.IntlGame;
import com.intl.entity.Session;
import com.intl.httphelper.HttpThreadHelper;
import com.intl.usercenter.AccountCache;
import com.intl.usercenter.IntlGameCenter;
import com.intl.utils.IntlGameLoading;
import com.intl.utils.IntlGameUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: yujingliang
 * @Date: 2019/12/4
 */
public class GuestBindFAPI {
    private IGuestBindCallback iGuestBindCallback;
    private HttpThreadHelper httpThreadHelper;
    public GuestBindFAPI(Session session){
        JSONObject jsonObject = new JSONObject();
        final String url = IntlGame.urlHost +"/api/sources/guestbind/" + session.getChannel() + "?client_id=" + IntlGame.GPclientid;
        try{
            jsonObject.put("request_type", session.getRequestType());
            jsonObject.put("account_id", session.get_account_id());
            jsonObject.put("access_token", session.get_access_token());
            jsonObject.put("access_token_expire", session.get_access_token_expire());
            jsonObject.put("refresh_token", session.get_refresh_token());
            jsonObject.put("refresh_token_expire", session.get_refresh_token_expire());
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
                        IntlGameUtil.logd("IntlEX","GuestBindFBAPI error:"+result.responseData.toString());
                        iGuestBindCallback.AfterBind(-1,result.responseData.optString("ErrorMessage"));
                    }
                }else {
                    IntlGameUtil.logd("IntlEX","GuestBindFBAPI time out:"+ (result.ex != null ? result.ex.getMessage() : null));
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
