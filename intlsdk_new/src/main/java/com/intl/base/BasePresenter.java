package com.intl.base;

/**
 * @Author: yujingliang
 * @Date: 2020/1/13
 */
public interface BasePresenter<V> {
    void attachView(V var1);

    void detachView();
}
