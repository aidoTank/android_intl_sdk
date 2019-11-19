package com.intl.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.intl.IntlGame;
import com.intl.UserCenter;

public class MainActivity extends Activity {
    private static TextView textView;
    private static String str = "";
    @SuppressLint("HandlerLeak")
    static Handler mTimeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                textView.setText(str);
                sendEmptyMessageDelayed(0, 1000);
            }
        }
    };
    @SuppressLint("SetTextI18n")
    public static void Print(String _str)
    {
        if(textView != null)
            str = str +"\n"+_str;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntlGame.init(MainActivity.this,null,"883426973649-jqul09g64m0too4adsbscat6i2b6lptf.apps.googleusercontent.com","http://10.0.2.2:8080/app/logincenter.html", new IntlGame.IInitListener() {
            @Override
            public void onComplete(int var1, String var2) {

            }
        });

        //setContentView(R.layout.activity_main);
        LinearLayout outer = new LinearLayout(this);
        outer.setOrientation(LinearLayout.VERTICAL);
        ScrollView scrollView = new ScrollView(this);
        LinearLayout.LayoutParams lpScrollView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(lpScrollView);
        textView = new TextView(this);
        LinearLayout.LayoutParams lpTextView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(lpTextView);
        Button loginBtn = new Button(this);
        loginBtn.setText("打开登录界面");
        outer.addView(loginBtn);
        scrollView.addView(textView);
        setContentView(outer);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    private void login()
    {
        IntlGame.OpenLoginCenter(MainActivity.this,new IntlGame.ILoginListener(){

            @Override
            public void onComplete(int code,String token) {
                Log.d("Google", "onComplete: login success===>"+token);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        IntlGame.onActivityResults(requestCode,resultCode,data);
    }
}
