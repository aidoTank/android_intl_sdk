package com.intl.login.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class BaseGoogleLogin {
    public static final String TAG = BaseGoogleLogin.class.getSimpleName();
    private static BaseGoogleLogin baseGoogleLogin;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String SCOPES = "oauth2:profile";

    public BaseGoogleLogin() {
    }

    public static BaseGoogleLogin getInstance() {
        if (baseGoogleLogin == null) {
            baseGoogleLogin = new BaseGoogleLogin();
        }

        return baseGoogleLogin;
    }

    public void setupGoogle(Context context) {
        GoogleSignInOptions gso = (new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)).requestEmail().build();
        this.mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void loginGoogle(Activity context) {
        if (this.mGoogleSignInClient == null) {
            this.setupGoogle(context);
        }

        Intent signInIntent = this.mGoogleSignInClient.getSignInIntent();
        context.startActivityForResult(signInIntent, 2019);
    }

    public void logoutGoogle(final BaseGoogleLogin.OnlogoutGoogleListener googleListener) {
        try {
            if (this.mGoogleSignInClient != null) {
                this.mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e(BaseGoogleLogin.TAG, "logoutGoogle");
                        if (googleListener != null) {
                            googleListener.onCompleteLogoutListener();
                        }

                    }
                });
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    @SuppressLint({"StaticFieldLeak"})
    public void getToken(final Context context, final GoogleSignInAccount signInAccount, final BaseGoogleLogin.onBaseGoogleLoginListener googleLoginListener) {
        try {
            (new AsyncTask<Void, Void, String>() {
                protected String doInBackground(Void... voids) {
                    try {
                        return GoogleAuthUtil.getToken(context, signInAccount.getEmail(), "oauth2:profile");
                    } catch (IOException var3) {
                        var3.printStackTrace();
                    } catch (GoogleAuthException var4) {
                        var4.printStackTrace();
                    }

                    return null;
                }

                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (googleLoginListener != null) {
                        googleLoginListener.onLoadTokenGoogleListener(s);
                    }

                }
            }).execute(new Void[0]);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public interface onBaseGoogleLoginListener {
        void onLoadTokenGoogleListener(String var1);
    }

    public interface OnlogoutGoogleListener {
        void onCompleteLogoutListener();
    }
}
