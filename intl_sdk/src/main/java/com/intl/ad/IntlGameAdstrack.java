package com.intl.ad;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;

import com.intl.IntlGame;
import com.intl.utils.IntlGameLogUtil;
import com.intl.utils.IntlGameUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: yujingliang
 * @Date: 2019/11/21
 */
public class IntlGameAdstrack {

    private static String TAG = "lemongame_Adstrack";
    public static String bbs;
    public static String customer_url;
    public static String guide_url;
    public static String cafe_cafeId;
    public static String cafe_clientId;
    public static String cafe_clientSecret;
    public static String rockssdkKey;
    public static String rockssenderId;
    public static String cl_appcode;
    public static Boolean cl_isdebug;
    public static String cl_paykey;
    public static String cl_paykey_test;
    public static String kokr_same_user = "한 번 계정을 생성하면 동일한 계정으로 룽투코리아의 모든 게임을 이용하실 수 있습니다.";
    public static int same_user_time = 10000;
    public static String us_same_user = "If you create a Longtu account, you are able to use this account to login all games of Longtu Korea.";
    public static String qq_key;
    public static String qq_secret;
    public static String qq_callbackurl;
    public static String sina_key = null;
    public static String sina_secret = null;
    public static String sina_callbackurl = null;
    public static String qqweibo_key = null;
    public static String qqweibo_secret = null;
    public static String qqweibo_callbackurl = null;
    public static String googleClientId = null;
    public static String googleApiKey = null;
    public static String google_play_key;
    public static String userid;
    public static String Nstore_BASE64_PUBLIC_KEY;
    public static String Tstore_Pay_Environment = "0";
    public static String inmobi_AppId;
    public static String AppId;
    public static String AppSignature;
    public static String devKey;
    public static String goalId = "";
    public static String MobogenieId = "";
    public static String PartyTrackAppKey;
    public static String PartyTrackAppId;
    public static String PartyTrackEventID;
    public static String game_facebook_url = "";
    public static String game_home_url = "";
    public static String AppId_fb;
    public static String APPSecret_fb;
    public static String callbackurl_fb;
    public static String AdId_fb;
    public static String oauthConsumerKey_twitter;
    public static String oauthConsumerSecret_twitter;
    public static String oauthCallback_twitter;
    public static String Avazu_id;
    public static String latest_version;
    public static String down_url;
    public static String limited_reg;
    public static String limited_reg_disc;
    public static String adTrack_profileId = "";
    public static String AppcessId = "";
    public static String GoCpaAppId = "";
    public static String GoCpaAdvertiserId = "";
    public static boolean GoCpaReferral = false;
    public static int LemonRsaORNot = 1;
    public static int LemonLogMode = 1;
    public static String TstoreAppId = "";
    static Bundle bundle = new Bundle();
    static IntlGame.IInitListener callbackListener;

    public IntlGameAdstrack() {
    }

    public static void initGet_adsTrack(Context context, String game_id, String union_id, String action, IntlGame.IInitListener initCallbackListener) {
        callbackListener = initCallbackListener;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        bundle.putString("domain", game_id);
        if ( IntlGame.UUID != null) {
            bundle.putString("udid", IntlGame.UUID);
        } else {
            bundle.putString("udid", IntlGameUtil.getLocalMacAddress(context));
        }

        bundle.putString("SDKVersion", "Android2.0.2");
//        bundle.putString("clientVersion", LemonGame.ClientVersion);
        bundle.putString("union_id", union_id);
        bundle.putString("action", action);
        bundle.putString("udid2", IntlGame.GooggleID);
        IntlGameLogUtil.i(TAG, "domain:" + game_id);
        IntlGameLogUtil.i(TAG, "udid:" + IntlGame.UUID);
        IntlGameLogUtil.i(TAG, "SDKVersion:Android2.0.2");
//        IntlGameLogUtil.i(TAG, "clientVersion:" + LemonGame.ClientVersion);
        IntlGameLogUtil.i(TAG, "union_id:" + union_id);
        IntlGameLogUtil.i(TAG, "action:" + action);
//        IntlGameLogUtil.i(TAG, LemonGame.ADS_CONFIGURATION);
        Thread thread = new IntlGameInitThread(countDownLatch);
        thread.start();
    }

}
