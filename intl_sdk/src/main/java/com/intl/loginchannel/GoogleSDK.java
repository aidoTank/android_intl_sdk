package com.intl.loginchannel;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;

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
import com.intl.entity.Account;
import com.intl.api.AuthorizeGAPI;
import com.intl.api.GuestBindGAPI;
import com.intl.entity.Session;
import com.intl.usercenter.AccountCache;
import com.intl.utils.IntlGameLoading;
import com.intl.utils.IntlGameUtil;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: yujingliang
 * @Date: 2019/11/2
 */
public class GoogleSDK {
    private static final String TAG = "GoogleSDK" ;
    private static WeakReference<GoogleSignInClient> mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static WeakReference<Activity> activity;
    private static Boolean _isBind = false;
    private static Boolean isSwitch = false;
    private static GoogleSignInOptions gso;
    public static void login(WeakReference<Activity>  _activity,Boolean isBind)
    {
        _isBind = isBind;
        isSwitch = false;
        activity = _activity;
        IntlGameUtil.logd(TAG,"=====>login _isBind:"+_isBind+" isSwitch"+isSwitch);
        if(IntlGame.GoogleClientId == null)
        {
            Toast.makeText(_activity.get(), "googleClientId erre", Toast.LENGTH_SHORT).show();
            return;
        }
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestEmail()
                .requestProfile()
                .requestServerAuthCode(IntlGame.GoogleClientId)
                .build();
        mGoogleSignInClient = new WeakReference<>(GoogleSignIn.getClient(_activity.get(), gso)) ;
        Intent signInIntent = mGoogleSignInClient.get().getSignInIntent();
        _activity.get().startActivityForResult(signInIntent, RC_SIGN_IN);
        IntlGameLoading.getInstance().show(_activity.get());
    }

    public static void SwitchLogin(WeakReference<Activity>  _activity)
    {
        _isBind = false;
        isSwitch = true;
        activity = _activity;
        IntlGameUtil.logd(TAG,"=====>SwitchLogin _isBind:"+_isBind+" isSwitch"+isSwitch);
        if(IntlGame.GoogleClientId == null)
        {
            Toast.makeText(_activity.get(), "googleClientId erre", Toast.LENGTH_SHORT).show();
            return;
        }
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestEmail()
                .requestProfile()
                .requestServerAuthCode(IntlGame.GoogleClientId)
                .build();
        mGoogleSignInClient = new WeakReference<>(GoogleSignIn.getClient(_activity.get(), gso)) ;
        Intent signInIntent = mGoogleSignInClient.get().getSignInIntent();
        _activity.get().startActivityForResult(signInIntent, RC_SIGN_IN);
        IntlGameLoading.getInstance().show(_activity.get());
    }

