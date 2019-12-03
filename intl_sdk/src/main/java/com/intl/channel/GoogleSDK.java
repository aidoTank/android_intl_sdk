package com.intl.channel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.intl.entity.IntlDefine;
import com.intl.IntlGame;
import com.intl.usercenter.Account;
import com.intl.usercenter.GetAccessTokeOneAPI;
import com.intl.usercenter.GuestBindAPI;
import com.intl.usercenter.Session;
import com.intl.usercenter.SessionCache;
import com.intl.utils.IntlGameUtil;

import org.json.JSONObject;


/**
 * @Author: yujingliang
 * @Date: 2019/11/2
 */
public class GoogleSDK {
    private static final String TAG = "GoogleSDK" ;
    private static GoogleSignInClient mGoogleSignInClient;
    private static ProgressDialog googlemSpinner;
    private static final int RC_SIGN_IN = 9001;
    private static Activity activity;
    private static Boolean _isBind = false;

    public static void login(Activity _activity,Boolean isBind)
    {
        _isBind = isBind;
        activity = _activity;
        if(IntlGame.GoogleClientId == null)
        {
            Toast.makeText(_activity, "googleClientId erre", Toast.LENGTH_SHORT).show();
            return;
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestEmail()
                .requestProfile()
                .requestServerAuthCode(IntlGame.GoogleClientId)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(_activity, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        _activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public static void logout()
    {
        if (activity == null)
        {
            return;
        }
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if (acct != null) {
            Log.d("GoogleSDK", "logout");
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener( activity, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Log.d(TAG, "Google logout: success");
                            }else{
                                Log.d(TAG, "Google logout: failed");
                            }
                        }
                    });
        }
    }
    public static void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RC_SIGN_IN&&mGoogleSignInClient !=null) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }



    private static void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String authCode = account.getServerAuthCode();
            Log.d(TAG, "handleSignInResult: authCode "+authCode+" uid=>"+account.getId()+" ExpirationTimeSecs=>"+account.getExpirationTimeSecs());
            // Signed in successfully.
            Session session = new Session("google",authCode,"code");

            if(_isBind)
            {
                GuestBindAPI guestBindAPI = new GuestBindAPI(session);
                guestBindAPI.setListener(new GuestBindAPI.IGuestBindCallback() {
                    @Override
                    public void AfterBind(int resultCode,String errorMsg) {
                        if(resultCode == 0)
                        {
                            IntlGameUtil.logd("GuestBindAPI","Bind success!");
                            IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_SUCCESS,errorMsg);
                        }else {
                            IntlGameUtil.logd("GuestBindAPI","Bind failed!");
                            IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_FAILED,errorMsg);
                        }

                    }
                });
                guestBindAPI.Excute();
            }else {
                final GetAccessTokeOneAPI accessTokeAPI = new GetAccessTokeOneAPI(session);
                accessTokeAPI.setListener(new GetAccessTokeOneAPI.IgetAccessTokenCallback() {
                    @Override
                    public void AfterGetAccessToken(String channel,JSONObject accountJson,String errorMsg) {
                        if(accountJson != null)
                        {
                            SessionCache.saveAccounts(activity,new Account(channel,accountJson));

                            IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_SUCCESS,accountJson.optString("openid"),accountJson.optString("access_token"),null);
                        }else {
                            IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED,null,null,errorMsg);
                        }

                    }
                });
                accessTokeAPI.Excute();
            }

        } catch (ApiException e) {
            if(_isBind)
            {
                if (e.getStatusCode() == 12501) {
                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_CANCEL,null);
                } else {
                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_FAILED,e.getMessage());
                }
            }else {
                if (e.getStatusCode() == 12501) {
                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_CANCEL,null, null,null);
                } else {
                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED, null,null,e.getMessage());
                }
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

}
