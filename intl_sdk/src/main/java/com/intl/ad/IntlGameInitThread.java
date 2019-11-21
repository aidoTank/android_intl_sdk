//package com.intl.ad;
//
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.intl.utils.IntlGameLogUtil;
//
//import org.json.JSONObject;
//
//import java.util.concurrent.CountDownLatch;
//
///**
// * @Author: yujingliang
// * @Date: 2019/11/21
// */
//public class IntlGameInitThread extends Thread{
//    private static String TAG = "lemongame_initThread";
//    private CountDownLatch countDownLatch;
//
//    public IntlGameInitThread(CountDownLatch countDownLatch) {
//        this.countDownLatch = countDownLatch;
//    }
//
//    public void run() {
//        System.out.println(this.getName() + "init线程开始" + "  " + System.currentTimeMillis());
//
//        try {
//            IntlGameLogUtil.i(TAG, IntlGameAdStrack.bundle.toString());
//            String resp = IntlGameLogUtil.openUrl(LemonGame.ADS_CONFIGURATION, "POST", LemonGameAdstrack.bundle, "");
//            IntlGameLogUtil.i(TAG, resp);
//            JSONObject json = new JSONObject(resp);
//            String info_code = json.getString("code");
//            String info = json.getString("info");
//            IntlGameLogUtil.i(TAG, "ad_info_code:" + info_code);
//            IntlGameLogUtil.i(TAG, "ad_info_info:" + info);
//            int code = Integer.parseInt(info_code);
//            if (code == 0) {
//                LemonGameAdstrack.callbackListener.onComplete(0, "success");
//                JSONObject json_obj = new JSONObject(info);
//                JSONObject switch_obj;
//                if (!json_obj.isNull("googleplus")) {
//                    switch_obj = json_obj.getJSONObject("googleplus");
//                    IntlGameLogUtil.i(TAG, "" + switch_obj);
//                    if (switch_obj != null) {
//                        LemonGameAdstrack.googleClientId = switch_obj.getString("googleClientId");
//                        LemonGameAdstrack.googleApiKey = switch_obj.getString("googleApiKey");
//                        IntlGameLogUtil.i(TAG, "googleClientId:" + LemonGameAdstrack.googleClientId);
//                        IntlGameLogUtil.i(TAG, "googleApiKey:" + LemonGameAdstrack.googleApiKey);
//                        if (LemonGame.db.isHaveColumn(LemonGameUnionConfig.segment, LemonGameUnionConfig.googleplus_channel)) {
//                            IntlGameLogUtil.i(TAG, "查询不到googleplus对应的数据，开始插入");
//                            LemonGame.db.insert_account_unionConfig(LemonGameUnionConfig.segment, LemonGameUnionConfig.googleplus_channel, LemonGameAdstrack.googleClientId, LemonGameAdstrack.googleApiKey, "");
//                        } else {
//                            IntlGameLogUtil.i(TAG, "查询到googleplus对应的数据，开始修改");
//                            LemonGame.db.updateData(LemonGameUnionConfig.segment, LemonGameUnionConfig.googleplus_channel, LemonGameAdstrack.googleClientId, LemonGameAdstrack.googleApiKey, "");
//                        }
//                    }
//                }
//
//                if (!json_obj.isNull("googleplay")) {
//                    switch_obj = json_obj.getJSONObject("googleplay");
//                    LemonGameAdstrack.google_play_key = switch_obj.getString("key");
//                    IntlGameLogUtil.i(TAG, "google_play_key:" + LemonGameAdstrack.google_play_key);
//                    if (LemonGame.db.isHaveColumn(LemonGameUnionConfig.segment, LemonGameUnionConfig.googleplay_channel)) {
//                        IntlGameLogUtil.i(TAG, "查询不到googleplay对应的数据，开始插入");
//                        LemonGame.db.insert_account_unionConfig(LemonGameUnionConfig.segment, LemonGameUnionConfig.googleplay_channel, LemonGameAdstrack.google_play_key, "", "");
//                    } else {
//                        IntlGameLogUtil.i(TAG, "查询到googleplay对应的数据，开始修改");
//                        LemonGame.db.updateData(LemonGameUnionConfig.segment, LemonGameUnionConfig.googleplay_channel, LemonGameAdstrack.google_play_key, "", "");
//                    }
//                }
//
//                if (!json_obj.isNull("NaverCafe")) {
//                    switch_obj = json_obj.getJSONObject("NaverCafe");
//                    if (!switch_obj.isNull("cafeId")) {
//                        LemonGameAdstrack.cafe_cafeId = switch_obj.getString("cafeId");
//                    }
//
//                    if (!switch_obj.isNull("clientId")) {
//                        LemonGameAdstrack.cafe_clientId = switch_obj.getString("clientId");
//                    }
//
//                    if (!switch_obj.isNull("clientSecret")) {
//                        LemonGameAdstrack.cafe_clientSecret = switch_obj.getString("clientSecret");
//                    }
//
//                    IntlGameLogUtil.i(TAG, "cafe_cafeId:" + LemonGameAdstrack.cafe_cafeId);
//                    IntlGameLogUtil.i(TAG, "cafe_clientId:" + LemonGameAdstrack.cafe_clientId);
//                    IntlGameLogUtil.i(TAG, "cafe_clientSecret:" + LemonGameAdstrack.cafe_clientSecret);
//                }
//
//                Message msg_version;
//                if (!json_obj.isNull("5rocks")) {
//                    switch_obj = json_obj.getJSONObject("5rocks");
//                    if (!switch_obj.isNull("sdkKey")) {
//                        LemonGameAdstrack.rockssdkKey = switch_obj.getString("sdkKey");
//                    }
//
//                    if (!switch_obj.isNull("senderId")) {
//                        LemonGameAdstrack.rockssenderId = switch_obj.getString("senderId");
//                    }
//
//                    msg_version = new Message();
//                    msg_version.what = 113;
//                    LemonGame.LemonGameHandler.sendMessage(msg_version);
//                    IntlGameLogUtil.i(TAG, "5rockssdkKey:" + LemonGameAdstrack.rockssdkKey);
//                    IntlGameLogUtil.i(TAG, "5rockssenderId:" + LemonGameAdstrack.rockssenderId);
//                }
//
//                if (!json_obj.isNull("CLstore")) {
//                    switch_obj = json_obj.getJSONObject("CLstore");
//                    LemonGameAdstrack.cl_appcode = switch_obj.getString("cl_appcode");
//                    LemonGameAdstrack.cl_paykey = switch_obj.getString("cl_paykey");
//                    LemonGameAdstrack.cl_paykey_test = switch_obj.getString("cl_paykey_test");
//                    String isdebug = switch_obj.getString("cl_isdebug");
//                    if (isdebug.equals("false")) {
//                        LemonGameAdstrack.cl_isdebug = false;
//                    } else {
//                        LemonGameAdstrack.cl_isdebug = true;
//                    }
//
//                    IntlGameLogUtil.i(TAG, "cl_appcode:" + LemonGameAdstrack.cl_appcode);
//                    IntlGameLogUtil.i(TAG, "cl_paykey:" + LemonGameAdstrack.cl_paykey);
//                    IntlGameLogUtil.i(TAG, "cl_paykey_test:" + LemonGameAdstrack.cl_paykey_test);
//                    IntlGameLogUtil.i(TAG, "cl_isdebug:" + LemonGameAdstrack.cl_isdebug.toString());
//                }
//
//                String cache_Tstore_Pay_Environment;
//                LemonGameCache LMcache;
//                if (!json_obj.isNull("Nstore")) {
//                    switch_obj = json_obj.getJSONObject("Nstore");
//                    LemonGameAdstrack.Nstore_BASE64_PUBLIC_KEY = switch_obj.getString("Nstore_BASE64_PUBLIC_KEY");
//                    IntlGameLogUtil.i(TAG, "Nstore_key:" + LemonGameAdstrack.Nstore_BASE64_PUBLIC_KEY);
//                    LMcache = LemonGameCache.get(LemonGame.LM_ctx);
//                    cache_Tstore_Pay_Environment = LMcache.getAsString(LemonGameUnionConfig.Nstore_channel);
//                    if (TextUtils.isEmpty(cache_Tstore_Pay_Environment)) {
//                        IntlGameLogUtil.i(TAG, "此时缓存中没有Nstore的数据");
//                        LMcache.put(LemonGameUnionConfig.Nstore_channel, LemonGameAdstrack.Nstore_BASE64_PUBLIC_KEY);
//                    } else if (cache_Tstore_Pay_Environment != LemonGameAdstrack.Nstore_BASE64_PUBLIC_KEY && !TextUtils.isEmpty(LemonGameAdstrack.Nstore_BASE64_PUBLIC_KEY)) {
//                        IntlGameLogUtil.i(TAG, "此时缓存中有Nstore的数据，但是不一致");
//                        boolean flag = LMcache.remove(LemonGameUnionConfig.Nstore_channel);
//                        if (flag) {
//                            LMcache.put(LemonGameUnionConfig.Nstore_channel, LemonGameAdstrack.Nstore_BASE64_PUBLIC_KEY);
//                        } else if (cache_Tstore_Pay_Environment == LemonGameAdstrack.Nstore_BASE64_PUBLIC_KEY) {
//                            IntlGameLogUtil.i(TAG, "此时缓存中有Nstore的数据，而且一致");
//                        }
//                    }
//                }
//
//                if (!json_obj.isNull("Tstore")) {
//                    switch_obj = json_obj.getJSONObject("Tstore");
//                    LemonGameAdstrack.Tstore_Pay_Environment = switch_obj.getString("Tstore_Pay_Environment");
//                    LemonGameAdstrack.TstoreAppId = switch_obj.getString("TstoreAppId");
//                    IntlGameLogUtil.i(TAG, "Tstore_Pay_Environment:" + LemonGameAdstrack.Tstore_Pay_Environment);
//                    IntlGameLogUtil.i(TAG, "TstoreAppId:" + LemonGameAdstrack.TstoreAppId);
//                    LMcache = LemonGameCache.get(LemonGame.LM_ctx);
//                    cache_Tstore_Pay_Environment = LMcache.getAsString(LemonGameUnionConfig.TstoreEnvironment_channel);
//                    String cache_Tstore_Pay_AppId = LMcache.getAsString(LemonGameUnionConfig.TstoreAppID_Channel);
//                    if (TextUtils.isEmpty(cache_Tstore_Pay_Environment)) {
//                        IntlGameLogUtil.i(TAG, "此时缓存中没有Tstore的Tstore_Pay_Environment数据");
//                        LMcache.put(LemonGameUnionConfig.TstoreEnvironment_channel, LemonGameAdstrack.Tstore_Pay_Environment);
//                    }
//
//                    if (TextUtils.isEmpty(cache_Tstore_Pay_AppId)) {
//                        IntlGameLogUtil.i(TAG, "此时缓存中没有Tstore的TstoreAppId数据");
//                        LMcache.put(LemonGameUnionConfig.TstoreAppID_Channel, LemonGameAdstrack.TstoreAppId);
//                    } else {
//                        boolean flag;
//                        if (cache_Tstore_Pay_Environment != LemonGameAdstrack.Tstore_Pay_Environment && !TextUtils.isEmpty(LemonGameAdstrack.Tstore_Pay_Environment)) {
//                            IntlGameLogUtil.i(TAG, "此时缓存中有Tstore_Pay_Environment的数据，但是不一致");
//                            flag = LMcache.remove(LemonGameUnionConfig.TstoreEnvironment_channel);
//                            if (flag) {
//                                LMcache.put(LemonGameUnionConfig.TstoreEnvironment_channel, LemonGameAdstrack.Tstore_Pay_Environment);
//                            } else if (cache_Tstore_Pay_Environment == LemonGameAdstrack.Tstore_Pay_Environment) {
//                                IntlGameLogUtil.i(TAG, "此时缓存中有Tstore_Pay_Environment的数据，而且一致");
//                            }
//                        } else if (cache_Tstore_Pay_AppId != LemonGameAdstrack.TstoreAppId && !TextUtils.isEmpty(LemonGameAdstrack.TstoreAppId)) {
//                            IntlGameLogUtil.i(TAG, "此时缓存中有TstoreAppId的数据，但是不一致");
//                            flag = LMcache.remove(LemonGameUnionConfig.TstoreAppID_Channel);
//                            if (flag) {
//                                LMcache.put(LemonGameUnionConfig.TstoreAppID_Channel, LemonGameAdstrack.TstoreAppId);
//                            } else if (cache_Tstore_Pay_AppId == LemonGameAdstrack.TstoreAppId) {
//                                IntlGameLogUtil.i(TAG, "此时缓存中有TstoreAppId的数据，而且一致");
//                            }
//                        }
//                    }
//                }
//
//                if (!json_obj.isNull("haslog")) {
//                    switch_obj = json_obj.getJSONObject("haslog");
//                    if (!switch_obj.isNull("flag")) {
//                        LemonGameAdstrack.LemonLogMode = switch_obj.getInt("flag");
//                    }
//
//                    IntlGameLogUtil.i(TAG, "LemonSDKLog:" + LemonGameAdstrack.LemonLogMode);
//                    if (LemonGameAdstrack.LemonLogMode == 0) {
//                        Log.e(TAG, "lemon不打印log");
//                    } else if (LemonGameAdstrack.LemonLogMode == 1) {
//                        Log.e(TAG, "lemon打印log");
//                    }
//                }
//
//                if (!json_obj.isNull("appsFlyer")) {
//                    switch_obj = json_obj.getJSONObject("appsFlyer");
//                    IntlGameLogUtil.i(TAG, "2222");
//                    LemonGameAdstrack.devKey = switch_obj.getString("DevKey");
//                    IntlGameLogUtil.i(TAG, "appsflyer_id:" + LemonGameAdstrack.devKey);
//                    if (LemonGame.db.isHaveColumn(LemonGameUnionConfig.segment, LemonGameUnionConfig.appsFlyer_channel)) {
//                        IntlGameLogUtil.i(TAG, "查询不到appsflyer对应的数据，开始插入");
//                        LemonGame.db.insert_account_unionConfig(LemonGameUnionConfig.segment, LemonGameUnionConfig.appsFlyer_channel, LemonGameAdstrack.devKey, "", "");
//                    } else {
//                        IntlGameLogUtil.i(TAG, "查询到appsflyer对应的数据，开始修改");
//                        LemonGame.db.updateData(LemonGameUnionConfig.segment, LemonGameUnionConfig.appsFlyer_channel, LemonGameAdstrack.devKey, "", "");
//                    }
//
//                    msg_version = new Message();
//                    msg_version.what = 101;
//                    LemonGame.LemonGameHandler.sendMessage(msg_version);
//                }
//
//                if (!json_obj.isNull("chartboost")) {
//                    switch_obj = json_obj.getJSONObject("chartboost");
//                    LemonGameAdstrack.AppId = switch_obj.getString("AppId");
//                    LemonGameAdstrack.AppSignature = switch_obj.getString("AppSignature");
//                    IntlGameLogUtil.i(TAG, "chartboost_appid:" + LemonGameAdstrack.AppId);
//                    IntlGameLogUtil.i(TAG, "chartboost_AppSignature:" + LemonGameAdstrack.AppSignature);
//                    if (LemonGame.db.isHaveColumn(LemonGameUnionConfig.segment, LemonGameUnionConfig.chartboost_channel)) {
//                        IntlGameLogUtil.i(TAG, "查询不到chartboost对应的数据，开始插入");
//                        LemonGame.db.insert_account_unionConfig(LemonGameUnionConfig.segment, LemonGameUnionConfig.chartboost_channel, LemonGameAdstrack.AppId, LemonGameAdstrack.AppSignature, "");
//                    } else {
//                        IntlGameLogUtil.i(TAG, "查询到chartboost对应的数据，开始修改");
//                        LemonGame.db.updateData(LemonGameUnionConfig.segment, LemonGameUnionConfig.chartboost_channel, LemonGameAdstrack.AppId, LemonGameAdstrack.AppSignature, "");
//                    }
//
//                    msg_version = new Message();
//                    msg_version.what = 102;
//                    LemonGame.LemonGameHandler.sendMessage(msg_version);
//                }
//
//                if (!json_obj.isNull("facebook")) {
//                    switch_obj = json_obj.getJSONObject("facebook");
//                    LemonGameAdstrack.AppId_fb = switch_obj.getString("AppId");
//                    LemonGameAdstrack.APPSecret_fb = switch_obj.getString("AppSecret");
//                    if (switch_obj.has("Appcallbackurl")) {
//                        LemonGameAdstrack.callbackurl_fb = switch_obj.getString("Appcallbackurl");
//                    }
//
//                    if (switch_obj.has("ADKey")) {
//                        LemonGameAdstrack.AdId_fb = switch_obj.getString("ADKey");
//                    }
//
//                    IntlGameLogUtil.i(TAG, "facebook_appid:" + LemonGameAdstrack.AppId_fb);
//                    IntlGameLogUtil.i(TAG, "facebook_APPSecret:" + LemonGameAdstrack.APPSecret_fb);
//                    IntlGameLogUtil.i(TAG, "facebook_callbackurl:" + LemonGameAdstrack.callbackurl_fb);
//                    IntlGameLogUtil.i(TAG, "facebook_ADKey:" + LemonGameAdstrack.AdId_fb);
//                    if (LemonGame.db.isHaveColumn(LemonGameUnionConfig.segment, LemonGameUnionConfig.facebook_channel)) {
//                        IntlGameLogUtil.i(TAG, "查询不到facebook对应的数据，开始插入");
//                        LemonGame.db.insert_account_unionConfig(LemonGameUnionConfig.segment, LemonGameUnionConfig.facebook_channel, LemonGameAdstrack.AppId_fb, LemonGameAdstrack.APPSecret_fb, LemonGameAdstrack.callbackurl_fb);
//                    } else {
//                        IntlGameLogUtil.i(TAG, "查询到facebook对应的数据，开始修改");
//                        LemonGame.db.updateData(LemonGameUnionConfig.segment, LemonGameUnionConfig.facebook_channel, LemonGameAdstrack.AppId_fb, LemonGameAdstrack.APPSecret_fb, LemonGameAdstrack.callbackurl_fb);
//                    }
//                }
//
//                if (!json_obj.isNull("inmobi")) {
//                    switch_obj = json_obj.getJSONObject("inmobi");
//                    LemonGameAdstrack.inmobi_AppId = switch_obj.getString("AppId");
//                    IntlGameLogUtil.i(TAG, "inmobi_appId:" + LemonGameAdstrack.inmobi_AppId);
//                    if (LemonGame.db.isHaveColumn(LemonGameUnionConfig.segment, LemonGameUnionConfig.inmobi_channel)) {
//                        IntlGameLogUtil.i(TAG, "查询不到inmobi对应的数据，开始插入");
//                        LemonGame.db.insert_account_unionConfig(LemonGameUnionConfig.segment, LemonGameUnionConfig.inmobi_channel, LemonGameAdstrack.inmobi_AppId, "", "");
//                    } else {
//                        IntlGameLogUtil.i(TAG, "查询到inmobi对应的数据，开始修改");
//                        LemonGame.db.updateData(LemonGameUnionConfig.segment, LemonGameUnionConfig.inmobi_channel, LemonGameAdstrack.inmobi_AppId, "", "");
//                    }
//
//                    msg_version = new Message();
//                    msg_version.what = 104;
//                    LemonGame.LemonGameHandler.sendMessage(msg_version);
//                }
//
//                if (!json_obj.isNull("floatUrl")) {
//                    switch_obj = json_obj.getJSONObject("floatUrl");
//                    LemonGameAdstrack.game_facebook_url = switch_obj.getString("floatBookUrl");
//                    LemonGameAdstrack.game_home_url = switch_obj.getString("floatHomeUrl");
//                    IntlGameLogUtil.i(TAG, "game_facebook_url:" + LemonGameAdstrack.game_facebook_url);
//                    IntlGameLogUtil.i(TAG, "game_Home_url:" + LemonGameAdstrack.game_home_url);
//                }
//
//                if (!json_obj.isNull("lm_version")) {
//                    switch_obj = json_obj.getJSONObject("lm_version");
//                    LemonGameAdstrack.latest_version = switch_obj.getString("latest_version");
//                    LemonGameAdstrack.down_url = switch_obj.getString("down_url");
//                    IntlGameLogUtil.i(TAG, "latest_version:" + LemonGameAdstrack.latest_version);
//                    IntlGameLogUtil.i(TAG, "down_url:" + LemonGameAdstrack.down_url);
//                    msg_version = new Message();
//                    msg_version.what = 6;
//                    LemonGame.LemonGameHandler.sendMessageAtFrontOfQueue(msg_version);
//                }
//
//                if (!json_obj.isNull("lm_switch")) {
//                    switch_obj = json_obj.getJSONObject("lm_switch");
//                    if (!switch_obj.isNull("bbs")) {
//                        LemonGameAdstrack.bbs = switch_obj.getString("bbs");
//                    }
//
//                    if (!switch_obj.isNull("customer_url")) {
//                        LemonGameAdstrack.customer_url = switch_obj.getString("customer_url");
//                    }
//
//                    if (!switch_obj.isNull("guide_url")) {
//                        LemonGameAdstrack.guide_url = switch_obj.getString("guide_url");
//                    }
//                }
//            } else {
//                LemonGameAdstrack.callbackListener.onComplete(1, "fail");
//            }
//        } catch (Exception var15) {
//            LemonGameExceptionUtil.handle(var15);
//        } finally {
//            this.countDownLatch.countDown();
//        }
//
//        System.out.println(this.getName() + "init线程结束" + "  " + System.currentTimeMillis());
//        if ((int)this.countDownLatch.getCount() == 0) {
//            IntlGameLogUtil.i(TAG, "进入到chartboost和fb的广告");
//            Message msg;
//            if (LemonGameAdstrack.AppId != null && LemonGameAdstrack.AppSignature != null && LemonGame.LemonGameHandlerStart != null && LemonGame.LemonGameHandlerResume != null) {
//                System.out.println(this.getName() + "chartboost线程开始" + "  " + System.currentTimeMillis());
//                msg = new Message();
//                msg.what = 103;
//                LemonGame.LemonGameHandlerStart.sendMessage(msg);
//                Message msg2 = new Message();
//                msg2.what = 112;
//                LemonGame.LemonGameHandlerResume.sendMessage(msg2);
//            }
//
//            if (LemonGameAdstrack.AdId_fb != null && LemonGame.LemonGameHandlerResume != null) {
//                IntlGameLogUtil.i(TAG, this.getName() + "fb广告线程开始" + "  " + System.currentTimeMillis());
//                msg = new Message();
//                msg.what = 106;
//                LemonGame.LemonGameHandlerResume.sendMessage(msg);
//            }
//        }
//
//    }
//}
