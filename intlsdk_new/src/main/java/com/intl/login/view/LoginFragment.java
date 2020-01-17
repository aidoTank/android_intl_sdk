package com.intl.login.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.intl.CallbackManager;
import com.intl.R;
import com.intl.base.IntlCallback;
import com.intl.base.IntlContext;
import com.intl.login.model.IntlLoginResult;
import com.intl.login.model.UserSdkInfo;
import com.intl.login.presenter.LoginActivityResult;
import com.intl.login.presenter.LoginContract.View;
import com.intl.login.presenter.LoginContract.Presenter;
import com.intl.utils.LocaleManager;
import com.intl.utils.PrefUtils;
import com.intl.utils.Utils;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class LoginFragment extends BaseLoginFragment implements View, LoginActivityResult {
    public static final String TAG = LoginFragment.class.getName();
    private boolean isAutoLogin = false;
    private Presenter presenter;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    protected String getURLRequest() {
        IntlLoginResult loginResult = (IntlLoginResult)PrefUtils.getObject("PREF_LOGIN_RESULT", IntlLoginResult.class);
        if (loginResult != null) {
            this.isAutoLogin = true;
            return null;
        } else {
            String language = LocaleManager.getInstance().getLanguage(this.getActivity());
//            return "https://gather-auth.ycgame.com"+ "/index.html?channeltype=agl"+"&lang=" + language;
            return "http://10.0.2.2:8080/app/logincenter.html";
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (this.isAutoLogin) {
            this.finishActivity();
        }

    }

    private void finishActivity() {
        Activity activity = this.getActivity();
        if (activity != null) {
            activity.finish();
        }

    }

    private String signRequest() {
        return "signed_request=";
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.presenter = new LoginPresenter();
        this.presenter.attachView(this);
        this.presenter.getAppInfo();
    }

    public void onDestroy() {
        if (this.presenter != null) {
            this.presenter.detachView();
        }

        super.onDestroy();
    }

    public void onResponseGetUserInfo(final UserSdkInfo resUserInfo) {
        if (resUserInfo.getStatus().equals("success")) {
            (new Handler()).postDelayed(new Runnable() {
                public void run() {
                }
            }, 200L);
            UserSdkInfo.Update update = resUserInfo.getUpdate();
            if (update != null && update.getStatus().equals("1")) {
                if (update.getForce().equals("0")) {
//                    intlDialog.showDialogUpdate(this.getActivity(), false, update.getLink(), new intlOnClickListener() {
//                        public void onClick() {
//                            LoginFragment.this.handleLoginSuccess(resUserInfo);
//                        }
//                    });
                } else {
//                    intlDialog.showDialogUpdate(this.getActivity(), true, update.getLink(), (intlOnClickListener)null);
                }

            } else {
                this.handleLoginSuccess(resUserInfo);
            }
        } else if (resUserInfo.getStatus().equals("notice")) {
            if (!TextUtils.isEmpty(resUserInfo.getDetailUrl())) {
                this.showPopupNotice(resUserInfo);
            } else {
                this.showDialogNotice(resUserInfo);
            }

        } else {
            PrefUtils.putObject("PREF_LOGIN_RESULT", (Object)null);
            Utils.showToast(this.getActivity(), resUserInfo.getMessage());
            if (this.isAutoLogin) {
                this.finishActivity();
                this.callbackLoginError();
            }

        }
    }

    private void showPopupNotice(UserSdkInfo resUserInfo) {
        this.webView.setVisibility(android.view.View.GONE);
        String request = PrefUtils.getString("PREF_REQUEST_INFO");
        if (!TextUtils.isEmpty(request)) {
//            DashBoardPopupWeb.getInstance().setWeb(this.getActivity(), resUserInfo, request).setFunctionPopupWebListener(new OnFunctionPopupWebListener() {
//                public void onOpenFunctionWebListener(GifCodeOpenObecjt gifCodeOpenObecjt) {
//                    String value = gifCodeOpenObecjt.getUrl();
//                    if (LoginFragment.this.getActivity() != null) {
//                        if (Utils.isValid(value)) {
//                            if (value.contains("https://m.me")) {
//                                if (Utils.isAppInstalled("com.facebook.orca", LoginFragment.this.getActivity())) {
//                                    Utils.openWeb(LoginFragment.this.getActivity(), value);
//                                } else {
//                                    Utils.showToast(LoginFragment.this.getActivity(), LoginFragment.this.getActivity().getString(string.intl_app_not_fb_message));
//                                }
//                            } else if (value.contains(Constants.FB)) {
//                                Utils.getOpenFacebook(LoginFragment.this.getActivity(), value);
//                            } else {
//                                Utils.openWeb(LoginFragment.this.getActivity(), value);
//                            }
//                        } else if (Utils.isAppInstalled(value, LoginFragment.this.getActivity())) {
//                            Utils.openAppsLaunch(LoginFragment.this.getActivity(), value);
//                        } else {
//                            Utils.openAppStore(LoginFragment.this.getActivity(), value);
//                        }
//                    }
//
//                }
//
//                public void onRetryFunctionWebListener() {
//                    LoginFragment.this.getUserInfo();
//                }
//
//                public void onCloseFunctionWebListener() {
//                    intlSDK.getInstance().logoutWeb(LoginFragment.this.getActivity(), new BaseLogoutWebCallback() {
//                        public void onCleanActivity() {
//                            LoginFragment.this.finishActivity();
//                        }
//
//                        public void onShowWebListener() {
//                            LoginFragment.this.webView.setVisibility(0);
//                        }
//                    });
//                }
//            }).showPopup();
        }

    }

    private void showDialogNotice(UserSdkInfo resUserInfo) {
        Context context = LocaleManager.getInstance().setLocale(this.getActivity());
        String messCancel = null;
        String messOk = null;
        if (resUserInfo.getLogout().equals("1")) {
            messCancel = context.getString(R.string.intl_exit);
        }

        if (resUserInfo.getRetry().equals("1")) {
            messOk = context.getString(R.string.intl_try_again);
        }

//        intlDialog.showDialog(this.getActivity(), resUserInfo.getMessage(), messCancel, messOk, new intlOnClickListener() {
//            public void onClick() {
//                LoginFragment.this.finishActivity();
//                intlSDK.getInstance().logoutNoMessageNotCallback();
//                intlCallback<intlLoginResult> callback = CallbackManager.getLoginCallback();
//                if (callback != null) {
//                    callback.onCancel();
//                }
//
//            }
//        }, new intlOnClickListener() {
//            public void onClick() {
//                LoginFragment.this.getUserInfo();
//            }
//        }).setCancelable(false);
    }

    private void handleLoginSuccess(UserSdkInfo resUserInfo) {
        IntlLoginResult loginResult = resUserInfo.getLoginResult();
        if (loginResult.getType_user().equalsIgnoreCase("play_now")) {
            IntlLoginResult loginResultPref = (IntlLoginResult) PrefUtils.getObject("PREF_LOGIN_RESULT", IntlLoginResult.class);
            if (loginResultPref != null && loginResult.getAccessToken() != null) {
                PrefUtils.putObject("PREF_LOGIN_RESULT", loginResult);
                if (loginResult.getNew_user().equals("1")) {
                }

                this.showDialogConnectAccount(loginResult);
                return;
            }

            if (loginResult.getNew_user().equals("1")) {
            }
        }

        PrefUtils.putObject("PREF_LOGIN_RESULT", loginResult);
        if (loginResult.getNew_user().equals("1")) {
        }

        this.callbackLoginSuccess(loginResult, true);
        String oldUserId = PrefUtils.getString("PREF_USER_ID_OLD");
        if (!oldUserId.equals(loginResult.getUserId())) {
            PrefUtils.putString("PREF_USER_ID_OLD", loginResult.getUserId());
            PrefUtils.putBoolean("PREF_IS_SEND_PUSH_NOTIFY_SUCCESS", false);
            this.sendTokenFCM(this.getContext());
        } else {
            boolean isSuccess = PrefUtils.getBoolean("PREF_IS_SEND_PUSH_NOTIFY_SUCCESS", false);
            if (!isSuccess) {
                this.sendTokenFCM(this.getContext());
            }
        }

    }

    private void sendTokenFCM(final Context context) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    String token = ((InstanceIdResult)task.getResult()).getToken();
                    //FCMRequest.sendRegistrationToServer(context, token);
                }
            }
        });
    }

    private void showDialogConnectAccount(final IntlLoginResult loginResult) {
        Context context = LocaleManager.getInstance().setLocale(this.getActivity());
        Resources resources = context.getResources();
//        intlDialog.showDialog(this.getActivity(), resources.getString(string.intl_login_des_connect_account), resources.getString(string.intl_login_giveup), resources.getString(string.intl_login_connect_account), new intlOnClickListener() {
//            public void onClick() {
//                LoginFragment.this.callbackLoginSuccess(loginResult, true);
//            }
//        }, new intlOnClickListener() {
//            public void onClick() {
//                LoginFragment.this.gotoConnectAccountPlayNow();
//                LoginFragment.this.callbackLoginSuccess(loginResult, false);
//            }
//        }).setCancelable(false);
    }

    private void gotoConnectAccountPlayNow() {

    }

    private void callbackLoginSuccess(final IntlLoginResult loginResult, final boolean isFinish) {
        final IntlCallback<IntlLoginResult> callback = CallbackManager.getLoginCallback();
        int time = PrefUtils.getInt("PREF_HIDE_BANNER_intl");
        int timeNow = Integer.parseInt(Utils.getDateSystem());
        if (time != 0 && time == timeNow) {
            this.loginSuccess(callback, loginResult, isFinish);
        } else {
            if (isFinish) {
                this.webView.setVisibility(android.view.View.GONE);
            }
            this.loginSuccess(callback, loginResult, isFinish);

        }

    }

    private void loginSuccess(IntlCallback<IntlLoginResult> callback, IntlLoginResult loginResult, boolean isFinish) {
        if (callback != null) {
            callback.onSuccess(loginResult);
        }

        this.showToastHello(loginResult.getUsername());
        if (isFinish) {
            this.finishActivity();
        }

    }

    private void callbackLoginError() {
        IntlCallback<IntlLoginResult> callback = CallbackManager.getLoginCallback();
        if (callback != null) {
            callback.onError();
        }

    }

    private void showToastHello(String text) {
        if (this.getActivity() != null) {
            LayoutInflater inflater = (LayoutInflater)this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                android.view.View layout = inflater.inflate(R.layout.intl_login_toast_hello, (ViewGroup)null);
                TextView tvHello = (TextView)layout.findViewById(R.id.tvHello);
                Context context = LocaleManager.getInstance().setLocale(this.getActivity());
                Resources resources = context.getResources();
                tvHello.setText(String.format(resources.getString(R.string.intl_login_hello), text));
                Toast toast = new Toast(IntlContext.getApplicationContext());
                toast.setGravity(49, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        }
    }

    public void onResultGoogle(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2019) {
            GoogleSignInAccount account = null;

            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                account = (GoogleSignInAccount)task.getResult(ApiException.class);
                if (account != null) {
                    Log.e(TAG, account.getEmail());
                }
            } catch (ApiException var6) {
                var6.printStackTrace();
            }

            this.onResultGoogle(account);
        }

    }

    public void onSuccessGetAppInfo() {
        if (this.isAutoLogin) {
            IntlLoginResult loginResult = (IntlLoginResult)PrefUtils.getObject("PREF_LOGIN_RESULT", IntlLoginResult.class);
            this.getUserInfo(loginResult.getAccessToken());
        }

    }
}
