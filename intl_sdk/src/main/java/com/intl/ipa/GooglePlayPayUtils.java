package com.intl.ipa;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

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
import com.intl.IntlGame;
import com.intl.af.AFManager;
import com.intl.api.VerifyOrderAPI;
import com.intl.utils.IntlGameLoading;
import com.intl.utils.IntlGameUtil;
import com.intl.utils.MsgManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * @Author: yujingliang
 * @Date: 2019/12/18
 */
public class GooglePlayPayUtils {
    public static String TAG = "IntlGamePay";
    private static BillingClient mBillingClient;
    public static String pay_currency = "";
    public static String pay_amounts = "";
    public static String itemids = null;
    public static int Consume = 0;
    private static int time = 0;

    public static void googlepay(final Activity context,final String itemid,final String roleinfo,final GooglePayListener googlePayListener)
    {
        Log.d(TAG, "googlepay: role_info=>"+roleinfo);
        if(itemid == null)
        {
            Log.e(TAG, "googlepay.error====>商品id = null");
            googlePayListener.onComplete(1, MsgManager.getMsg("payment_error"));
            return;
        }
        itemids = itemid;
        IntlGameLoading.getInstance().show(context);
        if(mBillingClient!=null) {
            mBillingClient = null;
        }
        mBillingClient = BillingClient.newBuilder(context).setListener(new PurchasesUpdatedListener() {
            public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
                IntlGameLoading.getInstance().show(context);
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null && !purchases.isEmpty()) {
                    Iterator iterator = purchases.iterator();
                    loop: while(iterator.hasNext()) {
                        Purchase purchase = (Purchase)iterator.next();
                        Log.d(TAG, " productId = " + purchase.getSku() + " token = " + purchase.getPurchaseToken() + "DeveloperPayload=" + purchase.getDeveloperPayload() + "  Sku = " + purchase.getSku() + ".." + purchase.getOrderId() + "..." + purchase.isAcknowledged());
                        String productId = purchase.getSku();
                        if(productId.equals(itemids))
                        {
                            String token = purchase.getPurchaseToken();
                            Log.i(TAG, googleplayutils.roleinfo + "...." + itemids);
                            IntlGameGooglePlayV3_Auth(context,token,pay_currency,pay_amounts,productId,googlePayListener,true);
                            break loop;
                        }

                    }
                } else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                    IntlGameLoading.getInstance().hide();
                    Log.e(TAG, "googlepay.cancel:用户取消了支付 ");
                    googlePayListener.onComplete(12501, MsgManager.getMsg("cancel"));
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
                        boolean flag = false;
                        loop: for (Purchase purchase : purchasesResult.getPurchasesList()) {
                            if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED)
                            {
                                final String productId = purchase.getSku();
                                if(productId.equals(itemids))
                                {
                                    final String token = purchase.getPurchaseToken();
                                    List<String> skuList  = new ArrayList<>();
                                    skuList.add(purchase.getSku());
                                    SkuDetailsParams.Builder params  = SkuDetailsParams.newBuilder();
                                    params.setSkusList(skuList ).setType(BillingClient.SkuType.INAPP);
                                    Log.d(TAG, "开始第二次查询");
                                    mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null && !skuDetailsList.isEmpty()) {
                                                Log.d(TAG, "第二次查询=: " + skuDetailsList.toString());
                                                for (SkuDetails skuDetails : skuDetailsList) {
                                                    long pay_amount = skuDetails.getPriceAmountMicros();
                                                    pay_currency = skuDetails.getPriceCurrencyCode();
                                                    long _amount = pay_amount / 1000000L;
                                                    pay_amounts = String.valueOf(_amount);
                                                    IntlGameUtil.logd(TAG, "开始第二次验证");
                                                    IntlGameGooglePlayV3_Auth(context, token,pay_currency,pay_amounts, productId, googlePayListener, false);
                                                }
                                            } else {
                                                Log.e(TAG, "googlepay.error:查询商品信息失败");
                                                googlePayListener.onComplete(1, MsgManager.getMsg("payment_error"));
                                            }

                                        }
                                    });
                                    break loop;
                                }
                            }else {
                                flag = true;
                            }
                            if(flag)
                            {
                                Log.e(TAG, "googlepay.error:状态有误");
                                googlePayListener.onComplete(1, MsgManager.getMsg("payment_error"));
                            }
                        }

                    } else {
                        Consume = 2;
                        Log.d(TAG, "onBillingSetupFinished: Consume = 2");
                        Log.i(TAG, GoogleUtils.getString(context, "itemid") + "..." + GoogleUtils.getString(context, "role_info"));
                        Log.i(TAG, GooglePlayPayUtils.itemids+ "...."+googleplayutils.roleinfo);
                        launchBillingFlow(context,itemid,googlePayListener);
                    }
                } else {
                    IntlGameLoading.getInstance().hide();
                    Log.e(TAG, "googlepay.error:google支付启动失败");
                    googlePayListener.onComplete(1, MsgManager.getMsg("googlepay_start_failed"));
                }

            }

            public void onBillingServiceDisconnected() {
                Log.e(TAG, "googlepay.error:google连接失败");
                IntlGameLoading.getInstance().hide();
                googlePayListener.onComplete(1, MsgManager.getMsg("payment_error_init_iap"));
            }
        });
    }
    static void launchBillingFlow(final Activity context,String itemid,final GooglePayListener googlePayListener)
    {
        IntlGameLoading.getInstance().hide();
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
                    }
                } else {
                    Log.e(TAG, "googlepay.error:查询商品信息失败");
                    googlePayListener.onComplete(1, MsgManager.getMsg("payment_error"));
                }

            }
        });
    }

    static void IntlGameGooglePlayV3_Consume(final Activity activity,final String token,final GooglePayListener payListener)
    {
        ++time;
        ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(token).build();
        ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "onConsumeResponse: success");
                    time = 0;
                    launchBillingFlow(activity,GooglePlayPayUtils.itemids,payListener);
                } else {
                    Log.d(TAG, "第"+time+"次onConsume: failed=====>token:"+purchaseToken);
                    if(time<5){
                        Log.d(TAG, "googlepay.consume.retry retryTime="+time);
                        IntlGameGooglePlayV3_Consume(activity,token,payListener);
                    }else {
                        time = 0;
                        IntlGameLoading.getInstance().hide();
                        Log.e(TAG, "googlepay.consume.retry.error: 商品消耗失败，token="+token);
                        IntlGame.googleplayv3Listener.onComplete(1, MsgManager.getMsg("payment_error"));
                    }
                }

            }
        };
        mBillingClient.consumeAsync(consumeParams, onConsumeListener);
    }

    static void IntlGameGooglePlayV3_Auth(final Context context, final String token, final String pay_currency, final String pay_amounts,final String productId, final GooglePayListener payListener, final boolean isCurrentOrder)
    {
        IntlGameUtil.logd(TAG,"Consumption successful. Provisioning.");
        ++IntlGame.retryTime;
        IntlGameUtil.logd(TAG,"付款成功，执行第"+IntlGame.retryTime+"次验证");
        HashMap<String,Object> paymap = new HashMap<>();
        paymap.put("token",token);
        paymap.put("product_id",productId);
        paymap.put("role_info",googleplayutils.roleinfo);
        VerifyOrderAPI verifyOrderAPI = new VerifyOrderAPI(paymap);
        verifyOrderAPI.setListener(new VerifyOrderAPI.IVerifyOrderListener() {
            @Override
            public void AfterPay(int code,String result) {
                if(isCurrentOrder)
                {
                    if(code == 0)
                    {
                        IntlGame.retryTime = 0;
                        IntlGameLoading.getInstance().hide();
                        payListener.onComplete(0, MsgManager.getMsg("payment_success"));
                        AFManager.getInstance().AdPurchase(context,productId,pay_currency,pay_amounts,"google");
                        ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(token).build();
                        ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
                            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                    Log.d(TAG, "onConsumeResponse: success");
                                } else {
                                    Log.e(TAG, "Consume: failed=====token:"+purchaseToken);
                                }
                            }
                        };
                        mBillingClient.consumeAsync(consumeParams, onConsumeListener);
                    }
                    else if(code == -1){
                        if (IntlGame.retryTime < 5)
                        {
                            Log.d(TAG, "googlepay.verify.retry: 本次订单服务器验证重试，retryTime="+IntlGame.retryTime);
                            IntlGameGooglePlayV3_Auth(context,token,pay_currency,pay_amounts,productId,payListener,isCurrentOrder);
                        }else
                        {
                            Log.e(TAG, "googlepay.verify.retry.error: 商品服务器验证失败，token="+token);
                            IntlGame.googleplayv3Listener.onComplete(1, MsgManager.getMsg("payment_error"));
                            IntlGame.retryTime = 0;
                        }
                    }else {
                        IntlGame.retryTime = 0;
                        IntlGameLoading.getInstance().hide();
                        Log.e(TAG, "googlepay.error: 商品服务器验证失败，token="+token);
                        IntlGame.googleplayv3Listener.onComplete(1,MsgManager.getMsg("payment_error"));
                    }
                }else {
                    if(code == 0)
                    {
                        IntlGame.retryTime = 0;
                        IntlGameUtil.logd(TAG,"上次订单发货完毕，to do=>Consume");
                        IntlGameGooglePlayV3_Consume((Activity) context,token,payListener);
                    }else if(code == -1){
                        if (IntlGame.retryTime < 5)
                        {
                            Log.d(TAG, "googlepay.verify.retry: 上次订单服务器验证重试，retryTime="+IntlGame.retryTime);
                            IntlGameGooglePlayV3_Auth(context,token,pay_currency,pay_amounts,productId,payListener,isCurrentOrder);
                        }else
                        {
                            Log.d(TAG, "googlepay.verify.retry: 上次订单服务器验证通过，调起支付页面");
                            launchBillingFlow((Activity) context,GooglePlayPayUtils.itemids,payListener);
                            IntlGame.retryTime = 0;
                        }
                    }
                    else {
                        IntlGame.retryTime = 0;
                        IntlGameLoading.getInstance().hide();
                        Log.e(TAG, "googlepay.error: 上次支付商品，服务器验证失败，token="+token);
                        IntlGame.googleplayv3Listener.onComplete(1,MsgManager.getMsg("payment_error"));
                    }

                }

            }
        });
        verifyOrderAPI.Excute();
    }
    public interface GooglePayListener {
        void onComplete(int code, String msg);
    }

    public static void onResume(final Activity context)
    {
        ReplyToPurchase(context);

    }

    public static void onDestroy()
    {
        if (mBillingClient != null && mBillingClient.isReady()) {
            mBillingClient.endConnection();
            mBillingClient = null;
        }
    }

    public static void ReplyToPurchase(final Activity context)
    {
        if(googleplayutils.roleinfo != null)
            return;
        mBillingClient = BillingClient.newBuilder(context).setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

            }
        }).enablePendingPurchases().build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
                    if (purchasesResult.getPurchasesList() != null && purchasesResult.getPurchasesList().size() != 0) {
                        Log.i(TAG, "ReplyToPurchase Purchase:" + purchasesResult.getPurchasesList().toString());
                        for (Purchase purchase : purchasesResult.getPurchasesList()) {
                            if(purchase.getPurchaseState() != Purchase.PurchaseState.PURCHASED){
                                final String productId = purchase.getSku();
                                final String token = purchase.getPurchaseToken();
                                List<String> skuList  = new ArrayList<>();
                                skuList.add(purchase.getSku());
                                SkuDetailsParams.Builder params  = SkuDetailsParams.newBuilder();
                                params.setSkusList(skuList ).setType(BillingClient.SkuType.INAPP);
                                mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                                    @Override
                                    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null && !skuDetailsList.isEmpty()) {
                                            Log.d(TAG, "ReplyToPurchase 查询=: " + skuDetailsList.toString());
                                            for (SkuDetails skuDetails : skuDetailsList) {
                                                long pay_amount = skuDetails.getPriceAmountMicros();
                                                pay_currency = skuDetails.getPriceCurrencyCode();
                                                long _amount = pay_amount / 1000000L;
                                                pay_amounts = String.valueOf(_amount);
                                                HashMap<String,Object> paymap = new HashMap<>();
                                                paymap.put("token",token);
                                                paymap.put("product_id",productId);
                                                paymap.put("role_info",googleplayutils.roleinfo);
                                                VerifyOrderAPI verifyOrderAPI = new VerifyOrderAPI(paymap);
                                                verifyOrderAPI.setListener(new VerifyOrderAPI.IVerifyOrderListener() {
                                                    @Override
                                                    public void AfterPay(int code, String result) {
                                                        if(code == 0)
                                                        {
                                                            ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(token).build();
                                                            ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
                                                                public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                                                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                                                        Log.d(TAG, "ReplyToPurchase Consume: success");
                                                                    } else {
                                                                        Log.e(TAG, "ReplyToPurchase Consume: failed=====token:"+purchaseToken);
                                                                    }
                                                                }
                                                            };
                                                            mBillingClient.consumeAsync(consumeParams, onConsumeListener);
                                                        }
                                                    }
                                                });
                                                verifyOrderAPI.Excute();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.e(TAG, "ReplyToPurchase onBillingServiceDisconnected!");
            }
        });
    }
}
