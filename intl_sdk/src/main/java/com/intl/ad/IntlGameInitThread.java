package com.intl.ad;

import android.os.Message;

import com.intl.IntlGame;
import com.intl.utils.IntlGameLogUtil;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: yujingliang
 * @Date: 2019/11/21
 */
public class IntlGameInitThread extends Thread{
    private static String TAG = "lemongame_initThread";
    private CountDownLatch countDownLatch;

    public IntlGameInitThread(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void run() {
        System.out.println(this.getName() + "init线程开始" + "  " + System.currentTimeMillis());

        try {
            IntlGameLogUtil.i(TAG, IntlGameAdstrack.bundle.toString());
            String resp = IntlGameLogUtil.openUrl(LemonGame.ADS_CONFIGURATION, "POST", LemonGameAdstrack.bundle, "");
            IntlGameLogUtil.i(TAG, resp);
            JSONObject json = new JSONObject(resp);
            String info_code = json.getString("code");
            String info = json.getString("info");
            IntlGameLogUtil.i(TAG, "ad_info_code:" + info_code);
            IntlGameLogUtil.i(TAG, "ad_info_info:" + info);
            int code = Integer.parseInt(info_code);
            if (code == 0) {
                IntlGameAdstrack.callbackListener.onComplete(0, "success");
                JSONObject json_obj = new JSONObject(info);
                JSONObject switch_obj;
                if (!json_obj.isNull("appsFlyer")) {
                    switch_obj = json_obj.getJSONObject("appsFlyer");
                    IntlGameLogUtil.i(TAG, "2222");
                    IntlGameAdstrack.devKey = switch_obj.getString("DevKey");
                    IntlGameLogUtil.i(TAG, "appsflyer_id:" + LemonGameAdstrack.devKey);
                    if (IntlGame.db.isHaveColumn(LemonGameUnionConfig.segment, LemonGameUnionConfig.appsFlyer_channel)) {
                        IntlGameLogUtil.i(TAG, "查询不到appsflyer对应的数据，开始插入");
                        IntlGame.db.insert_account_unionConfig(LemonGameUnionConfig.segment, LemonGameUnionConfig.appsFlyer_channel, LemonGameAdstrack.devKey, "", "");
                    } else {
                        IntlGameLogUtil.i(TAG, "查询到appsflyer对应的数据，开始修改");
                        IntlGame.db.updateData(LemonGameUnionConfig.segment, LemonGameUnionConfig.appsFlyer_channel, LemonGameAdstrack.devKey, "", "");
                    }

                    msg_version = new Message();
                    msg_version.what = 101;
                    IntlGame.LemonGameHandler.sendMessage(msg_version);
                }
            } else {
                LemonGameAdstrack.callbackListener.onComplete(1, "fail");
            }
        } catch (Exception var15) {
            LemonGameExceptionUtil.handle(var15);
        } finally {
            this.countDownLatch.countDown();
        }

        System.out.println(this.getName() + "init线程结束" + "  " + System.currentTimeMillis());
        if ((int)this.countDownLatch.getCount() == 0) {
            IntlGameLogUtil.i(TAG, "进入到chartboost和fb的广告");
            Message msg;
            if (LemonGameAdstrack.AppId != null && LemonGameAdstrack.AppSignature != null && LemonGame.LemonGameHandlerStart != null && LemonGame.LemonGameHandlerResume != null) {
                System.out.println(this.getName() + "chartboost线程开始" + "  " + System.currentTimeMillis());
                msg = new Message();
                msg.what = 103;
                LemonGame.LemonGameHandlerStart.sendMessage(msg);
                Message msg2 = new Message();
                msg2.what = 112;
                LemonGame.LemonGameHandlerResume.sendMessage(msg2);
            }

            if (LemonGameAdstrack.AdId_fb != null && LemonGame.LemonGameHandlerResume != null) {
                IntlGameLogUtil.i(TAG, this.getName() + "fb广告线程开始" + "  " + System.currentTimeMillis());
                msg = new Message();
                msg.what = 106;
                LemonGame.LemonGameHandlerResume.sendMessage(msg);
            }
        }

    }
}
