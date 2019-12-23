package com.intl.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.intl.IntlGame;
import com.intl.entity.IntlDefine;
import com.intl.entity.Account;
import com.intl.usercenter.IntlGameCenter;
import com.ycgame.t11.gp.R;
import org.json.JSONException;
import java.util.HashMap;

public class MainActivity extends Activity {
    private TextView textView;
    private TextView debugview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntlGame.init(MainActivity.this,"t11","YGAaFkq3vf753xo3JeZLvX","1061953441680-6l4ts44pco3vj1ao002qe1psf8rrqjam.apps.googleusercontent.com","754170961660851","7453817292517158","EVWHPXxGEOXzbjfWxhUp4yOYgTMSJDNA", new IntlGame.IInitListener() {
            @Override
            public void onComplete(int var1, String var2) {

            }
        });
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.result_View);
        debugview = findViewById(R.id.debug_View);
        Button loginbtn = findViewById(R.id.btn_login);
        Button logoutbtn = findViewById(R.id.btn_logout);
        Button bind = findViewById(R.id.btn_bind);
        Button gameLogin = findViewById(R.id.btn_gamelogin);
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
                PersonCenter(MainActivity.this);
            }
        });
        gameLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameLogin(MainActivity.this);
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void initResultView()
    {
        if(IntlGame.isLogin(MainActivity.this))
        {
            textView.setText("已登录:"+IntlGameCenter.getInstance().loadAccounts(MainActivity.this).getAccessToken());
            return;
        }
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
            public void onComplete(int code,String openid,String token,String msg) {
                if(code == IntlDefine.SUCCESS)
                {
                    UpdateUI("已登录:"+token);
                }else if(code == IntlDefine.CANCEL)
                {
                    UpdateUI("LoginCenterFirst cancel");
                }else {
                    UpdateUI("LoginCenterFirst failed: errorMsg==>"+msg);
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
        UpdateDebugUI("");
        IntlGame.LoginCenterLogout(activity, new IntlGame.ILogoutListener() {
            @Override
            public void onComplete(int code, String errorMsg) {
                if(code == 0)
                {
                    UpdateUI("注销成功！");
                }else{
                    UpdateUI("注销失败！");

                }
            }

        });
    }

    private void PersonCenter(final Activity activity)
    {
        IntlGame.PersonCenter(activity, new IntlGame.IPersonCenterListener() {
            @Override
            public void onComplete(String type, int code, String errorMsg) {
                String msg = null;
                if(type.equals("bind"))
                    msg = (code ==0?"绑定成功！":(code ==1?"绑定失败！":"绑定取消！"));
                if(type.equals("switchroles"))
                    msg = "点击了切换账号！之后游戏需调用Logout和重新Login！";
                UpdateUI(msg);
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
}
