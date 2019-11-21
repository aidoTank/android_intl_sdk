package com.intl.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.intl.IntlGame;
import com.ycgame.test.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntlGame.init(MainActivity.this,"YGAaFkq3vf753xo3JeZLvX","883426973649-jqul09g64m0too4adsbscat6i2b6lptf.apps.googleusercontent.com",null, new IntlGame.IInitListener() {
            @Override
            public void onComplete(int var1, String var2) {

            }
        });
final int level = 0;
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.result_View);
        Button loginbtn = findViewById(R.id.btn_login);
        Button logoutbtn = findViewById(R.id.btn_logout);
//        LinearLayout outer = new LinearLayout(this);
//        outer.setOrientation(LinearLayout.VERTICAL);
//        ScrollView scrollView = new ScrollView(this);
//        LinearLayout.LayoutParams lpScrollView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//        scrollView.setLayoutParams(lpScrollView);
//        textView = new TextView(this);
//        LinearLayout.LayoutParams lpTextView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//        textView.setLayoutParams(lpTextView);
//        Button loginBtn = new Button(this);
//        loginBtn.setText("打开登录界面");
//        outer.addView(loginBtn);
//        scrollView.addView(textView);
//        setContentView(outer);
        initResultView();
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


        //AppsFlyers 上报

        Button reg = findViewById(R.id.btn_registered);
        Button logined = findViewById(R.id.btn_logined);
        Button createRole = findViewById(R.id.btn_createRole);
        Button levelUp = findViewById(R.id.btn_levelup);
        Button payed = findViewById(R.id.btn_payed);
        Button churu = findViewById(R.id.customize_churu);
        Button jiaru = findViewById(R.id.customize_jiaru);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registeredEvent();
            }
        });
        logined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEvent();
            }
        });
        createRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoleEvent();
            }
        });
        payed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payedEvent();
            }
        });
        levelUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelupEvent(level);
            }
        });
        jiaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void initResultView()
    {
        textView.setText("Token : null");
    }
    @SuppressLint("SetTextI18n")
    private void UpdateUI(String channel_token){
        textView.setText("Token: "+channel_token);

    }
    private void login()
    {
        IntlGame.Login(MainActivity.this,new IntlGame.ILoginListener(){

            @Override
            public void onComplete(int code,String token) {
                Log.d("Google", "onComplete: login success===>"+token);
                UpdateUI(token);
            }
        });
    }
    private void logout()
    {
        IntlGame.LogOut();
        initResultView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        IntlGame.onActivityResults(requestCode,resultCode,data);
    }

    private void registeredEvent()
    {
        Map<String,Object> map = new HashMap<>();
        map.put("registered","");
        IntlGame.AfEvent(this,"registered",map);
    }
    private void loginEvent()
    {
        Map<String,Object> map = new HashMap<>();
        map.put("login","");
        IntlGame.AfEvent(this,"login",map);
    }
    private void createRoleEvent()
    {
        Map<String,Object> map = new HashMap<>();
        map.put("createRole","");
        IntlGame.AfEvent(this,"createRole",map);
    }
    private void levelupEvent(int level)
    {
        level++;
        UpdateLevel(level);
        Map<String,Object> map = new HashMap<>();
        map.put("level",level);
        IntlGame.AfEvent(this,"levelUp",map);
    }
    private void payedEvent()
    {
        Map<String,Object> map = new HashMap<>();
        map.put("pay","");
        IntlGame.AfEvent(this,"pay",map);
    }

    @SuppressLint("SetTextI18n")
    private void UpdateLevel(int level)
    {
        TextView textView=findViewById(R.id.level_view);
        textView.setText("当前等级："+level);
    }
}
