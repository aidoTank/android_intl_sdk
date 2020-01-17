package com.intl.login.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.intl.R;
import com.intl.base.IntlContext;
import com.intl.login.model.IntlLoginResult;
import com.intl.login.model.ResponseLoginBig4;
import com.intl.login.presenter.BaseGoogleLogin;
import com.intl.login.presenter.BaseLoginPresenter;
import com.intl.login.presenter.BaseLoginContract.View;
import com.intl.base.BaseWebViewFragment;

import org.json.JSONException;
import org.json.JSONObject;
import com.intl.login.presenter.BaseLoginContract.Presenter;
import com.intl.utils.IntlGameLoading;
import com.intl.utils.LocaleManager;
import com.intl.utils.PrefUtils;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public abstract class BaseLoginFragment extends BaseWebViewFragment implements View {
    public static final String TAG = BaseLoginFragment.class.getSimpleName();
    private Presenter presenter;
    private CallbackManager callbackManager;
    private String bodyRequestGG;
    private String accessToken;

    public BaseLoginFragment() {
    }

    protected int getLayoutRes() {
        return R.layout.intl_fragment_login;
    }

    protected void onActivityCreated() {
        this.callbackManager = CallbackManager.Factory.create();
        this.presenter = new BaseLoginPresenter();
        this.presenter.attachView(this);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.webView.setBackgroundColor(0);
    }

    public void onDestroy() {
        if (this.presenter != null) {
            this.presenter.detachView();
        }

        if (this.webView != null) {
            this.webView.stopLoading();
        }

        super.onDestroy();
    }

    public void onBackPressed() {
        this.webView.loadUrl("javascript:onclick_back()");
    }

    @Nullable
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.onChangeConfig(this.getResources().getConfiguration());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.onChangeConfig(newConfig);
    }

    private void onChangeConfig(Configuration newConfig) {
        Activity activity = this.getActivity();
        if (activity != null) {
            if (newConfig.orientation == 1) {
                activity.getWindow().setSoftInputMode(32);
            } else if (newConfig.orientation == 2) {
                activity.getWindow().setSoftInputMode(16);
            }

        }
    }

    public void showLoading(boolean isShow) {
        if (isShow) {
            this.progressLoading.setVisibility(android.view.View.VISIBLE);
        } else {
            this.progressLoading.setVisibility(android.view.View.GONE);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void getUserInfo() {
        this.getUserInfo(this.accessToken);
    }

    protected void getUserInfo(String mAccessToken) {
        String plantextRequest = this.createObjectRequestLogin(mAccessToken).toString();
        if (!TextUtils.isEmpty(mAccessToken)) {
            PrefUtils.putString("PREF_REQUEST_INFO", plantextRequest);
        }

        this.presenter.getUserInfo("", mAccessToken);
    }

    protected JSONObject createObjectRequestLogin(String accessToken) {
        JSONObject jsonObject = this.createObjectRequest();

        try {
            jsonObject.put("access_token", accessToken);
        } catch (JSONException var4) {
            var4.printStackTrace();
        }

        return jsonObject;
    }

    protected boolean onShouldOverrideUrlLoading(String url) {
        if (url.contains("access_token")) {
            Uri uri = Uri.parse(url);
            this.updateAccessToken(uri.getQueryParameter("access_token"));
            this.getUserInfo();
            return true;
        } else {
            return false;
        }
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    protected void onReceivedError(int errorCode, String description, final String failingUrl) {
//        Dialog dialog = SohaDialog.showDialog(this.getActivity(), this.getString(string.soha_error_generic), this.getString(string.soha_cancel), this.getString(string.soha_try_again), new SohaOnClickListener() {
//            public void onClick() {
//                BaseLoginFragment.this.finishActivity();
//            }
//        }, new SohaOnClickListener() {
//            public void onClick() {
//                BaseLoginFragment.this.webView.loadUrl(failingUrl);
//            }
//        });
//        dialog.setCancelable(false);
    }

    private void finishActivity() {
        Activity activity = this.getActivity();
        if (activity != null) {
            activity.moveTaskToBack(true);
            activity.finish();
        }

    }

    protected void onPageStarted(String url) {
    }

    protected void onPageFinished(String url) {
    }

    protected void onJavaScriptInteract(String method, String value) {
        if (method.equalsIgnoreCase("LoginFB")) {
            this.loginFb(true);
        } else if (method.equalsIgnoreCase("ConnectLoginFB")) {
            this.loginFb(false);
        } else if (method.equalsIgnoreCase("close_popup")) {
            this.onClose();
        } else if (method.equals("onclick_back")) {
            if (value.equalsIgnoreCase("0")) {
                if (this instanceof LoginFragment) {
//                    SohaDialog.showDialog(this.getActivity(), this.getString(string.soha_dialog_exit_game), this.getString(string.soha_ok), this.getString(string.soha_cancel), new SohaOnClickListener() {
//                        public void onClick() {
//                            BaseLoginFragment.this.finishActivity();
//                            SohaCallback callback = com.soha.sdk.CallbackManager.getLoginCallback();
//                            if (callback != null) {
//                                callback.onCancel();
//                            }
//
//                        }
//                    }, new SohaOnClickListener() {
//                        public void onClick() {
//                        }
//                    });
                } else {
                    this.finishActivity();
                }
            }
        } else if (method.equalsIgnoreCase("LoginGG")) {
            this.loginGG(true);
        } else if (method.equalsIgnoreCase("ConnectLoginGG")) {
            this.loginGG(false);
        }

    }

    private void loginGG(boolean isLogin) {
        PrefUtils.putBoolean("PREF_LOGIN_LOGOUT", isLogin);
        if (!isLogin) {
            BaseGoogleLogin.getInstance().logoutGoogle(new BaseGoogleLogin.OnlogoutGoogleListener() {
                public void onCompleteLogoutListener() {
                }
            });
        }

        BaseGoogleLogin.getInstance().loginGoogle(this.getActivity());
        Log.e(TAG, "bodyRequestGG: " + this.bodyRequestGG);
    }

    private void loginFb(boolean isLogin) {
        IntlLoginResult loginResult = (IntlLoginResult)PrefUtils.getObject("PREF_LOGIN_RESULT", IntlLoginResult.class);
        String accessToken = "";
        if (loginResult != null) {
            accessToken = loginResult.getAccessToken();
        }

        JSONObject jsonObject = this.createObjectRequestLogin(accessToken);
        if (!isLogin) {
            LoginManager.getInstance().logOut();

            try {
                jsonObject.put("connect_account", 1);
            } catch (JSONException var6) {
                var6.printStackTrace();
            }
        }

        String bodyRequest = jsonObject.toString();
        this.presenter.loginFacebook(this.callbackManager, bodyRequest);
    }

    void onResultGoogle(GoogleSignInAccount account) {
        Log.e(TAG, "bodyRequestGGIV: " + this.bodyRequestGG);
        boolean isLogut = PrefUtils.getBoolean("PREF_LOGIN_LOGOUT", false);
        IntlLoginResult loginResult = (IntlLoginResult)PrefUtils.getObject("PREF_LOGIN_RESULT", IntlLoginResult.class);
        String accessToken = "";
        if (loginResult != null) {
            accessToken = loginResult.getAccessToken();
        }

        JSONObject jsonObject = this.createObjectRequestLogin(accessToken);
        if (!isLogut) {
            try {
                jsonObject.put("connect_account", 1);
            } catch (JSONException var7) {
                var7.printStackTrace();
            }
        }

        this.bodyRequestGG = jsonObject.toString();
        Log.e(TAG, "bodyRequestGG2: " + this.bodyRequestGG);
        this.presenter.loginGoogle(account, this.bodyRequestGG);
    }

    private void onClose() {
        if (this.getActivity() != null) {
            this.finishActivity();
        }
    }

    public void gotoConfirmOTP(ResponseLoginBig4 responseLoginBig4) {
        String language = LocaleManager.getInstance().getLanguage(this.getActivity());
        this.webView.loadUrl("https://soap.soha.vn/dialog/webview/login210?" + this.createParamsLoginOTP(responseLoginBig4) + "&lang=" + language);
    }

    private String createParamsLoginOTP(ResponseLoginBig4 responseLoginBig4) {
        JSONObject objectRequest = this.createObjectRequestLogin("");

        try {
            objectRequest.put("confirm_otp", "1");
            objectRequest.put("otp_token", responseLoginBig4.getOtp_token());
            objectRequest.put("message", responseLoginBig4.getMessage());
            objectRequest.put("syntax", responseLoginBig4.getSyntax());
            objectRequest.put("phone_number", responseLoginBig4.getPhone_number());
        } catch (JSONException var4) {
            var4.printStackTrace();
        }

        return "signed_request=";
    }
}
