package com.intl.login.presenter;

import android.app.Activity;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import com.intl.R;
import com.intl.base.BaseResponse;
import com.intl.login.model.ResponseLoginBig4;
import com.intl.login.model.UserSdkInfo;
import com.intl.login.presenter.BaseLoginContract.Presenter;
import com.intl.login.presenter.BaseLoginContract.View;
import com.intl.network.RetrofitService;
import com.intl.utils.LocaleManager;
import com.intl.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class BaseLoginPresenter implements Presenter {
    public static final String TAG = BaseLoginPresenter.class.getSimpleName();
    private View baseView;
    private Call<BaseResponse> callGetUserInfo;
    private Call<BaseResponse> callLoginBig4;

    public BaseLoginPresenter() {
    }

    public void attachView(View view) {
        this.baseView = view;
    }

    public void detachView() {
        if (this.callGetUserInfo != null) {
            this.callGetUserInfo.cancel();
        }

        if (this.callLoginBig4 != null) {
            this.callLoginBig4.cancel();
        }

        this.baseView = null;
    }

    public void getUserInfo(String bodyRequest, final String accessToken) {
        this.baseView.showLoading(true);
        LoginService loginApi = (LoginService) RetrofitService.create(LoginService.class);
        String language = LocaleManager.getInstance().getLanguage(this.baseView.getContext());
        this.callGetUserInfo = loginApi.getUserInfo(bodyRequest, language);
        this.callGetUserInfo.enqueue(new Callback<BaseResponse>() {
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse res = (BaseResponse)response.body();
                if (BaseLoginPresenter.this.baseView != null) {
                    BaseLoginPresenter.this.baseView.showLoading(false);
                }

                if (res == null) {
                    BaseLoginPresenter.this.showErrorGeneric();
                } else {
                    UserSdkInfo resUserInfo = (UserSdkInfo)res.decodeResponse(UserSdkInfo.class);
                    if (resUserInfo == null) {
                        BaseLoginPresenter.this.showErrorGeneric();
                    } else {
                        BaseLoginPresenter.this.baseView.updateAccessToken(accessToken);
                        if (resUserInfo.getLoginResult() != null) {
                            resUserInfo.getLoginResult().setAccessToken(accessToken);
                        }

                        BaseLoginPresenter.this.baseView.onResponseGetUserInfo(resUserInfo);
                    }
                }
            }

            public void onFailure(Call<BaseResponse> call, Throwable t) {
                if (BaseLoginPresenter.this.baseView != null) {
                    BaseLoginPresenter.this.baseView.showLoading(false);
                }

                BaseLoginPresenter.this.showErrorGeneric();
            }
        });
    }

    private void showErrorGeneric() {
        if (this.baseView != null) {
            Utils.showToastError(this.baseView.getContext());
            ((Activity)this.baseView.getContext()).finish();
        }

    }

    public void loginFacebook(CallbackManager callbackManager, final String bodyRequest) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            this.requestLoginFb(bodyRequest, accessToken.getToken(), 2);
        } else {
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                public void onSuccess(LoginResult loginResult) {
                    BaseLoginPresenter.this.requestLoginFb(bodyRequest, loginResult.getAccessToken().getToken(), 2);
                }

                public void onCancel() {
                }

                public void onError(FacebookException error) {
                    Utils.showToast(BaseLoginPresenter.this.baseView.getContext(), BaseLoginPresenter.this.baseView.getContext().getString(R.string.intl_error_login_fb));
                }
            });
            LoginManager.getInstance().logInWithReadPermissions((Fragment)this.baseView, Arrays.asList("email", "public_profile", "user_friends"));
        }

    }

    public void loginGoogle(GoogleSignInAccount account, final String bodyRequestGG) {
        if (account != null && !TextUtils.isEmpty(bodyRequestGG)) {
            BaseGoogleLogin.getInstance().getToken(this.baseView.getContext(), account, new BaseGoogleLogin.onBaseGoogleLoginListener() {
                public void onLoadTokenGoogleListener(String tokenGoogle) {
                    if (!TextUtils.isEmpty(tokenGoogle)) {
                        Log.e(BaseLoginPresenter.TAG, "bodyRequestGG: " + bodyRequestGG);
                        BaseLoginPresenter.this.requestLoginFb(bodyRequestGG, tokenGoogle, 3);
                    }

                }
            });
        } else {
            Utils.showToast(this.baseView.getContext(), this.baseView.getContext().getString(R.string.intl_error_login_gg));
        }

    }

    private void requestLoginFb(final String bodyRequest, String tokenFb, final int big4Type) {
        this.baseView.showLoading(true);
        JSONObject object = null;

        try {
            object = new JSONObject(bodyRequest);
            object.put("big4_access_token", tokenFb);
            object.put("big4_type", big4Type);
        } catch (JSONException var8) {
            var8.printStackTrace();
        }

        if (object != null) {
            LoginService loginApi = (LoginService)RetrofitService.create(LoginService.class);
            //String signedResquest = EncryptorEngine.encryptDataNoURLEn(object.toString(), "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCU+1bLfPmcY7qrF/dTbAtuJlv4R/FVc1WEH9HKU0jQjX/n/db9vz/x0i3te/bKLNEcwUhBu+PWPnOt/qVURG9BUT6RsCRFUn0CyGiUKoy45o9K/mJAHmbrNtrUB6ckrYLF75Y50nUNsBVHUDw8yQymmiOBT1gc/KM5s1xTz44LMwIDAQAB");
            String language = LocaleManager.getInstance().getLanguage(this.baseView.getContext());
            this.callLoginBig4 = loginApi.loginBig4("", language);
            this.callLoginBig4.enqueue(new Callback<BaseResponse>() {
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (BaseLoginPresenter.this.baseView != null) {
                        BaseLoginPresenter.this.baseView.showLoading(false);
                    }

                    BaseResponse res = (BaseResponse)response.body();
                    if (res == null) {
                        Utils.showToastError(BaseLoginPresenter.this.baseView.getContext());
                    } else {
                        ResponseLoginBig4 responseLoginBig4 = (ResponseLoginBig4)res.decodeResponse(ResponseLoginBig4.class);
                        if (responseLoginBig4 == null) {
                            Utils.showToastError(BaseLoginPresenter.this.baseView.getContext());
                        } else if (responseLoginBig4.getStatus().equalsIgnoreCase("success")) {
                            JSONObject object = null;

                            try {
                                object = new JSONObject(bodyRequest);
                                object.put("access_token", responseLoginBig4.getData().getAccess_token());
                            } catch (JSONException var7) {
                                var7.printStackTrace();
                            }

                            if (object != null) {
                                BaseLoginPresenter.this.getUserInfo("", responseLoginBig4.getData().getAccess_token());
                            }
                        } else if (responseLoginBig4.getStatus().equalsIgnoreCase("confirm_otp")) {
                            BaseLoginPresenter.this.baseView.gotoConfirmOTP(responseLoginBig4);
                        } else {
                            if (big4Type == 2) {
                                LoginManager.getInstance().logOut();
                            } else if (big4Type == 3) {
                                BaseGoogleLogin.getInstance().logoutGoogle(new BaseGoogleLogin.OnlogoutGoogleListener() {
                                    public void onCompleteLogoutListener() {
                                    }
                                });
                            }

                            Utils.showToast(BaseLoginPresenter.this.baseView.getContext(), responseLoginBig4.getMessage());
                        }
                    }
                }

                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    if (BaseLoginPresenter.this.baseView != null) {
                        BaseLoginPresenter.this.baseView.showLoading(false);
                        Utils.showToastError(BaseLoginPresenter.this.baseView.getContext());
                    }

                }
            });
        }
    }
}
