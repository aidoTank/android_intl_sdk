package com.intl.demo;

import android.app.Application;

import com.intl.IntlGame;
import com.intl.utils.IntlContext;

/**
 * @Author: yujingliang
 * @Date: 2019/11/20
 */
public class GameApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        IntlContext.setApplicationContext(this);
        IntlGame.Afinit(this);
    }
}
