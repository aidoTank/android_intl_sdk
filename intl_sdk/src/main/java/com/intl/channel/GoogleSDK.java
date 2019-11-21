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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.intl.GFLoginActivity;
import com.intl.entity.IntlDefine;
import com.intl.IntlGame;


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

    public static void login(Activity _activity)
    {
        googlemSpinner = new ProgressDialog(_activity);
        googlemSpinner.setMessage("Loading...");
        googlemSpinner.show();
        activity = _activity;
        if(IntlGame.GoogleClientId == null)
        {
            Toast.makeText(_activity, "googleClientId erre", Toast.LENGTH_SHORT).show();
            diss();
            return;
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(IntlGame.GoogleClientId)
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
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            diss();
            // Signed in successfully, show authenticated UI.
            IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_SUCCESS,account.getIdToken());
        } catch (ApiException e) {
            diss();
            if(e.getStatusCode() == 12501){
                IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_CANCEL,e.getMessage());
            }else{
                IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_FAILED,e.getMessage());
            }
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private static void diss()
    {
        googlemSpinner.dismiss();
        GFLoginActivity.dissRootView();
    }

}
