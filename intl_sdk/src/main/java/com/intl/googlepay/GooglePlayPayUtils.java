package com.intl.googlepay;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;


import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.intl.api.VerifyOrderAPI;
import com.intl.utils.IntlGameLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * @Author: yujingliang
 * @Date: 2019/12/18
 */
public class GooglePlayPayUtils {
    public static String TAG = "GooglePlayPayUtils";
    private static BillingClient mBillingClient;
    public static String pay_currency = "";
    public static String pay_amounts = "";
    public static String itemids = null;
    public static String packagename = "";
    public static String OrderId = "";
    public static String token = "";
    public static String payexts = "";
    public static String serverids = "";
    public static int Consume = 0;
    static String currency;

    public static void googlepay(final Activity context,final String itemid,final String roleinfo,final GooglePayListener googleplayv3Listener)
    {
        Log.d(TAG, "googlepay: role_info=>"+roleinfo);
        if(itemid == null)
        {
            googleplayv3Listener.onComplete(200, "商品id = null");
            return;
        }
        IntlGameLoading.getInstance().show(context);
        mBillingClient = BillingClient.newBuilder(context).setListener(new PurchasesUpdatedListener() {
            public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null && !purchases.isEmpty()) {
                    Iterator var4 = purchases.iterator();

                    while(var4.hasNext()) {
                        Purchase purchase = (Purchase)var4.next();
                        Log.d(TAG, " productId = " + purchase.getSku() + " token = " + purchase.getPurchaseToken() + "DeveloperPayload=" + purchase.getDeveloperPayload() + "  Sku = " + purchase.getSku() + ".." + purchase.getOrderId() + "..." + purchase.isAcknowledged());
                        itemids = purchase.getSku();
                        packagename = purchase.getPackageName();
                        OrderId = purchase.getOrderId();
                        token = purchase.getPurchaseToken();
                        Log.i(TAG, payexts + "...." + itemids);
                        HashMap<String,Object> paymap = new HashMap<>();
                        paymap.put("token",token);
                        paymap.put("roduct_id",itemids);
                        paymap.put("role_info",roleinfo);
                        VerifyOrderAPI verifyOrderAPI = new VerifyOrderAPI(paymap);
                        verifyOrderAPI.setListener(new VerifyOrderAPI.IVerifyOrderListener() {
                            @Override
                            public void AfterPay(int code,String result) {
                                if(code == 0)
                                {
                                    ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(token).build();
                                    ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
                                        public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                                Log.d(TAG, "onConsumeResponse: 消耗商品成功！");
                                                IntlGameLoading.getInstance().hide();
                                                googleplayv3Listener.onComplete(0, "ok");
                                            } else {
                                                IntlGameLoading.getInstance().hide();

                                                googleplayv3Listener.onComplete(104, "消耗商品失败");
                                            }
                                        }
                                    };
                                    mBillingClient.consumeAsync(consumeParams, onConsumeListener);
                                }else {
                                    IntlGameLoading.getInstance().hide();

                                    googleplayv3Listener.onComplete(108, "fill");
                                }

                            }
                        });
                        verifyOrderAPI.Excute();

//                        verification(token, itemids, roleinfo, new VerifyOrderListener() {
//                            @Override
//                            public void onComplete(int code, String msg) {
//                                if(code == 0)
//                                {
//                                    ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(token).build();
//                                    ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
//                                        public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
//                                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                                                Log.d(TAG, "onConsumeResponse: 消耗商品成功！");
//                                                googleplayv3Listener.onComplete(0, "ok");
//                                            } else {
//                                                googleplayv3Listener.onComplete(104, "消耗商品失败");
//                                            }
//                                        }
//                                    };
//                                    mBillingClient.consumeAsync(consumeParams, onConsumeListener);
//                                }else {
//                                    googleplayv3Listener.onComplete(108, "fill");
//                                }
//                            }
//                        });

                    }
                } else {
                    IntlGameLoading.getInstance().hide();
                    googleplayv3Listener.onComplete(105, "fill");
                }

            }
        }).enablePendingPurchases().build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            public void onBillingSetupFinished(BillingResult billingResult) {
                Log.d(TAG, "Setup finished. Response code: " + billingResult.getResponseCode());
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
                    if (purchasesResult.getPurchasesList() != null && purchasesResult.getPurchasesList().size() != 0) {
                        Consume = 1;
                        Log.i(TAG, "Purchase:" + purchasesResult.getPurchasesList().toString());
                        Iterator var8 = purchasesResult.getPurchasesList().iterator();
                        while(true) {
                            while(var8.hasNext()) {
                                final Purchase purchase = (Purchase)var8.next();
                                itemids = purchase.getSku();
                                packagename = purchase.getPackageName();
                                OrderId = purchase.getOrderId();
                                token = purchase.getPurchaseToken();
                                if (!TextUtils.isEmpty(GoogleUtils.getString(context, "itemid")) && !TextUtils.isEmpty(GoogleUtils.getString(context, "pay_ext")) && !TextUtils.isEmpty(GoogleUtils.getString(context, "role_info"))) {
                                    payexts = GoogleUtils.getString(context, "pay_ext");
                                    if (GoogleUtils.getString(context, "itemid").equals(itemids)) {
                                        List skuListx = new ArrayList();
                                        skuListx.add(purchase.getSku());
                                        SkuDetailsParams.Builder paramsx = SkuDetailsParams.newBuilder();
                                        paramsx.setSkusList(skuListx).setType(BillingClient.SkuType.INAPP);
                                        mBillingClient.querySkuDetailsAsync(paramsx.build(), new SkuDetailsResponseListener() {
                                            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null && !skuDetailsList.isEmpty()) {
                                                    Log.d(TAG, "第二次消耗查询=: " + skuDetailsList.toString());
                                                    Iterator var4 = skuDetailsList.iterator();

                                                    while(var4.hasNext()) {
                                                        SkuDetails skuDetails = (SkuDetails)var4.next();
                                                        long pay_amount = skuDetails.getPriceAmountMicros();
                                                        pay_currency = skuDetails.getPriceCurrencyCode();
                                                        long _amount = pay_amount / 1000000L;
                                                        pay_amounts = String.valueOf(_amount);
                                                        HashMap<String,Object> paymap = new HashMap<>();
                                                        paymap.put("token",token);
                                                        paymap.put("roduct_id",itemids);
                                                        paymap.put("role_info",roleinfo);
                                                        VerifyOrderAPI verifyOrderAPI = new VerifyOrderAPI(paymap);
                                                        verifyOrderAPI.setListener(new VerifyOrderAPI.IVerifyOrderListener() {
                                                            @Override
                                                            public void AfterPay(int code,String result) {
                                                                if(code == 0)
                                                                {
                                                                    ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(token).build();
                                                                    ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
                                                                        public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                                                                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                                                                Log.d(TAG, "onConsumeResponse: 消耗商品成功！");
                                                                                IntlGameLoading.getInstance().hide();
                                                                                googleplayv3Listener.onComplete(0, "ok");
                                                                            } else {
                                                                                IntlGameLoading.getInstance().hide();
                                                                                googleplayv3Listener.onComplete(104, "消耗商品失败");
                                                                            }
                                                                        }
                                                                    };
                                                                    mBillingClient.consumeAsync(consumeParams, onConsumeListener);
                                                                }else {
                                                                    IntlGameLoading.getInstance().hide();
                                                                    googleplayv3Listener.onComplete(108, "fill");
                                                                }

                                                            }
                                                        });
                                                        verifyOrderAPI.Excute();

//                                                        verification(token, itemids, roleinfo, new VerifyOrderListener() {
//                                                            @Override
//                                                            public void onComplete(int code, String msg) {
//                                                                if(code == 0)
//                                                                {
//                                                                    ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(token).build();
//                                                                    ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
//                                                                        public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
//                                                                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                                                                                googleplayv3Listener.onComplete(0, "ok");
//                                                                            } else {
//                                                                                googleplayv3Listener.onComplete(104, "消耗商品失败");
//                                                                            }
//                                                                        }
//                                                                    };
//                                                                    mBillingClient.consumeAsync(consumeParams, onConsumeListener);
//                                                                }else {
//                                                                    googleplayv3Listener.onComplete(108, "fill");
//                                                                }
//                                                            }
//                                                        });

                                                    }
                                                } else {
                                                    IntlGameLoading.getInstance().hide();
                                                    googleplayv3Listener.onComplete(107, "查询商品信息失败");
                                                }

                                            }
                                        });
                                    }
                                } else {
                                    Log.i(TAG, "不是最新");
                                    ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                                    ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
                                        public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                                IntlGameLoading.getInstance().hide();
                                                googleplayv3Listener.onComplete(101, "ok");
                                            } else {
                                                IntlGameLoading.getInstance().hide();
                                                googleplayv3Listener.onComplete(106, "消耗商品失败");
                                            }

                                        }
                                    };
                                    mBillingClient.consumeAsync(consumeParams, onConsumeListener);
                                }
                            }

                            return;
                        }
                    } else {
                        Consume = 2;
                        Log.d(TAG, "onBillingSetupFinished: Consume = 2");
                        uitlsremove(context, "role_info");
                        uitlsremove(context, "itemid");
                        Log.i(TAG, GoogleUtils.getString(context, "pay_ext") + "..." + GoogleUtils.getString(context, "serverId") + "...." + GoogleUtils.getString(context, "itemid"));
                        if (TextUtils.isEmpty(GoogleUtils.getString(context, "role_info"))) {
                            GoogleUtils.putString(context, "role_info", roleinfo);
                        }

                        if (TextUtils.isEmpty(GoogleUtils.getString(context, "itemid"))) {
                            GoogleUtils.putString(context, "itemid", itemid);
                        }

                        if (!TextUtils.isEmpty(GoogleUtils.getString(context, "itemid"))) {
                            itemids = GoogleUtils.getString(context, "itemid");
                        } else {
                            itemids = itemid;
                        }

                        Log.i(TAG, payexts + "..." + serverids + "...." + itemids);
                        List skuList = new ArrayList();
                        skuList.add(itemid);
                        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                        mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null && !skuDetailsList.isEmpty()) {
                                    Log.d(TAG, "skuDetailsList=: " + skuDetailsList.toString());
                                    Iterator var4 = skuDetailsList.iterator();

                                    while(var4.hasNext()) {
                                        SkuDetails skuDetails = (SkuDetails)var4.next();
                                        long pay_amount = skuDetails.getPriceAmountMicros();
                                        pay_currency = skuDetails.getPriceCurrencyCode();
                                        long _amount = pay_amount / 1000000L;
                                        pay_amounts = String.valueOf(_amount);
                                        BillingFlowParams purchaseParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build();
                                        mBillingClient.launchBillingFlow(context, purchaseParams);
                                        IntlGameLoading.getInstance().hide();
                                        googleplayv3Listener.onComplete(2, "查询商品信息成功");
                                    }
                                } else {
                                    IntlGameLoading.getInstance().hide();
                                    googleplayv3Listener.onComplete(103, "查询商品信息失败");
                                }

                            }
                        });
                    }
                } else {
                    IntlGameLoading.getInstance().hide();
                    googleplayv3Listener.onComplete(101, "google支付启动失败");
                }

            }

            public void onBillingServiceDisconnected() {
                IntlGameLoading.getInstance().hide();
                googleplayv3Listener.onComplete(102, "google连接失败");
            }
        });
    }

    public static void uitlsremove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("GooglePayUtils", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }
    public interface GooglePayListener {
        void onComplete(int code, String msg);
    }
    public interface VerifyOrderListener{
        void onComplete(int code, String msg);
    }
}
