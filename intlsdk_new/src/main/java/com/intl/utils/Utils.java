package com.intl.utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.Toast;

import com.intl.R;
import com.intl.base.IntlContext;
import com.intl.login.model.IntlLoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * @Author: yujingliang
 * @Date: 2020/1/13
 */
public class Utils {
    public Utils() {
    }

    public static String getAppVersionName(Context mContext) {
        String versionName = "";

        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
        }

        return versionName;
    }

    public static int getAppVersionCode(Context mContext) {
        int versionCode = 0;

        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
        }

        return versionCode;
    }

    public static String getDeviceIDVCC(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        return androidId;
    }

    public static String getAppName(Context mContext) {
        String appName = "Intlgame";

        try {
            int stringId = mContext.getApplicationInfo().labelRes;
            appName = mContext.getString(stringId);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return appName;
    }

    public static Spanned fromHtml(String html) {
        return Build.VERSION.SDK_INT >= 24 ? Html.fromHtml(html, 0) : Html.fromHtml(html);
    }

    public static int getAppIcon(Context mContext) {
        int appIcon = 17301595;

        try {
            appIcon = mContext.getApplicationInfo().icon;
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return appIcon;
    }

    public static String getSDKVersion(Context context) {
        return context.getApplicationContext().getString(R.string.intl_sdk_version);
    }

    public static void showToastError(Context context) {
        showToast(context, context.getString(R.string.intl_error_generic));
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
    }

    public static JSONObject createDefaultParams(Context context) {
//        InitModel initModel = (InitModel)PrefUtils.getObject("PREF_INIT_MODEL", InitModel.class);
//        IntlLoginResult intlLoginResult = (IntlLoginResult)PrefUtils.getObject("PREF_LOGIN_RESULT", IntlLoginResult.class);
//        String accessToken = "";
//        if (intlLoginResult != null) {
//            accessToken = intlLoginResult.getAccessToken();
//        }

        String area_id = "";
        String role_id = "";
        String role_name = "";
        String role_level = "";
//        UserGameInfo userGameInfo = (UserGameInfo)PrefUtils.getObject("PREF_USER_GAME_INFO", UserGameInfo.class);
//        if (userGameInfo != null) {
//            area_id = userGameInfo.getAreaId();
//            role_id = userGameInfo.getRoleId();
//            role_name = Base64.encodeToString(userGameInfo.getRoleName().getBytes(), 0);
//            role_level = userGameInfo.getRoleLevel();
//        }

        JSONObject obj = new JSONObject();
        String clientIdMqtt = getClientIdMQTT();

        try {
//            obj.put("app_id", initModel.getAppId());
//            obj.put("area_id", area_id);
//            obj.put("role_id", role_id);
//            obj.put("role_name", role_name);
//            obj.put("role_level", role_level);
//            obj.put("gver", getAppVersionName(context));
//            obj.put("sdkver", getSDKVersion(context));
//            obj.put("clientname", initModel.getClientName());
//            obj.put("access_token", accessToken);
            obj.put("device_id_vcc", getDeviceIDVCC(context));
            obj.put("bundle_id", context.getApplicationContext().getPackageName());
            obj.put("device_id", PrefUtils.getString("PREF_ADS_ID_GG"));
            obj.put("redirect_uri", "uri_login");
            obj.put("client_id", clientIdMqtt);
        } catch (JSONException var12) {
            var12.printStackTrace();
        }

        return obj;
    }

    public static String getClientIdMQTT() {
        String clientId = PrefUtils.getString("PREF_CLIENTID_MQTT");
//        if (TextUtils.isEmpty(clientId)) {
//            clientId = String.format("%s_%s_%s_%s_%s", initModel.getClientCode(), initModel.getAppId(), getDeviceIDVCC(IntlContext.getApplicationContext()), System.currentTimeMillis(), UUID.randomUUID().toString().substring(0, 5));
//            PrefUtils.putString("PREF_CLIENTID_MQTT", clientId);
//        }

        return clientId;
    }

    public static void getKeyhash(Context context) {
        try {
            @SuppressLint("WrongConstant") PackageInfo info = context.getPackageManager().getPackageInfo(context.getApplicationContext().getPackageName(), 64);
            Signature[] var2 = info.signatures;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Signature signature = var2[var4];
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException var7) {
            var7.printStackTrace();
        } catch (NoSuchAlgorithmException var8) {
            var8.printStackTrace();
        }

    }

    public static boolean isPermissionEnable(Context context, String[] permission) {
        boolean isPermission = true;
        String[] var3 = permission;
        int var4 = permission.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String s = var3[var5];
            int result = ContextCompat.checkSelfPermission(context, s);
            if (result != 0) {
                isPermission = false;
            }
        }

        return isPermission;
    }

    @SuppressLint("WrongConstant")
    public static void openWeb(Context context, String urlWeb) {
        try {
            Uri uri = Uri.parse(urlWeb);
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            intent.setFlags(268435456);
            context.startActivity(intent);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static boolean isAppInstalled(String packagename, Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    @SuppressLint("WrongConstant")
    public static void openAppStore(Context context, String packagedata) {
        try {
            Uri uri = Uri.parse("market://details?id=" + packagedata);
            Intent myAppLinkToMarket = new Intent("android.intent.action.VIEW", uri);
            myAppLinkToMarket.setFlags(268435456);
            context.startActivity(myAppLinkToMarket);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static boolean isValid(String urlString) {
        try {
            new URL(urlString);
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
        } catch (MalformedURLException var2) {
            var2.printStackTrace();
            return false;
        }
    }

    @SuppressLint("WrongConstant")
    public static void openAppsLaunch(Context context, String packageApps) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageApps);
        launchIntent.setFlags(268435456);
        context.startActivity(launchIntent);
    }

    public static void getOpenFacebook(Context context, String idHere) {
        Uri uri = Uri.parse(idHere);

        String linkFb;
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + idHere);
                linkFb = "fb://facewebmodal/f?href=" + idHere;
            } else {
                linkFb = idHere;
            }
        } catch (Exception var6) {
            linkFb = idHere;
            var6.printStackTrace();
        }

        openWeb(context, linkFb);
    }

    public static String getDateSystem() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(Calendar.getInstance().getTime());
    }

    public static boolean isCheckMobile(Context context) {
        String ua = (new WebView(context)).getSettings().getUserAgentString();
        return ua.contains("Mobile");
    }

    public static String getLanguage() {
        String languageDevice = Locale.getDefault().getLanguage();
        if (languageDevice.equals("vi")) {
            return "vi-VN";
        } else {
            return languageDevice.equals("zh") ? "zh-CN" : "en-US";
        }
    }

    static void setupLanguage(Context context, String languageToLoad) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        Locale locale = new Locale(languageToLoad);
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }

    @SuppressLint("WrongConstant")
    public static void generateNotification(Context context, String title, String messageBody, Bitmap bitmap, String campaign) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());

        assert intent != null;

        intent.putExtra("open_notifi", true);
        if (!TextUtils.isEmpty(campaign)) {
            intent.putExtra("campaign", campaign);
        }

        intent.addFlags(603979776);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 134217728);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(messageBody);
        String channelId = context.getPackageName();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(2);
        NotificationCompat.Builder notificationBuilder = (new NotificationCompat.Builder(context, channelId)).setSmallIcon(R.drawable.intl_ic_intlgame).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), context.getApplicationInfo().icon)).setContentTitle(title).setContentText(messageBody).setStyle(bigTextStyle).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
        if (bitmap != null) {
            notificationBuilder.setStyle((new NotificationCompat.BigPictureStyle()).bigPicture(bitmap));
        }

        NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel channel = new NotificationChannel(channelId, "Intl", 3);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
