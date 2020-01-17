package com.intl.base;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public interface IntlCallback<RESULT> {
    void onSuccess(RESULT var1);

    void onError();

    void onCancel();
}
