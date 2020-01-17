package com.intl;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.intl.base.BaseActivity;
import com.intl.login.presenter.LoginActivityResult;
import com.intl.login.view.LoginFragment;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class IntlActivity extends BaseActivity {
    private LoginActivityResult loginActivityResult;

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(-2147483648);
            window.clearFlags(67108864);
            window.setStatusBarColor(this.getResources().getColor(R.color.intl_transparent_status));
        }

        this.onCreateActivity();
    }
    private void onCreateActivity(){
        gotoLogin();
    }

    private void gotoLogin() {
        FragmentManager manager = this.getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(LoginFragment.TAG);
        if (fragment == null) {
            Fragment fragment2 = LoginFragment.newInstance();
            fragment2.setRetainInstance(true);
            this.loginActivityResult = (LoginActivityResult)fragment2;
            manager.beginTransaction().add(R.id.intlContainer, fragment2, LoginFragment.TAG).commit();
        }

    }
    protected void clickConnectAgain() {
        super.clickConnectAgain();
        this.onCreateActivity();
    }
    @Override
    protected int getLayoutView() {
        return R.layout.intl_activity;
    }
}
