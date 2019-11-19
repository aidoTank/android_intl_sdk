package com.intl.webview;

import android.content.Context;

import com.intl.webview.WebSession;

/**
 * Created by jerry on 2017/8/19.
 */

interface IWebPage {
    void setSize(int width, int height);

    void setFullScreen();

    void close();

    void sendEvent(String event, String evntData);

    Context getContext();

    WebSession getHostWebSession();
}
