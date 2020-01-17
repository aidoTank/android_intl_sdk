package com.intl.init.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.internal.Validate;
import com.intl.R;
import com.intl.base.IntlContext;
import com.intl.init.model.InitModel;
import com.intl.utils.PrefUtils;
import com.intl.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class InitPresenter {
    public InitPresenter() {
    }

    public static void readParamsFromAssets(Context context) {
        BufferedReader reader = null;
        StringBuilder returnString = new StringBuilder();

        label115: {
            try {
                reader = new BufferedReader(new InputStreamReader(context.getAssets().open("client.txt"), "UTF-8"));

                while(true) {
                    String mLine;
                    if ((mLine = reader.readLine()) == null) {
                        break label115;
                    }

                    returnString.append(mLine);
                }
            } catch (IOException var15) {
                Utils.showToast(IntlContext.getApplicationContext(), IntlContext.getApplicationContext().getString(R.string.intl_error_init));
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var13) {
                    }
                }

            }

            return;
        }

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(returnString.toString());
        } catch (JSONException var14) {
            var14.printStackTrace();
        }

        Validate.notNull(jsonObject, "client.txt");
        InitModel initModel = (InitModel) PrefUtils.getObject(context, "PREF_INIT_MODEL", InitModel.class);
        if (initModel == null) {
            initModel = new InitModel();
        }

        initModel.setAppId(jsonObject.optString("app_id"));
        initModel.setAppIdFacebook(jsonObject.optString("app_id_facebook"));
        initModel.setAppIdAppsflyer(jsonObject.optString("app_id_appsflyer"));
        initModel.setClientCode(jsonObject.optString("client_code"));
        if (TextUtils.isEmpty(initModel.getClientName())) {
            initModel.setClientName(jsonObject.optString("client_name"));
        }

        PrefUtils.putObject("PREF_INIT_MODEL", initModel);
    }
}

