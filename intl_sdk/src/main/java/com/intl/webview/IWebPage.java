package com.intl.webview;

import android.content.Context;

import com.intl.webview.WebSession;

/**
 * Created by jerry on 2017/8/19.
 */

interface IWebPage {
    public void setSize(int width, int height);

    public void setFullScreen();

    public void close();

    public Context getContext();

    public WebSession getHostWebSession();
}
