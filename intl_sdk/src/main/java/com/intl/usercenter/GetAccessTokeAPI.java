package com.intl.usercenter;

import android.annotation.SuppressLint;
import android.util.Log;

import com.intl.httphelper.AsyncHttpTask;
import com.intl.utils.IntlGameExceptionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @Author: yujingliang
 * @Date: 2019/11/27
 */
public class GetAccessTokeAPI {

    private Session _session;
    private Thread _httpTask = null;

    public GetAccessTokeAPI(final Session session)
    {
        _session = session;

    }
    public void asyncExcute() {
        final String url = "http://agg.ycgame.com/api/auth/authorize/" + _session.getChannel() + "?client_id=" + _session.getClientid();

        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("request_type", _session.getRequestType());
            jsonObject.put("code", _session.getAuthCode());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    try {
                        byte[] postData = jsonObject.toString().getBytes();
                        URL murl = new URL(url);
                        connection = (HttpURLConnection) murl.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setConnectTimeout(15000);
                        connection.setReadTimeout(20000);
                        connection.setInstanceFollowRedirects(false);
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("Charset", "UTF-8");
                        connection.connect();
                        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                        dos.write(postData);
                        dos.flush();
                        dos.close();
                        int responseCode = connection.getResponseCode();
                        if (HttpURLConnection.HTTP_OK == responseCode) {
//                            String result = streamToString(connection.getInputStream());
                            JSONObject result = new JSONObject(streamToString(connection.getInputStream()));
                            if(result.optInt("ErrorCode") == 0&& result.optString("ErrorMessage").equals("Successed"))
                            {

                            }

                        }
                    } catch (Exception e) {
                    }
                }
            }).start();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            IntlGameExceptionUtil.handle(e);
            return null;
        }
    }

    interface IgetAccessToken{
        void AfterGetAccessToken();
    }
}
