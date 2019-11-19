package com.intl.webview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.ref.WeakReference;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class WebActivity extends Activity implements IWebPage {

    protected WebView _webView;
    WeakReference<WebSession> _hostWebSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();

        WebSession hostWebSession = WebSession.currentWebSession();
        _hostWebSession = new WeakReference<WebSession>(hostWebSession);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.setContentView(getApplicationContext().getResources().getIdentifier("activity_ycweb", "layout", getApplication().getPackageName()));//R.layout.activity_ycweb);
        _webView = (android.webkit.WebView) this.findViewById(getApplicationContext().getResources().getIdentifier("web_view", "id", getApplication().getPackageName()));//R.id.web_view);

        if (bundle.containsKey("orientation")) {
            this.setRequestedOrientation(bundle.getInt("orientation"));
        }
        else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }


        hostWebSession.onActivityOpen(this);

        _webView.getSettings().setJavaScriptEnabled(true);
        _webView.getSettings().setSupportZoom(false);
        _webView.getSettings().setBuiltInZoomControls(false);
        _webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        _webView.getSettings().setLoadWithOverviewMode(true);
        _webView.getSettings().setDomStorageEnabled(true);

        _webView.setWebViewClient(new WebPageClient(this));

        _webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        _webView.loadUrl(bundle.getString("url"));

    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 当新设置中，屏幕布局模式为横排时
        if(newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        {
            //TODO 某些操作
            sendEvent("orientation_changed", "{\"orientation\":\"" + 2 + "\"}");
        } else {
            sendEvent("orientation_changed", "{\"orientation\":\""+ 1 + "\"}");
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            if (null != _hostWebSession.get())
                _hostWebSession.get().onActivityClose();
        }
    }

    public void setSize(int widthPixels, int hightPixels) {
        throw new UnsupportedOperationException();
    }


    public void setFullScreen() {
        throw new UnsupportedOperationException();
    }

    public void sendEvent(String event, String evntData) {

        String jsString = "OnYCEvent("+ event +", "+ evntData+")";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            _webView.evaluateJavascript(jsString, null);
        } else {
            _webView.loadUrl("javascript:" + jsString);
        }
    }

    public WebSession getHostWebSession() {
        return _hostWebSession.get();
    }

    public void close() {
        this.finish();
    }

    public Context getContext() {
        return this;
    }


}
