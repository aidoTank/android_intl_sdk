package com.intl;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.intl.base.IntlContext;
import com.intl.init.model.InitModel;
import com.intl.init.presenter.InitPresenter;
import com.intl.utils.LocaleManager;
import com.intl.utils.PrefUtils;
import com.intl.utils.Utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: yujingliang
 * @Date: 2020/1/14
 */
public class IntlApplication extends MultiDexApplication {
    private Context context;

    public IntlApplication() {
    }

    public void onCreate() {
        super.onCreate();
        this.context = this;
        this.init(this);
    }

    public Context getContext() {
        return this.context;
    }

    public void init(Context context) {
        IntlContext.setApplicationContext(context.getApplicationContext());
        this.getAdvertisingIdClient();
        InitPresenter.readParamsFromAssets(IntlContext.getApplicationContext());
        this.initFirebase(IntlContext.getApplicationContext());
        this.initFacebook();
        this.initAppsflyer();
    }

    private void initAppsflyer() {
        InitModel initModel = (InitModel) PrefUtils.getObject("PREF_INIT_MODEL", InitModel.class);
        AppsFlyerConversionListener conversionDataListener = new AppsFlyerConversionListener() {
            public void onInstallConversionDataLoaded(Map<String, String> map) {
            }

            public void onInstallConversionFailure(String s) {
            }

            public void onAppOpenAttribution(Map<String, String> conversionData) {
                Iterator var2 = conversionData.keySet().iterator();

                while(var2.hasNext()) {
                    String attrName = (String)var2.next();
                }

            }

            public void onAttributionFailure(String s) {
            }
        };
        String appId = initModel.getAppIdAppsflyer();
        AppsFlyerLib.getInstance().init(appId, conversionDataListener, this.getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);
    }

    private void getAdvertisingIdClient() {
        String idAds = PrefUtils.getString("PREF_ADS_ID_GG");
        final String deviceIdVcc = Utils.getDeviceIDVCC(IntlContext.getApplicationContext());
        if (TextUtils.isEmpty(idAds) || idAds.equals(deviceIdVcc)) {
            (new Thread(new Runnable() {
                public void run() {
                    AdvertisingIdClient.Info adInfo = null;

                    try {
                        adInfo = AdvertisingIdClient.getAdvertisingIdInfo(IntlContext.getApplicationContext());
                    } catch (GooglePlayServicesNotAvailableException var3) {
                        var3.printStackTrace();
                    } catch (GooglePlayServicesRepairableException var4) {
                        var4.printStackTrace();
                    } catch (IOException var5) {
                        var5.printStackTrace();
                    }

                    if (adInfo != null) {
                        String idAds = adInfo.getId();
                        PrefUtils.putString("PREF_ADS_ID_GG", idAds);
                    } else {
                        PrefUtils.putString("PREF_ADS_ID_GG", deviceIdVcc);
                    }

                }
            })).start();
        }

    }

    private void initFirebase(Context context) {
        //FCMInit.initFirebase(context);
    }

    private void initFacebook() {
        InitModel initModel = (InitModel)PrefUtils.getObject("PREF_INIT_MODEL", InitModel.class);
        if (initModel == null) {
            Utils.showToast(IntlContext.getApplicationContext(), IntlContext.getApplicationContext().getString(R.string.intl_error_init));
        } else {
            FacebookSdk.setApplicationId(initModel.getAppIdFacebook());
            FacebookSdk.sdkInitialize(IntlContext.getApplicationContext());
        }
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.getInstance().setLocale(base));
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.getInstance().setLocale(this);
    }
}
