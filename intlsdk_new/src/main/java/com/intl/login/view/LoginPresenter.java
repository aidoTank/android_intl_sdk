package com.intl.login.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.intl.base.BaseResponse;
import com.intl.base.IntlContext;
import com.intl.login.model.ResponseLoginBig4;
import com.intl.login.presenter.LoginContract.Presenter;
import com.intl.login.presenter.LoginContract.View;
import com.intl.utils.LocaleManager;
import com.intl.utils.PrefUtils;
import com.intl.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class LoginPresenter implements Presenter {
    private View baseView;
    private Call<BaseResponse> callInit;

    public LoginPresenter() {
    }

    public void attachView(View view) {
        this.baseView = view;
    }

    public void detachView() {
        if (this.callInit != null) {
            this.callInit.cancel();
        }

        this.baseView = null;
    }

    public void getAppInfo() {
        final ProgressDialog progressDialog = new ProgressDialog(this.baseView.getContext());
        progressDialog.requestWindowFeature(1);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
        progressDialog.show();
        LoginPresenter.this.baseView.onSuccessGetAppInfo();

    }

    private void showDialogError(String message) {
        ResponseLoginBig4.Data data = (ResponseLoginBig4.Data) PrefUtils.getObject("PREF_RESPONSE_INIT_DATA", ResponseLoginBig4.Data.class);
        if (data == null) {

        } else {


            this.baseView.onSuccessGetAppInfo();
        }

    }
}

