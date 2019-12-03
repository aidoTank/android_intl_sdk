package com.intl.webview;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.lang.ref.WeakReference;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */

public class WebDialog extends Dialog implements IWebPage {


    private WebView _webView;
    private boolean _isEnableBackKey;

    private RelativeLayout _symbolView = null;
    private LinearLayout _containerLayout  = null;

    private boolean _isFullScreen = false;
    private int hightPixels;
    private int widthPixels;

    private WeakReference<WebSession> _hostWebSession;

    public WebDialog(Context context, Uri uri, int with, int hight, boolean enableBackKey, WebSession hostWebSession) {
        super(context, android.R.style.Theme_Dialog);
        _hostWebSession = new WeakReference<WebSession>(hostWebSession);
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(context.getApplicationContext().getResources().getIdentifier("activity_ycweb_dialog", "layout", context.getPackageName()));
        _isEnableBackKey = enableBackKey;
        _webView =  this.findViewById(context.getApplicationContext().getResources().getIdentifier("web_view", "id", context.getPackageName()));//R.id.web_view);
        _symbolView = this.findViewById(context.getApplicationContext().getResources().getIdentifier("symbol_view", "id", context.getPackageName()));//R.id.symbol_view);
        _containerLayout = this.findViewById(context.getApplicationContext().getResources().getIdentifier("container_layout", "id", context.getPackageName()));//R.id.symbol_view);

        setSize(with, hight);

        hostWebSession.onDialogOpen(this);

        _containerLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                _webView.layout(left, top, right,bottom);
            }
        });

        _webView.getSettings().setJavaScriptEnabled(true);
        _webView.getSettings().setDomStorageEnabled(true);
        _webView.getSettings().setSupportZoom(false);
        _webView.getSettings().setBuiltInZoomControls(false);
        _webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        _webView.getSettings().setLoadWithOverviewMode(true);
        _webView.getSettings().setDomStorageEnabled(true);

        _webView.setWebViewClient(new WebPageClient(this));

        _webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        _webView.loadUrl(uri.toString());
    }

    public WebDialog(Context context, Uri uri, boolean enableBackKey, WebSession hostWebSession) {
        super(context, android.R.style.Theme_Dialog);
        _hostWebSession = new WeakReference<WebSession>(hostWebSession);
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(context.getApplicationContext().getResources().getIdentifier("activity_ycweb_dialog", "layout", context.getPackageName()));
        _isEnableBackKey = enableBackKey;
        _webView = (WebView) this.findViewById(context.getApplicationContext().getResources().getIdentifier("web_view", "id", context.getPackageName()));//R.id.web_view);
        _symbolView = (RelativeLayout)this.findViewById(context.getApplicationContext().getResources().getIdentifier("symbol_view", "id", context.getPackageName()));//R.id.symbol_view);
        _containerLayout = (LinearLayout)this.findViewById(context.getApplicationContext().getResources().getIdentifier("container_layout", "id", context.getPackageName()));//R.id.symbol_view);

        setFullScreen();

        hostWebSession.onDialogOpen(this);

        _containerLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                _webView.layout(left, top, right,bottom);
            }
        });

        _webView.getSettings().setJavaScriptEnabled(true);
        _webView.getSettings().setSupportZoom(false);
        _webView.getSettings().setBuiltInZoomControls(false);
        _webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        _webView.getSettings().setLoadWithOverviewMode(true);
        _webView.getSettings().setDomStorageEnabled(true);

        _webView.setWebViewClient(new WebPageClient(this));

        _webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        _webView.loadUrl(uri.toString());

    }

    public void setSize(int width, int height) {

        hightPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, this.getContext().getResources().getDisplayMetrics());
        widthPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, this.getContext().getResources().getDisplayMetrics());

        ViewGroup.LayoutParams layoutParams = _symbolView.getLayoutParams();
        layoutParams.width = widthPixels;
        layoutParams.height = hightPixels;
        _symbolView.setLayoutParams(layoutParams);
        _isFullScreen = false;
    }


    public void setFullScreen() {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        setSize(dm.widthPixels, dm.heightPixels);
        _isFullScreen = true;
    }

    public void close() {
        this.dismiss();
    }

    public WebSession getHostWebSession() {
        return _hostWebSession.get();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean value = super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return _isEnableBackKey;
        }
        return value;
    }


}
