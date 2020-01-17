package com.intl.fcm;

import android.content.Context;

import com.google.firebase.FirebaseApp;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class FCMInit {
    public static void initFirebase(Context context) {
        FirebaseApp.initializeApp(context.getApplicationContext());
    }
}
