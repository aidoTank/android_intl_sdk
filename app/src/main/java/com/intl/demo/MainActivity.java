package com.intl.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.intl.IntlGame;
import com.intl.entity.IntlDefine;
import com.intl.usercenter.Account;
import com.intl.usercenter.IntlGameCenter;
import com.ycgame.test.R;


import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private TextView textView;
    private TextView debugview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntlGame.init(MainActivity.this,"YGAaFkq3vf753xo3JeZLvX","883426973649-ik34obtorst3iug0p43jpae4q80l9usk.apps.googleusercontent.com","","7453817292517158","EVWHPXxGEOXzbjfWxhUp4yOYgTMSJDNA","http://agg.ycgame.com/index.html", new IntlGame.IInitListener() {
            @Override
            public void onComplete(int var1, String var2) {

            }
        });
        final int level = 0;
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.result_View);
        debugview = findViewById(R.id.debug_View);
        Button loginbtn = findViewById(R.id.btn_login);
        Button logoutbtn = findViewById(R.id.btn_logout);
        Button bind = findViewById(R.id.btn_bind);
        Button gameLogin = findViewById(R.id.btn_gamelogin);
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
                logout(MainActivity.this);
            }
        });
        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bind(MainActivity.this);
            }
        });
        gameLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameLogin(MainActivity.this);
            }
        });


        //AppsFlyers 上报

//        Button reg = findViewById(R.id.btn_registered);
//        Button logined = findViewById(R.id.btn_logined);
//        Button createRole = findViewById(R.id.btn_createRole);
//        Button levelUp = findViewById(R.id.btn_levelup);
//        Button payed = findViewById(R.id.btn_payed);
//        Button churu = findViewById(R.id.customize_churu);
//        Button jiaru = findViewById(R.id.customize_jiaru);
//        reg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                registeredEvent();
//            }
//        });
//        logined.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginEvent();
//            }
//        });
//        createRole.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createRoleEvent();
//            }
//        });
//        payed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                payedEvent();
//            }
//        });
//        levelUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                levelupEvent(level);
//            }
//        });
//        jiaru.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout(MainActivity.this);
//            }
//        });


    }

    @SuppressLint("SetTextI18n")
    private void initResultView()
    {
        textView.setText("未登录");
    }
    @SuppressLint("SetTextI18n")
    private void UpdateUI(final String msg){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(msg);
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void UpdateDebugUI(final String msg){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                debugview.setText(msg);
            }
        });
    }
    HashMap<String ,String> map = new HashMap<>();
    private void login()
    {
        IntlGame.LoginCenter(MainActivity.this,new IntlGame.ILoginCenterListener(){

            @Override
            public void onComplete(int code,String openid,String token) {
                if(code == IntlDefine.LOGIN_SUCCESS)
                {
                    UpdateUI(token);
                }else if(code == IntlDefine.LOGIN_CANCEL)
                {
                    UpdateUI("LoginCenter cancel");
                }else {
                    UpdateUI("LoginCenter failed: errorMsg==>"+token);
                }

            }
        });
    }

    public void GameLogin(Activity activity)
    {
        Account account= IntlGameCenter.getInstance().loadAccounts(activity);
        if(account != null) {
            try {
                UpdateDebugUI(account.getJSONObj().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GameLoginAPI gameLoginAPI = new GameLoginAPI(account);
            gameLoginAPI.setListener(new GameLoginAPI.ILoginCallback() {
                @Override
                public void AfterLogin(String result) {
                    UpdateUI(result);
                }
            });
            gameLoginAPI.Excute();
        }
    }

    private void logout(Activity activity)
    {
        IntlGame.LogOut(activity );
        initResultView();
    }

    private void bind(final Activity activity)
    {
        IntlGame.PersonCenter(activity, new IntlGame.IPersonCenterListener() {
            @Override
            public void onComplete(int code) {
                if(code == IntlDefine.BIND_SUCCESS)
                {
                    UpdateUI("绑定成功！");
                }else if(code ==IntlDefine.BIND_FAILED ) {
                    UpdateUI("绑定失败！");
                }else {
                    UpdateUI("绑定取消！");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        IntlGame.IntlonActivityResults(requestCode,resultCode,data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntlGame.IntlonResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        IntlGame.IntlonPause();
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
//    private void levelupEvent(int level)
//    {
//        level++;
//        UpdateLevel(level);
//        Map<String,Object> map = new HashMap<>();
//        map.put("level",level);
//        IntlGame.AfEvent(this,"levelUp",map);
//    }
//    private void payedEvent()
//    {
//        Map<String,Object> map = new HashMap<>();
//        map.put("pay","");
//        IntlGame.AfEvent(this,"pay",map);
//    }

//    @SuppressLint("SetTextI18n")
//    private void UpdateLevel(int level)
//    {
//        TextView textView=findViewById(R.id.level_view);
//        textView.setText("当前等级："+level);
//    }
}
