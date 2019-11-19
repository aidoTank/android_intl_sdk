package com.intl.webview;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Iterator;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */

public class WebCommandSender {

    private WeakReference<WebView> _webView;
    private WeakReference<IWebPage> _webPage;
    private String _commandIdentity;


    WebCommandSender(IWebPage senderWebPage, WebView webView, String identity) {
        _webPage = new WeakReference<>(senderWebPage);
        _webView = new WeakReference<>(webView);
        _commandIdentity = identity;
    }

    IWebPage getWebPage() {
        return _webPage.get();
    }

    WebView getWebView() {
        return _webView.get();
    }

    public Context getContext() {
        return _webPage.get().getContext().getApplicationContext();
    }

    public void onWebCommandResponse(int code, String message, JSONObject resultData) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);
            jsonObject.put("message", message);
            if (resultData != null) {
                Iterator<String> keys = resultData.keys();
                while ( keys.hasNext()) {
                    String key = keys.next();
                    jsonObject.put(key, resultData.get(key));
                }
            }

            String jsonString = jsonObject.toString();
            String jsString = "OnYCCommandResponse("+ _commandIdentity +", "+ jsonString+")";
            Log.d("WEB_PAGE", jsonString);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (_webView.get() != null) {
                    _webView.get().evaluateJavascript(jsString, null);
                }
            } else {
                if (_webView.get() != null) {
                    _webView.get().loadUrl("javascript:" + jsString);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void redirectUri(Uri uri) {
        _webView.get().loadUrl(uri.toString());
    }
}
