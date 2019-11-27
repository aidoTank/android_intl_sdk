package com.intl.httphelper;

import android.os.AsyncTask;
import android.util.Log;

import com.intl.utils.IntlGameExceptionUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by jerry on 2017/7/28.
 */

public abstract class AsyncHttpTask extends AsyncTask<String, String, AsyncHttpTask.AsyncHttpTaskResult> {

    @Override
    protected AsyncHttpTask.AsyncHttpTaskResult doInBackground(String... params) {
        try {
            String urlStr = params[0];
            String postStr = null;
            String method = "GET";
            if (params.length > 0) {
                method = params[1];
            }
            if (params.length > 1) {
                postStr = params[2];
            }
            URL url = new URL(urlStr);

            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod(method);// 提交模式

            if (postStr != null) {
                urlConnection.setDoOutput(true);// 是否输入参数

                StringBuffer postBuffer = new StringBuffer();
                postBuffer.append(postStr);
                byte[] bytes = postBuffer.toString().getBytes();
                urlConnection.connect();
                urlConnection.getOutputStream().write(bytes);// 输入参数 urlConnection.connect();
            } else {
                urlConnection.connect();
            }
            AsyncHttpTaskResult result = new AsyncHttpTaskResult();
            result.responseData = null;
            result.ex = null;

            InputStream inputStream = urlConnection.getInputStream();
            Map<String, List<String>> header = urlConnection.getHeaderFields();
            result.httpCode = urlConnection.getResponseCode();
            if (result.httpCode == 200 ) {
                String responseData = streamToString(inputStream);
                result.responseData = responseData;
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            AsyncHttpTaskResult result = new AsyncHttpTaskResult();
            result.httpCode = 0;
            result.responseData = null;
            result.ex = ex;
            StringBuffer resultBuffer = new StringBuffer();

            return result;
        }
    }
    @Override
    protected void onPostExecute(AsyncHttpTask.AsyncHttpTaskResult taskResult) {
        if (taskResult.ex == null || taskResult.httpCode == 200) {
            String jsonStr= taskResult.responseData;


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
    public class AsyncHttpTaskResult {
            public int httpCode;
            public Exception ex;
            public String responseData;
        }
}
