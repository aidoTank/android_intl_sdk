package com.intl;

import com.intl.base.IntlCallback;
import com.intl.login.model.IntlLoginResult;
import com.intl.payment.model.IntlPaymentResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class CallbackManager {
    private static final int ID_CALLBACK_LOGIN = 1;
    private static final int ID_CALLBACK_PAYMENT = 2;
    private static final int ID_CALLBACK_LOGOUT = 3;
    private static Map<Integer, IntlCallback> staticCallback = new HashMap();
    private static Map<Integer, LogoutCallback> staticCallback2 = new HashMap();

    public CallbackManager() {
    }

    public static void setLoginCallback(IntlCallback<IntlLoginResult> callback) {
        staticCallback.put(1, callback);
    }

    public static IntlCallback getLoginCallback() {
        return (IntlCallback)staticCallback.get(1);
    }

    public static void setPaymentCallback(IntlCallback<IntlPaymentResult> callback) {
        staticCallback.put(2, callback);
    }

    public static IntlCallback getPaymentCallback() {
        return (IntlCallback)staticCallback.get(2);
    }

    public static void setLogoutCallback(LogoutCallback callback) {
        staticCallback2.put(3, callback);
    }

    public static LogoutCallback getLogoutCallback() {
        return (LogoutCallback)staticCallback2.get(3);
    }

    public static void clearCallback() {
        if (staticCallback != null) {
            staticCallback.clear();
        }

        if (staticCallback2 != null) {
            staticCallback2.clear();
        }

    }
}