    public static void logout(Activity activity)
    {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if (acct != null) {
            Log.d("GoogleSDK", "logout");
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                    .requestEmail()
                    .requestProfile()
                    .requestServerAuthCode(IntlGame.GoogleClientId)
                    .build();
            mGoogleSignInClient = new WeakReference<>(GoogleSignIn.getClient(activity, gso)) ;
            mGoogleSignInClient.get().signOut()
                    .addOnCompleteListener( activity, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Log.d(TAG, "Google logout: success");
                                if(IntlGame.iLogoutListener != null)
                                {
                                    IntlGame.iLogoutListener.onComplete(IntlDefine.SUCCESS,null);
                                }
                            }else{
                                Log.d(TAG, "Google logout: failed");
                                if(IntlGame.iLogoutListener != null)
                                {
                                    IntlGame.iLogoutListener.onComplete(IntlDefine.FAILED,task.getException()!=null?task.getException().getMessage():null);
                                }
                            }
                        }
                    });
        }
        else {
            IntlGameUtil.logd("IntlEX","GoogleSDK getLastSignedInAccount == null!");
        }
    }
    public static void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RC_SIGN_IN&&mGoogleSignInClient !=null) {
            IntlGameLoading.getInstance().hide();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }



    private static void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String authCode = account.getServerAuthCode();
            Log.d(TAG, "handleSignInResult: authCode "+authCode+" uid=>"+account.getId());
            // Signed in successfully.
            Session session = new Session("google",authCode,"code");
            if(isSwitch)
            {
                final AuthorizeGAPI accessTokeAPI = new AuthorizeGAPI(session);
                accessTokeAPI.setListener(new AuthorizeGAPI.IgetAccessTokenCallback() {
                    @Override
                    public void AfterGetAccessToken(String channel,JSONObject accountJson,String errorMsg) {
                        if(accountJson != null)
                        {
                            Account userac = new Account(channel,accountJson);
                            boolean first_authorize = userac.getIsFirstAuthorize();
                            if(first_authorize){
                                Map<String, Object> map = new HashMap<>();
                                map.put("user_id", accountJson.optString("openid"));
                                IntlGame.AfEvent(activity.get(), "af_complete_registration", map);
                            }
                            AccountCache.saveAccounts(activity.get(),userac);
                            IntlGame.iSwitchAccountListener.onComplete(IntlDefine.SUCCESS,accountJson.optString("openid"),accountJson.optString("access_token"),null);
                        }else {
                            IntlGame.iSwitchAccountListener.onComplete(IntlDefine.FAILED,null,null,errorMsg);
                        }

                    }
                });
                accessTokeAPI.Excute();
                return;
            }

            if(_isBind)
            {
                GuestBindGAPI guestBindAPI = new GuestBindGAPI(session);
                guestBindAPI.setListener(new GuestBindGAPI.IGuestBindCallback() {
                    @Override
                    public void AfterBind(int resultCode,JSONObject accountJson,String errorMsg) {
                        if(IntlGame.iPersonCenterListener ==null)
                            return;
                        if(resultCode == 0)
                        {
                            IntlGameUtil.logd("GuestBindAPI","Bind success!");
                            AccountCache.saveAccounts(activity.get(),new Account("google",accountJson));
                            IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_SUCCESS,null,errorMsg);
                        }else if(resultCode == 10010){
                            IntlGameUtil.logd("GuestBindAPI","Bind failed!===>该账户已经绑定了游客账号");
                            IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.HAVE_BIND,null,errorMsg);
                        }else {
                            IntlGameUtil.logd("GuestBindAPI","Bind failed!");
                            IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_FAILED,null,errorMsg);
                        }

                    }
                });
                guestBindAPI.Excute();
            }else {
                final AuthorizeGAPI accessTokeAPI = new AuthorizeGAPI(session);
                accessTokeAPI.setListener(new AuthorizeGAPI.IgetAccessTokenCallback() {
                    @Override
                    public void AfterGetAccessToken(String channel,JSONObject accountJson,String errorMsg) {
                        if(accountJson != null)
                        {
                            Account userac = new Account(channel,accountJson);
                            boolean first_authorize = userac.getIsFirstAuthorize();
                            if(first_authorize){
                                Map<String, Object> map = new HashMap<>();
                                map.put("user_id", accountJson.optString("openid"));
                                IntlGame.AfEvent(activity.get(), "af_complete_registration", map);
                            }
                            AccountCache.saveAccounts(activity.get(),userac);
                            IntlGameUtil.logd(TAG,"login Success");
                            IntlGame.iLoginListener.onComplete(IntlDefine.SUCCESS,accountJson.optString("openid"),accountJson.optString("access_token"),null);
                        }else {
                            IntlGame.iLoginListener.onComplete(IntlDefine.FAILED,null,null,errorMsg);
                        }

                    }
                });
                accessTokeAPI.Excute();
            }

        } catch (ApiException e) {
            if (isSwitch)
            {
                if (e.getStatusCode() == 12501) {
                    IntlGame.iSwitchAccountListener.onComplete(IntlDefine.CANCEL,null, null,null);
                } else {
                    IntlGame.iSwitchAccountListener.onComplete(IntlDefine.FAILED, null,null,e.getMessage());
                }
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                return;
            }
            if(_isBind)
            {
                if (e.getStatusCode() == 12501) {
                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_CANCEL,null,null);
                } else {
                    IntlGame.iPersonCenterListener.onComplete("bind",IntlDefine.BIND_FAILED,null,e.getMessage());
                }
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }else {
                if (e.getStatusCode() == 12501) {
                    IntlGame.iLoginListener.onComplete(IntlDefine.CANCEL,null, null,null);
                } else {
                    IntlGame.iLoginListener.onComplete(IntlDefine.FAILED, null,null,e.getMessage());
                }
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

}
