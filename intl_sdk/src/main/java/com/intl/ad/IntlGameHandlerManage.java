package com.intl.ad;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.intl.IntlGame;
import com.intl.sqlite.IntlGameDBHelper;
import com.intl.utils.IntlGameExceptionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @Author: yujingliang
 * @Date: 2019/11/21
 */
public class IntlGameHandlerManage {
    private static String TAG = "IntlGameHandlerManage";
    static Dialog dialog;
    static Dialog dialogs;

    public IntlGameHandlerManage() {
    }

    static void checkHandler() {
        try {
            if (IntlGame.IntlGameHandler == null) {
                IntlGame.IntlGameHandler = new Handler();
            }
        } catch (Exception var1) {
            IntlGameExceptionUtil.handle(var1);
            IntlGame.IntlGameHandler = null;
        }

    }

    public static void IntlGame_HandlerManage(final Context context) {
        if (IntlGame.IntlGameHandler == null) {
            IntlGame.IntlGameHandler = new Handler(Looper.getMainLooper()) {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    HashMap mapx;
                    IntlGameDBHelper var10000;
                    switch(msg.what) {
                        case 1:
                            PopupWindow popup = (PopupWindow)msg.obj;
                            popup.dismiss();
                            break;
                        case 2:
                            Dialog dialog = (Dialog)msg.obj;
                            dialog.dismiss();
                            break;
                        case 101:
                            if (IntlGameAdstrack.devKey == null && !IntlGame.db.isHaveColumn(IntlGameUnionConfig.segment, IntlGameUnionConfig.appsFlyer_channel)) {
                                var10000 = IntlGame.db;
                                HashMap map = IntlGameDBHelper.select_defaultunionConfig_allvalue(IntlGameUnionConfig.segment, IntlGameUnionConfig.appsFlyer_channel);
                                IntlGameAdstrack.devKey = map.get("number1").toString();
                                IntlGameLogUtil.i(IntlGameHandlerManage.TAG, "从数据库取appsflyer数据：" + map.get("number1").toString());
                            }

                            AppsFlyerConversionListener conversionDataListener = new AppsFlyerConversionListener() {
                                @WorkerThread
                                public void onAppOpenAttribution(Map<String, String> arg0) {
                                }

                                public void onAttributionFailure(String arg0) {
                                }

                                @WorkerThread
                                public void onInstallConversionDataLoaded(Map<String, String> arg0) {
                                }

                                public void onInstallConversionFailure(String arg0) {
                                }
                            };
                            AppsFlyerLib.getInstance().init(IntlGameAdstrack.devKey, conversionDataListener, context);
                            AppsFlyerLib.getInstance().startTracking(IntlGame.application);
                            IntlGameLogUtil.i(IntlGameHandlerManage.TAG, "appsflyer开始");
                            AppsFlyerLib.getInstance().setAndroidIdData(IntlGameUtil.getLocalAndroidId(context));
                            AppsFlyerLib.getInstance().setDebugLog(true);
                            Map<String, Object> mapa = new HashMap();
                            mapa.put("Start", "1");
                            IntlGame.AfEvent(context, "Start_AppLaunch", mapa);
                            if (IntlGame.eventindex == 2) {
                                mapx = new HashMap();
                                mapx.put("Policies terms", "1");
                                IntlGame.AfEvent(IntlGame.LM_ctx, "Policies terms_Agree", mapx);
                            }
                            break;
                        case 102:
                            if (IntlGameAdstrack.AppId == null && IntlGame.db.isHaveColumn(IntlGameUnionConfig.segment, IntlGameUnionConfig.chartboost_channel)) {
                                var10000 = IntlGame.db;
                                mapx = IntlGameDBHelper.select_defaultunionConfig_allvalue(IntlGameUnionConfig.segment, IntlGameUnionConfig.chartboost_channel);
                                IntlGameAdstrack.AppId = mapx.get("number1").toString();
                                IntlGameAdstrack.AppSignature = mapx.get("number2").toString();
                                IntlGameLogUtil.i(IntlGameHandlerManage.TAG, "从数据库取chartboost数据appId：" + mapx.get("number1").toString());
                                IntlGameLogUtil.i(IntlGameHandlerManage.TAG, "从数据库取chartboost数据AppSignature：" + mapx.get("number2").toString());
                            }
                            break;
                        case 104:
                            try {
                                if (IntlGameAdstrack.inmobi_AppId == null && IntlGame.db.isHaveColumn(IntlGameUnionConfig.segment, IntlGameUnionConfig.inmobi_channel)) {
                                    var10000 = IntlGame.db;
                                    mapx = IntlGameDBHelper.select_defaultunionConfig_allvalue(IntlGameUnionConfig.segment, IntlGameUnionConfig.inmobi_channel);
                                    IntlGameAdstrack.inmobi_AppId = mapx.get("number1").toString();
                                    IntlGameLogUtil.i(IntlGameHandlerManage.TAG, "从数据库取inmobi数据：" + mapx.get("number1").toString());
                                }

                                IntlGameLogUtil.i(IntlGameHandlerManage.TAG, "inmobi广告开始");
                            } catch (Exception var21) {
                                IntlGameExceptionUtil.handle(var21);
                            }
                            break;
                        case 113:
                            if (IntlGameAdstrack.rockssenderId != null) {
                                Hashtable<String, Object> connectFlags = new Hashtable();
                                connectFlags.put("TJC_OPTION_ENABLE_LOGGING", "true");
                                Tapjoy.setGcmSender(IntlGameAdstrack.rockssenderId);
                                Tapjoy.connect(context.getApplicationContext(), IntlGameAdstrack.rockssdkKey, connectFlags, new TJConnectListener() {
                                    public void onConnectSuccess() {
                                        if (IntlGame.eventindex == 2) {
                                            IntlGame.LemonTapjoyEvent("policies terms");
                                        }

                                        String yes = UtiUtils.getString(context, "Pushyes");
                                        String no = UtiUtils.getString(context, "Pushno");
                                        if (yes != null && !yes.equals("")) {
                                            Log.i(IntlGameHandlerManage.TAG, "11");
                                            Tapjoy.isPushNotificationDisabled();
                                            Tapjoy.setPushNotificationDisabled(false);
                                        } else {
                                            Log.i(IntlGameHandlerManage.TAG, "22");
                                            if (no != null && !no.equals("")) {
                                                Log.i(IntlGameHandlerManage.TAG, "33");
                                                Tapjoy.isPushNotificationDisabled();
                                                Tapjoy.setPushNotificationDisabled(true);
                                            } else {
                                                Log.i(IntlGameHandlerManage.TAG, "44");
                                                Tapjoy.isPushNotificationDisabled();
                                                Tapjoy.setPushNotificationDisabled(false);
                                                UtiUtils.putString(context, "Pushyes", "Pushyes");
                                                SharedPreferences sp = context.getSharedPreferences("TrineaAndroidCommon", 0);
                                                SharedPreferences.Editor editor = sp.edit();
                                                editor.remove("Pushno");
                                                editor.commit();
                                            }
                                        }

                                    }

                                    public void onConnectFailure() {
                                        IntlGameLogUtil.i(IntlGameHandlerManage.TAG, "TapjoyFailure");
                                    }
                                });
                            }
                    }

                }
            };
        }

    }
}
