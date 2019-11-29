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
import com.intl.usercenter.GetAccessTokeAPI;
import com.intl.usercenter.GuestBindAPI;
import com.intl.usercenter.IntlGameCenter;
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
//        googlemSpinner = new ProgressDialog(_activity);
//        googlemSpinner.setMessage("Loading...");
//        googlemSpinner.show();
        activity = _activity;
        if(IntlGame.GoogleClientId == null)
        {
            Toast.makeText(_activity, "googleClientId erre", Toast.LENGTH_SHORT).show();
//            diss();
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
//            diss();
            // Signed in successfully.
            Session session = new Session("google",authCode,"code");

            if(_isBind)
            {
                GuestBindAPI guestBindAPI = new GuestBindAPI(session);
                guestBindAPI.setListener(new GuestBindAPI.IGuestBindCallback() {
                    @Override
                    public void AfterBind(int resultCode) {
                        if(resultCode == 0)
                        {
                            IntlGameUtil.logd("GuestBindAPI","Bind success!");
                            IntlGame.iPersonCenterListener.onComplete(IntlDefine.BIND_SUCCESS);
                        }else {
                            IntlGameUtil.logd("GuestBindAPI","Bind failed!");
                            IntlGame.iPersonCenterListener.onComplete(IntlDefine.BIND_FAILED);
                        }

                    }
                });
                guestBindAPI.Excute();
            }else {
                final GetAccessTokeAPI accessTokeAPI = new GetAccessTokeAPI(session);
                accessTokeAPI.setListener(new GetAccessTokeAPI.IgetAccessTokenCallback() {
                    @Override
                    public void AfterGetAccessToken(String channel,JSONObject accountJson) {
                        if(accountJson != null)
                        {
                            SessionCache.saveAccounts(activity,new Account(channel,accountJson));

                            IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_SUCCESS,accountJson.optString("openid"),accountJson.optString("access_token"));
                        }else {
                            IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED,null,null);
                        }

                    }
                });
                accessTokeAPI.Excute();
            }

        } catch (ApiException e) {
//            diss();
            if(_isBind)
            {
                if (e.getStatusCode() == 12501) {
                    IntlGame.iPersonCenterListener.onComplete(IntlDefine.BIND_CANCEL);
                } else {
                    IntlGame.iPersonCenterListener.onComplete(IntlDefine.BIND_FAILED);
                }
            }else {
                if (e.getStatusCode() == 12501) {
                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_CANCEL,null, null);
                } else {
                    IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED, null,null);
                }
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

//    private static void diss()
//    {
//        googlemSpinner.dismiss();
//    }

}
