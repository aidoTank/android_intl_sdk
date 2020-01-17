package com.intl.base;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;

import com.intl.CallbackManager;
import com.intl.login.model.IntlLoginResult;
import com.intl.utils.LocaleManager;
import com.intl.utils.Utils;

import java.util.List;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public abstract class BaseActivity extends FragmentActivity {
    private IntentFilter mIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private Dialog dialogNetwork;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!Utils.isOnline(BaseActivity.this)) {
                BaseActivity.this.showDialogNetworkError();
            }

        }
    };

    public BaseActivity() {
    }

    @LayoutRes
    protected abstract int getLayoutView();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LocaleManager.getInstance().setLocale(this);
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutView());
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        try {
            this.registerReceiver(this.mBroadcastReceiver, this.mIntentFilter);
        } catch (Exception var2) {
        }

        super.onResume();
    }

    protected void onPause() {
        if (this.mBroadcastReceiver != null) {
            try {
                this.unregisterReceiver(this.mBroadcastReceiver);
            } catch (Exception var2) {
            }
        }

        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    public void finish() {
        super.finish();
    }

    protected void showDialogNetworkError() {
        if (this.dialogNetwork == null) {

        } else {
            (new Handler()).postDelayed(new Runnable() {
                public void run() {
                    if (!BaseActivity.this.dialogNetwork.isShowing()) {
                        BaseActivity.this.dialogNetwork.show();
                    }

                }
            }, 500L);
        }

    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void clickConnectAgain() {
    }

    private void callbackLoginCancel() {
        IntlCallback<IntlLoginResult> callback = CallbackManager.getLoginCallback();
        if (callback != null) {
            callback.onCancel();
        }

    }

    protected Fragment getCurrentFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment currentFragment = null;
        if (fragmentManager.getBackStackEntryCount() > 1) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        } else {
            List<Fragment> fragments = this.getSupportFragmentManager().getFragments();
            if (fragments != null && fragments.size() > 0) {
                currentFragment = (Fragment)fragments.get(0);
            }
        }

        return currentFragment;
    }
}
