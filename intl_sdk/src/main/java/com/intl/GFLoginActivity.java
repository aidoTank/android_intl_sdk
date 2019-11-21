package com.intl;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.intl.channel.FaceBookSDK;
import com.intl.channel.GoogleSDK;

/**
 * @Author: yujingliang
 * @Date: 2019/11/11
 */
public class GFLoginActivity {

    public static Dialog rootView;
    static Handler guest_handler;
    @SuppressLint("HandlerLeak")
    public static void LoginCenter(final Context context)
    {
        if (guest_handler == null) {
            guest_handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){

                    }
                }
            };
        }

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (rootView == null) {
            rootView = new Dialog(context, context.getResources().getIdentifier("my_dialog", "style", context.getPackageName()));
        }
        rootView.setContentView(context.getResources().getIdentifier("com_funapps_tw_skinlayout_roots", "layout", context.getPackageName()));
        rootView.setCancelable(false);
        rootView.show();
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = rootView.getWindow().getAttributes();
        lp.width = display.getWidth();
        lp.height = display.getHeight();
        rootView.getWindow().setAttributes(lp);
        TextView base_header_main_tv = rootView.findViewById(context.getResources().getIdentifier("login_main_tv", "id", context.getPackageName()));
        TextView com_funapps_logintext = rootView.findViewById(context.getResources().getIdentifier("com_funapps_logintext", "id", context.getPackageName()));
        RadioGroup radioGroup = rootView.findViewById(context.getResources().getIdentifier("com_funapps_loginradio", "id", context.getPackageName()));
        RadioButton com_funapps_loginfb = rootView.findViewById(context.getResources().getIdentifier("com_funapps_loginfb", "id", context.getPackageName()));
        RadioButton com_funapps_logingp = rootView.findViewById(context.getResources().getIdentifier("com_funapps_logingp", "id", context.getPackageName()));
        com_funapps_loginfb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //facebook login
                Log.d("GFLoginActivity", "onClick: facebook login");
                FaceBookSDK.login((Activity) context);

            }
        });
        com_funapps_logingp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //google login
                Log.d("GFLoginActivity", "onClick: google login");
                GoogleSDK.login((Activity) context);
            }
        });

    }

    public static void dissRootView()
    {
        if(rootView != null)
        {
            rootView.dismiss();
        }
    }
}
