package com.intl.utils;

import android.content.Context;
import android.provider.Settings;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class IntlGameUtil {
    public static void getLocalGoogleAdID(final Context context, final IGgetLocalGoogleAdIdListener iGgetLocalGoogleAdIdListener){
        (new Thread(){
            public void run(){
                try{
                    Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context.getApplicationContext());
                    String googleAdID = adInfo.getId();
                    iGgetLocalGoogleAdIdListener.onComplete(0, googleAdID);
                }catch (Exception e)
                {
                    iGgetLocalGoogleAdIdListener.onComplete(-1, "");
                }
            }
        }).start();
    }
    public static String getLocalAndroidId(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(), "android_id");
        return androidID;
    }
    public interface IGgetLocalGoogleAdIdListener{
        void onComplete(int code,String ID);
    }
}
