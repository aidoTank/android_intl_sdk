package com.intl.login.presenter;

import com.intl.base.BasePresenter;
import com.intl.base.BaseView;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public interface LoginContract {
    public interface Presenter extends BasePresenter<View> {
        void getAppInfo();
    }

    public interface View extends BaseView {
        void onSuccessGetAppInfo();
    }
}