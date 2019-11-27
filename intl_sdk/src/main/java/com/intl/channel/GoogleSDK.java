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
import com.intl.usercenter.GetAccessTokeAPI;
import com.intl.usercenter.Session;
import com.intl.utils.IntlGameExceptionUtil;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


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
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String authCode = account.getServerAuthCode();
            Log.d(TAG, "handleSignInResult: authCode "+authCode+" uid=>"+account.getId()+" ExpirationTimeSecs=>"+account.getExpirationTimeSecs());
            diss();
            // Signed in successfully, show authenticated UI.
            IntlGame.iLoginListener.onComplete(IntlDefine.LOGIN_SUCCESS,authCode);
            Session session = new Session("google","7453817292517158",authCode,"code");
            GetAccessTokeAPI accessTokeAPI = new GetAccessTokeAPI(session);
            accessTokeAPI.asyncExcute();
//
//            final JSONObject jsonObject = new JSONObject();
//            jsonObject.put("request_type","code");
//            jsonObject.put("code",authCode);
//            Log.d(TAG, "postparme: "+jsonObject.toString());
//            final HttpClient httpClient = new DefaultHttpClient();
//            HttpParams httpParams = httpClient.getParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
//            HttpConnectionParams.setSoTimeout(httpParams, 20000);
//            HttpClientParams.setRedirecting(httpParams, false);
//            final Thread thread=new Thread(){
//                public void run() {
//                    HttpPost post = new HttpPost("http://agg.ycgame.com/api/auth/authorize/google?client_id=7453817292517158");
//                    post.setHeader("Content-Type", "application/json");
//                    post.setHeader("Charset", "UTF-8");
//                    StringEntity entity= null;
//                    try {
//                        entity = new StringEntity(jsonObject.toString(), HTTP.UTF_8);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    entity.setContentType("application/json");
//                    post.setEntity(entity);
//                    try {
//                        HttpResponse httpResponse = httpClient.execute(post);
//                        int statusCode = httpResponse.getStatusLine().getStatusCode();
//                        if (statusCode == HttpStatus.SC_OK)
//                        {
//                            String sdkresult = EntityUtils.toString(httpResponse.getEntity());
//                            Log.d(TAG, "sdkresult: "+sdkresult);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            };
//            thread.start();


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
    }

}
