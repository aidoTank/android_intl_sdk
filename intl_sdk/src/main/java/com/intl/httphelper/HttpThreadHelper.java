package com.intl.httphelper;

import com.intl.entity.Account;
import com.intl.utils.IntlGameExceptionUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * @Author: yujingliang
 * @Date: 2019/11/27
 */
public class HttpThreadHelper {
    private Thread thread;
    public HttpThreadHelper(final JSONObject jsonObject, final String url, final HttpCallback httpCallback)
    {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                HttpResult result = new HttpResult();
                result.ex = null;
                result.responseData = null;
                try {
                    byte[] postData = null;
                    String method = "GET";
                    if(jsonObject != null)
                    {
                        method = "POST";
                        postData  = jsonObject.toString().getBytes();
                    }
                    URL murl = new URL(url);
                    connection = (HttpURLConnection) murl.openConnection();
                    connection.setRequestMethod(method);
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(20000);
                    connection.setInstanceFollowRedirects(false);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Charset", "UTF-8");
                    connection.connect();
                    if(postData != null)
                    {
                        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                        dos.write(postData);
                        dos.flush();
                        dos.close();
                    }

                    int responseCode = connection.getResponseCode();
                    result.httpCode = responseCode;
                    if (HttpURLConnection.HTTP_OK == responseCode) {
                        result.responseData = new JSONObject(streamToString(connection.getInputStream()));
                    }
                } catch (Exception e) {
                    result.ex = e;
                    IntlGameExceptionUtil.handle(e);
                }
                httpCallback.onPostExecute(result);
            }
        });
    }
    public HttpThreadHelper(final Account account, final JSONObject jsonObject, final String url, final HttpCallback httpCallback)
    {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                HttpResult result = new HttpResult();
                result.ex = null;
                result.responseData = null;
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
                    connection.setRequestProperty("AccessToken",account.getAccessToken());
                    connection.setRequestProperty("OpenId",account.getOpenid());
                    connection.connect();
                    DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                    dos.write(postData);
                    dos.flush();
                    dos.close();
                    result.httpCode = connection.getResponseCode();
                    result.responseData = new JSONObject(streamToString(connection.getInputStream()));
                } catch (Exception e) {
                    result.ex = e;
                    IntlGameExceptionUtil.handle(e);
                }
                httpCallback.onPostExecute(result);
            }
        });
    }
    public HttpThreadHelper(final HashMap<String,String> headers , final JSONObject jsonObject, final String url, final HttpCallback httpCallback)
    {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                HttpResult result = new HttpResult();
                result.ex = null;
                result.responseData = null;
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
                    for (String key :headers.keySet())
                    {
                        connection.setRequestProperty(key,headers.get(key));
                    }
                    connection.connect();
                    DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                    dos.write(postData);
                    dos.flush();
                    dos.close();
                    result.httpCode = connection.getResponseCode();
                    result.responseData = new JSONObject(streamToString(connection.getInputStream()));
                } catch (Exception e) {
                    result.ex = e;
                    IntlGameExceptionUtil.handle(e);
                }
                httpCallback.onPostExecute(result);
            }
        });
    }

    public HttpThreadHelper(final HashMap<String,Object> postmap, final String url, final HttpCallback httpCallback )
    {
        final StringBuilder str = new StringBuilder();
        for(String key :postmap.keySet())
        {
            str.append(key).append("=").append(postmap.get(key)).append("&");
        }
        str.deleteCharAt(str.length()-1);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                HttpResult result = new HttpResult();
                result.ex = null;
                result.responseData = null;
                try {
                    byte[] postData = str.toString().getBytes();
                    String method = "POST";
                    URL murl = new URL(url);
                    connection = (HttpURLConnection) murl.openConnection();
                    connection.setRequestMethod(method);
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(20000);
                    connection.setInstanceFollowRedirects(false);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Charset", "UTF-8");
                    connection.connect();
                    if(postData != null)
                    {
                        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                        dos.write(postData);
                        dos.flush();
                        dos.close();
                    }

                    int responseCode = connection.getResponseCode();
                    result.httpCode = responseCode;
                    if (HttpURLConnection.HTTP_OK == responseCode) {
                        result.responseData = new JSONObject(streamToString(connection.getInputStream()));
                    }
                } catch (Exception e) {
                    result.ex = e;
                    IntlGameExceptionUtil.handle(e);
                }
                httpCallback.onPostExecute(result);
            }
        });
    }

    public HttpThreadHelper(final String url, final HttpCallback httpCallback)
    {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                HttpResult result = new HttpResult();
                result.ex = null;
                result.responseData = null;
                try {
                    URL murl = new URL(url);
                    connection = (HttpURLConnection) murl.openConnection();
                    connection.setRequestMethod("DELETE");
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(20000);
                    connection.setInstanceFollowRedirects(false);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Charset", "UTF-8");
                    connection.connect();
                    result.httpCode = connection.getResponseCode();
                    result.responseData = new JSONObject(streamToString(connection.getInputStream()));
                } catch (Exception e) {
                    result.ex = e;
                    IntlGameExceptionUtil.handle(e);
                }
                httpCallback.onPostExecute(result);
            }
        });
    }
    public void start()
    {
        thread.start();
    }
    private String streamToString(InputStream is) {
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

    public interface HttpCallback{
        void onPostExecute(HttpResult result);
    }

    public class HttpResult{
         public int httpCode;
         public Exception ex;
         public JSONObject responseData;
    }
}
