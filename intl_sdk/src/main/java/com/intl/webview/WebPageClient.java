package com.intl.webview;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Set;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */

class WebPageClient extends android.webkit.WebViewClient {

    private WeakReference<IWebPage> _webBase;

    private ProgressDialog _progressDialog;

    WebPageClient(IWebPage ycWebPage) {
        _webBase = new WeakReference<>(ycWebPage);
        _progressDialog = new ProgressDialog(_webBase.get().getContext());
        _progressDialog.setCanceledOnTouchOutside(false);
        _progressDialog.setMessage("Loading...");
    }

    @Deprecated
    public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {

        Uri uri = Uri.parse(url);
        if (uri.getScheme().equals("ycwebcommand") )
        {
            String commandDomain = uri.getHost();
            String command = uri.getLastPathSegment();
            Hashtable<String, String> queryTable = new Hashtable<>();
            Set<String> queryName = uri.getQueryParameterNames();
            for (String key : queryName) {
                String value = uri.getQueryParameter(key);
                if (value != null) {
                    queryTable.put(key, value);
                }
            }
            if (command == null || commandDomain == null)
                return true;

            if (_webBase.get() != null && _webBase.get().getHostWebSession() != null ) {
                WebSession.IWebCommandListener listener = _webBase.get().getHostWebSession().getCommand(commandDomain, command);

                if (listener != null) {
                    WebCommandSender sender = new WebCommandSender(_webBase.get(), view, queryTable.get("identity"));//queryTable 不包含identity 返回Null
                    listener.handleCommand(sender,commandDomain,command, queryTable);
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public void onPageStarted(final android.webkit.WebView view, String url, Bitmap favicon) {
        _progressDialog.show();
        _progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode != KeyEvent.KEYCODE_BACK;
            }
        });
        _progressDialog.setCanceledOnTouchOutside(false);
    }


    @Override
    public void onPageFinished(android.webkit.WebView view, String url) {
        _progressDialog.dismiss();
    }

    @Override
    public void onReceivedError(android.webkit.WebView view, int errorCode,
                                String description, String failingUrl) {
        // 出错
        _progressDialog.dismiss();

        WebCommandSender sender = new WebCommandSender(_webBase.get(), view, null);

        if (_webBase.get() != null && _webBase.get().getHostWebSession() != null ) {
            _webBase.get().getHostWebSession().onWebPageLoadFailed(sender, errorCode, description, failingUrl);
        }
    }
}
