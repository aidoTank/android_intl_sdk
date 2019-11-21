package com.intl.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

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

    public static String md5(String source) {
        StringBuffer sb = new StringBuffer(32);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                array = md.digest(source.getBytes(StandardCharsets.UTF_8));
            }

            for(int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString(array[i] & 255 | 256).toUpperCase().substring(1, 3));
            }
        } catch (Exception var5) {
            return null;
        }

        return sb.toString().toLowerCase();
    }

    public static boolean checkNetwork(Activity act) {
        ConnectivityManager manager = (ConnectivityManager)act.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        } else {
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            return networkinfo != null && networkinfo.isAvailable();
        }
    }

    public static String getLocalSimOperator(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String simOperator = tm.getSimOperator();
        return simOperator;
    }

    public static String getLocalNetwordTypeName(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        String typeName = "";
        if (info != null) {
            typeName = info.getTypeName();
        } else {
            typeName = "NONE";
        }

        return typeName;
    }

    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    @SuppressLint("LongLogTag")
    public static String getLocalIpAddress(Context context) {
        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();

            while(en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface)en.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();

                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException var5) {
            Log.e("WifiPreference IpAddress", var5.toString());
        }

        return null;
    }


    public static JSONObject parseJson(String jsonstring) throws JSONException {
        JSONTokener jsonParser = new JSONTokener(jsonstring);
        JSONObject jsonObject = (JSONObject)jsonParser.nextValue();
        return jsonObject;
    }

//    public static void logd(String tag, String msg) {
//        if (ENABLE_LOG && msg != null && !msg.equals("")) {
//            Log.d(tag, msg);
//        }
//
//    }

    public static byte[] desEncrypt(byte[] plainText, String KEY) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(KEY.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(1, key, sr);
        byte[] encryptedData = cipher.doFinal(plainText);
        return encryptedData;
    }

    public static byte[] desDecrypt(byte[] encryptText, String KEY) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(KEY.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, key, sr);
        byte[] decryptedData = cipher.doFinal(encryptText);
        return decryptedData;
    }

    public static String objectToJson(Object object) {
        StringBuilder json = new StringBuilder();
        if (object == null) {
            json.append("\"\"");
        } else if (object instanceof String || object instanceof Integer) {
            json.append("\"").append(object.toString()).append("\"");
        }

        return json.toString();
    }

    public static String listToJson(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (list != null && list.size() > 0) {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
                Object obj = var3.next();
                json.append(objectToJson(obj));
                json.append(",");
            }

            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }

        return json.toString();
    }

    public static boolean isAllNumber(String str) {
        for(int i = 0; i < str.length(); ++i) {
            if (!Character.isDigit(str.charAt(0))) {
                return false;
            }
        }

        return true;
    }

    public static String RandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }

        return buf.toString();
    }

    public static String getNowTime() {
        SimpleDateFormat fomat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curdate = new Date(System.currentTimeMillis());
        String str = fomat.format(curdate);
        return str;
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email.trim());
        return m.matches();
    }

    public interface LMgetLocalGoogleAdIdListener {
        void onComplete(int var1, String var2);
    }
}
