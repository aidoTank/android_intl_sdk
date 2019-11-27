package com.intl.httphelper;

import com.intl.utils.IntlGameExceptionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: yujingliang
 * @Date: 2019/11/27
 */
public class HttpThreadHelper{
    private Thread thread;
    public HttpThreadHelper(final JSONObject jsonObject, final String url, final HttpCallback httpCallbac)
    {
        init(jsonObject,url,httpCallbac);
    }

    private void init (final JSONObject jsonObject, final String url, final HttpCallback httpCallback)
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
                    connection.connect();
                    DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                    dos.write(postData);
                    dos.flush();
                    dos.close();
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
