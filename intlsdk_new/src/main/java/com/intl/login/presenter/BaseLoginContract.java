package com.intl.login.presenter;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.intl.base.BasePresenter;
import com.intl.base.BaseView;
import com.intl.login.model.ResponseLoginBig4;
import com.intl.login.model.UserSdkInfo;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public interface BaseLoginContract {
    public interface Presenter extends BasePresenter<View> {
        void getUserInfo(String var1, String var2);

        void loginFacebook(CallbackManager var1, String var2);

        void loginGoogle(GoogleSignInAccount var1, String var2);
    }

    public interface View extends BaseView {
        void onResponseGetUserInfo(UserSdkInfo var1);

        void gotoConfirmOTP(ResponseLoginBig4 var1);

        void showLoading(boolean var1);

        void updateAccessToken(String var1);
    }
}
