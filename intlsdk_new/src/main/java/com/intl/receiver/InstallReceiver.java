package com.intl.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.intl.init.model.InitModel;
import com.intl.utils.PrefUtils;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class InstallReceiver extends BroadcastReceiver {
    public InstallReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String referrerValue = extras.getString("referrer");
        if (!TextUtils.isEmpty(referrerValue)) {
            String[] mang = referrerValue.split("=");
            if (mang.length > 1) {
                String clientName = referrerValue.split("=")[1];
                if (TextUtils.isEmpty(clientName)) {
                    return;
                }

                InitModel initModel = (InitModel) PrefUtils.getObject(context, "PREF_INIT_MODEL", InitModel.class);
                if (initModel == null) {
                    initModel = new InitModel();
                }

                initModel.setClientName(clientName);
                PrefUtils.putObject(context, "PREF_INIT_MODEL", initModel);
            }
        }

    }
}