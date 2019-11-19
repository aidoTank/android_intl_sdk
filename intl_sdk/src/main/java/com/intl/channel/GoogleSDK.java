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
import com.intl.IntlDefine;
import com.intl.IntlGame;

import java.lang.ref.WeakReference;


/**
 * @Author: yujingliang
 * @Date: 2019/11/2
 */
public class GoogleSDK {
    private static final String TAG = "GoogleSDK" ;
    private static WeakReference<GoogleSignInClient> mGoogleSignInClient;
    private static ProgressDialog googlemSpinner;
    private static final int RC_SIGN_IN = 9001;
    private static WeakReference<Activity> activity;

    public static void login(Activity _activity)
    {
        googlemSpinner = new ProgressDialog(_activity);
        googlemSpinner.setMessage("Loading...");
        googlemSpinner.show();
        activity = new WeakReference<>(_activity);
        if(IntlGame.GoogleClientId == null)
        {
            Toast.makeText(_activity, "googleClientId erre", Toast.LENGTH_SHORT).show();
            googlemSpinner.dismiss();
            return;
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(IntlGame.GoogleClientId)
                .build();
        mGoogleSignInClient = new WeakReference<>( GoogleSignIn.getClient(_activity, gso));
        Intent signInIntent = mGoogleSignInClient.get().getSignInIntent();
        _activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public static void logout()
    {
        mGoogleSignInClient.get().signOut()
                .addOnCompleteListener( activity.get(), new OnCompleteListener<Void>() {
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
            googlemSpinner.dismiss();
            // Signed in successfully, show authenticated UI.
            IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_SUCCESS,account.getIdToken());
        } catch (ApiException e) {
            googlemSpinner.dismiss();
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
}
