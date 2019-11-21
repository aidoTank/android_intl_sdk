package com.intl.demo;

import android.app.Application;

import com.intl.IntlGame;

/**
 * @Author: yujingliang
 * @Date: 2019/11/20
 */
public class GameApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        IntlGame.Afinit(this);
    }
}
