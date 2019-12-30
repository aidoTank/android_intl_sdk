package com.intl.utils;

/**
 * @Author: yujingliang
 * @Date: 2019/12/24
 */
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class IntlGameToast {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static Object synObj = new Object();

    public IntlGameToast() {
    }

    public static void showMessage(Context act, String msg) {
        showMessage(act, msg, 1);
    }

    public static void showMessage(final Context act, final String msg, final int len) {
        handler.post(new Runnable() {
            public void run() {
                synchronized(synObj) {
                    if (toast == null) {
                        toast = Toast.makeText(act, msg, len);
                    } else {
                        toast.setText(msg);
                        toast.setDuration(len);
                    }
                    toast.show();
                }
            }
        });
    }
}