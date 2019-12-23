package com.intl.api;

import com.intl.IntlGame;
import com.intl.httphelper.HttpThreadHelper;

import java.util.HashMap;

/**
 * @Author: yujingliang
 * @Date: 2019/12/18
 */
public class VerifyOrderAPI {
    private IVerifyOrderListener iVerifyOrderListener;
    private HttpThreadHelper httpThreadHelper;

    public VerifyOrderAPI(final HashMap<String,Object> postMap)
    {
        final String url = "http://eu-sdk-plat.ycgame.com/notify/"+ IntlGame.Game +"t11/google";
        httpThreadHelper = new HttpThreadHelper(
                postMap, url, new HttpThreadHelper.HttpCallback() {
            @Override
            public void onPostExecute(HttpThreadHelper.HttpResult result) {
                if(result.ex == null&&result.httpCode == 200)
                {
                    if(result.responseData.optInt("ok") == 0)
                    {
                        iVerifyOrderListener.AfterPay(0,result.responseData.toString());
                    }
                    else {
                        iVerifyOrderListener.AfterPay(1,result.responseData.toString());
                    }
                }else {
                    iVerifyOrderListener.AfterPay(-1,result.responseData.toString());
                }
            }
        }

        );

    }
    public void Excute() {
        httpThreadHelper.start();
    }
    public void setListener(IVerifyOrderListener listener)
    {
        iVerifyOrderListener = listener;
    }
    public interface IVerifyOrderListener {
        void AfterPay(int code, String result);
    }
}
