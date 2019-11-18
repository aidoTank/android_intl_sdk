package com.intl;

import android.content.Context;

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
