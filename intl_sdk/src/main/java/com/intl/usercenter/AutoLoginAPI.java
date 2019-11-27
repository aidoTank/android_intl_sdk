package com.intl.usercenter;

import android.annotation.SuppressLint;

import com.intl.httphelper.AsyncHttpTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * @Author: yujingliang
 * @Date: 2019/11/27
 */
public class AutoLoginAPI {
    private Account _account;
    private IAutoLoginListener _listener;
    private AsyncHttpTask _httpTask = null;
    public AutoLoginAPI(final Account accout)
    {
        _account = accout;
        _httpTask = new AsyncHttpTask() {
            @Override
            protected void onPostExecute(AsyncHttpTask.AsyncHttpTaskResult taskResult) {
                if (taskResult.ex == null || taskResult.httpCode == 200) {
                    String jsonStr = taskResult.responseData;
                } else {
                    if (_listener != null ) {
                        _listener.AfterLogin(_account);
                    }
                    if (taskResult.hashCode() == 0) {

                    } else  {

                    }

                }
            }
        };

    }
    void asyncExcute() {
        String url = "https://app-loginapi.ycgame.com/api/v1/auth.ashx";
        String postString = String.format("request_type=%s&code=%s",
                _account.getOpenid(),
                _account.getAccessToken());

        _httpTask.execute(url, "POST", postString);
    }

    void setListener(IAutoLoginListener listener) {
        _listener = listener;
    }

    interface  IAutoLoginListener {
        void AfterLogin(Account account);
    }
}
