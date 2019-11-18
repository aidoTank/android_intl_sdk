package com.intl;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


/**
 * @Author: yujingliang
 * @Date: 2019/11/2
 */
public class GoogleSDK {
    private static final String TAG = "GoogleSDK" ;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleLoginListener googleLoginListener;
    private static final int RC_SIGN_IN = 9001;
    private Activity activity;
    GoogleSDK(Activity activity){
        this.activity = activity;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(IntlGame.GoogleAppId)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }
    void setGoogleLoginListener(GoogleLoginListener googleLoginListener)
    {
        this.googleLoginListener = googleLoginListener;
    }
    void SignIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    void SignOut()
    {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
    void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }



    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            googleLoginListener.onGoogleLoginSuccess(account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    interface GoogleLoginListener{
        void onGoogleLoginSuccess(String id);
    }
}
